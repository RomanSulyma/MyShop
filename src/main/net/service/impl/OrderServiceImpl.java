package net.service.impl;

import net.entity.Account;
import net.entity.Order;
import net.entity.OrderItem;
import net.entity.Products;
import net.exception.AccessDeniedException;
import net.exception.InternalServerErrorException;
import net.exception.ResourceNotFoundException;
import net.form.ProductForm;
import net.jdbc.JDBCUtils;
import net.jdbc.ResultSetHandler;
import net.jdbc.ResultSetHandlerFactory;
import net.model.CurrentAccount;
import net.model.ShoppingCart;
import net.model.ShoppingCartItem;
import net.model.SocialAccount;
import net.service.OrderService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

import javax.sql.DataSource;
import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

//implementation of work with orders
public class OrderServiceImpl implements OrderService {

    private static final ResultSetHandler<Products> productsRSH = ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    private static final ResultSetHandler<Account> accountRSH = ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ACCOUNT_RESULT_SET_HANDLER);
    private final ResultSetHandler<Order> orderRSH = ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<OrderItem>> orderItemListRSH = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_ITEM_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<Order>> ordersRSH = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
    private final ResultSetHandler<Integer> countRSH = ResultSetHandlerFactory.getCountResultSetHandler();

    private final DataSource dataSource;
    //private final String rootDir;

    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String host;
    private String fromAddress;

    public OrderServiceImpl(DataSource dataSource, ServiceManager serviceManager) {
        super();

        this.dataSource = dataSource;
        //this.rootDir = serviceManager.getApplicationProperty("app.avatar.root.dir");

        this.smtpHost = serviceManager.getApplicationProperty("email.smtp.server");
        this.smtpPort = serviceManager.getApplicationProperty("email.smtp.port");
        this.smtpUsername = serviceManager.getApplicationProperty("email.smtp.username");
        this.smtpPassword = serviceManager.getApplicationProperty("email.smtp.password");
        this.host = serviceManager.getApplicationProperty("app.host");
        this.fromAddress = serviceManager.getApplicationProperty("email.smtp.fromAddress");
    }

    //executing a query to the database searches for the product by its id
    @Override
    public void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart) {
        try (Connection c = dataSource.getConnection()) {
            Products product = JDBCUtils.select(c, "select p.*, c.name as category, pr.name as producer from product p , producer pr , category c where c.id = p.id_category and pr.id = p.id_producer and p.id = ?", productsRSH, productForm.getIdProduct());
            if(product == null)
            {
                throw new InternalServerErrorException("Product not found" + productForm.getIdProduct());
            }
            shoppingCart.addProduct(product,productForm.getCount());
        } catch (SQLException | ValidationException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage(), e);
        }
    }
    //Deletes a product from ShoppingCart
    @Override
    public void removeProductFromShoppingCart(ProductForm form, ShoppingCart shoppingCart) {
        shoppingCart.removeProduct(form.getIdProduct(), form.getCount());
    }
    //serialize the contents of the basket into a string and return String
    @Override
    public String serializeShoppingCart(ShoppingCart shoppingCart) {
        StringBuilder res = new StringBuilder();
        for (ShoppingCartItem item : shoppingCart.getItems()) {
            res.append(item.getProduct().getId()).append("-").append(item.getCount()).append("|");
        }
        if (res.length() > 0) {
            res.deleteCharAt(res.length() - 1);
        }
        return res.toString();
    }
    //deserializes the basket from the string and returns the ShoppingCart object
    @Override
    public ShoppingCart deserializeShoppingCart(String string) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String[] items = string.split("\\|");
        for (String item : items) {
            try {
                String data[] = item.split("-");
                int idProduct = Integer.parseInt(data[0]);
                int count = Integer.parseInt(data[1]);
                addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return shoppingCart.getItems().isEmpty() ? null : shoppingCart;
    }
    //checks for a user in the database and returns Account, if not found, then adds data to the database
    public CurrentAccount authentificate(SocialAccount socialAccount) {
        try (Connection c = dataSource.getConnection()) {
            Account account = JDBCUtils.select(c, "select * from account where email=?", accountRSH, socialAccount.getEmail());
            if (account == null) {
                //String uniqFileName = UUID.randomUUID().toString()+".jpg";
                //Path filePathToSave = Paths.get(rootDir+"/"+uniqFileName);
                //downloadAvatar(socialAccount.getAvatarUrl(), filePathToSave);
                account = JDBCUtils.insert(c, "insert into account values (nextval('account_seq'),?,?)", accountRSH, socialAccount.getName(), socialAccount.getEmail()/*, "/media/avatar/"+uniqFileName*/);
                c.commit();
            }
            return account;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }/*catch (IOException e) {
            throw new InternalServerErrorException("Can't process avatar link", e);
        }*/

    }
    //download user avatar to media directory
    /*protected void downloadAvatar(String avatarUrl, Path filePathToSave) throws IOException {
        try(InputStream in = new URL(avatarUrl).openStream()) {
            System.out.println(filePathToSave);
            Files.copy(in, filePathToSave);
        }
    }*/
    //first adds the order to the database, and then with the help of batch adds the entire contents of the basket to OrderItem
    @Override
    public long makeOrder(ShoppingCart shoppingCart, CurrentAccount currentAccount) {
        if (shoppingCart == null || shoppingCart.getItems().isEmpty()) {
            throw new InternalServerErrorException("shoppingCart is null or empty");
        }
        try (Connection c = dataSource.getConnection()) {
            Order order = JDBCUtils.insert(c, "insert into \"order\" values(nextval('order_seq'),?,?)", orderRSH,
                    currentAccount.getId(), new Timestamp(System.currentTimeMillis()));
            JDBCUtils.insertBatch(c, "insert into order_item values(nextval('order_item_seq'),?,?,?)",
                    toOrderItemParameterList(order.getId(), shoppingCart.getItems()));
            c.commit();
            sendEmail(currentAccount.getEmail(), order);
            return order.getId();
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }
    //send email with order
    private void sendEmail(String emailAddress, Order order) {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setCharset("utf-8");
            email.setHostName(smtpHost);
            email.setSSLOnConnect(true);
            email.setSslSmtpPort(smtpPort);
            email.setFrom(fromAddress);
            email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
            email.setSubject("New order");
            email.setMsg("Hi :) you have new order: \n" + host + "/order?id="+order.getId());
            email.addTo(emailAddress);
            email.send();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    //contains a collection of mass parameters that will be substituted into the parameters of the query to the database to add (batch)
    private List<Object[]> toOrderItemParameterList(long idOrder, Collection<ShoppingCartItem> items) {
        List<Object[]> parametersList = new ArrayList<>();
        for (ShoppingCartItem item : items) {
            parametersList.add(new Object[] { idOrder, item.getProduct().getId(), item.getCount() });
        }
        return parametersList;
    }
    //checks the availability of the order and whether the order belongs to the user, after which he makes a request to the database and receives an array of OrderItem
    @Override
    public Order findOrderById(long id, CurrentAccount currentAccount) {
        try (Connection c = dataSource.getConnection()) {
            Order order = JDBCUtils.select(c, "select * from \"order\" where id=?", orderRSH, id);
            if (order == null) {
                throw new ResourceNotFoundException("Order not found by id: " + id);
            }
            if (!order.getIdAccount().equals(currentAccount.getId())) {
                throw new AccessDeniedException("Account with id=" + currentAccount.getId() + " is not owner for order with id=" + id);
            }
            List<OrderItem> list = JDBCUtils.select(c,
                    "select o.id as oid, o.id_order as id_order, o.id_product, o.count, p.*, c.name as category, pr.name as producer from order_item o, product p, category c, producer pr "
                            + "where pr.id=p.id_producer and c.id=p.id_category and o.id_product=p.id and o.id_order=?",
                    orderItemListRSH, id);
            order.setItems(list);
            return order;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }
    //Finds a list of all user orders and returns an order sheet
    @Override
    public List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit) {
        int offset = (page - 1) * limit;
        try (Connection c = dataSource.getConnection()) {
            List<Order> orders = JDBCUtils.select(c, "select * from \"order\" where id_account=? order by id desc limit ? offset ?", ordersRSH, currentAccount.getId(), limit, offset);
            return orders;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }
    //counts the number of all user orders for pagination
    @Override
    public int countMyOrders(CurrentAccount currentAccount) {
        try (Connection c = dataSource.getConnection()) {
            return JDBCUtils.select(c, "select count(*) from \"order\" where id_account=?", countRSH, currentAccount.getId());
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }


}
