package net.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.entity.*;


public final class ResultSetHandlerFactory {
    //parses the response from the database and returns the finished product object that will be sent to the JSP page on display
    public final static ResultSetHandler<Products> PRODUCT_RESULT_SET_HANDLER = new ResultSetHandler<Products>() {
        @Override
        public Products handle(ResultSet rs) throws SQLException {
            Products p = new Products();
            p.setCategory(rs.getString("category"));
            p.setDescription(rs.getString("description"));
            p.setId(rs.getInt("id"));
            p.setImageLink(rs.getString("image_link"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setProducer(rs.getString("producer"));
            return p;
        }
    };
    //parses the response from the database and returns the ready object of the category that will be sent to the JSP page on display
    public final static ResultSetHandler<Category> CATEGORY_RESULT_SET_HANDLER = new ResultSetHandler<Category>() {
        @Override
        public Category handle(ResultSet rs) throws SQLException {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            category.setUrl(rs.getString("url"));
            category.setProductCount(rs.getInt("product_count"));
            return category;
        }
    };
    //parses the response from the database and returns the ready object of the manufacturer that will be sent to the JSP page on display
    public final static ResultSetHandler<Producers> PRODUCER_RESULT_SET_HANDLER = new ResultSetHandler<Producers>() {
        @Override
        public Producers handle(ResultSet rs) throws SQLException {
            Producers producers = new Producers();
            producers.setId(rs.getInt("id"));
            producers.setName(rs.getString("name"));
            producers.setProductCount(rs.getInt("product_count"));
            return producers;
        }
    };
    //parses requests for the amount of goods from the database
    public final static ResultSetHandler<Integer> getCountResultSetHandler() {
        return new ResultSetHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        };
    }
    //parses the response from the database and returns one result
    public final static <T> ResultSetHandler<T> getSingleResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) {
        return new ResultSetHandler<T>() {
            @Override
            public T handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return oneRowResultSetHandler.handle(rs);
                } else {
                    return null;
                }
            }
        };
    }
    //parses the response from the database and returns a result sheet if there is more than one object in the response
    public final static <T> ResultSetHandler<List<T>> getListResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) {
        return new ResultSetHandler<List<T>>() {
            @Override
            public List<T> handle(ResultSet rs) throws SQLException {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(oneRowResultSetHandler.handle(rs));
                }
                return list;
            }
        };
    }
    //Creates an Account Object from data from the database
    public final static ResultSetHandler<Account> ACCOUNT_RESULT_SET_HANDLER = new ResultSetHandler<Account>() {
        @Override
        public Account handle(ResultSet rs) throws SQLException {
            Account a = new Account();
            a.setId(rs.getInt("id"));
            a.setEmail(rs.getString("email"));
            a.setName(rs.getString("name"));
            //a.setAvatarUrl(rs.getString("avatar_url"));
            return a;
        }
    };
    //ResultSet handler for the OrderItem table
    public final static ResultSetHandler<OrderItem> ORDER_ITEM_RESULT_SET_HANDLER = new ResultSetHandler<OrderItem>() {
        @Override
        public OrderItem handle(ResultSet rs) throws SQLException {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getLong("oid"));
            orderItem.setCount(rs.getInt("count"));
            orderItem.setIdOrder(rs.getLong("id_order"));
            Products p = PRODUCT_RESULT_SET_HANDLER.handle(rs);
            orderItem.setProduct(p);
            return orderItem;
        }
    };
    //ResultSet handler for the Order table
    public final static ResultSetHandler<Order> ORDER_RESULT_SET_HANDLER = new ResultSetHandler<Order>() {
        @Override
        public Order handle(ResultSet rs) throws SQLException {
            Order o = new Order();
            o.setId(rs.getLong("id"));
            o.setCreated(rs.getTimestamp("created"));
            o.setIdAccount(rs.getInt("id_account"));
            return o;
        }
    };

    private ResultSetHandlerFactory() {
    }
}
