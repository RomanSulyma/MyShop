package net.service;

import net.entity.Order;
import net.form.ProductForm;
import net.model.CurrentAccount;
import net.model.ShoppingCart;
import net.model.SocialAccount;

import java.util.List;

//contains methods for working with orders
public interface OrderService {

    void addProductToShoppingCart(ProductForm productForm , ShoppingCart shoppingCart);

    void removeProductFromShoppingCart(ProductForm form, ShoppingCart shoppingCart);

    String serializeShoppingCart(ShoppingCart shoppingCart);

    ShoppingCart deserializeShoppingCart(String string);

    CurrentAccount authentificate(SocialAccount socialAccount);

    long makeOrder(ShoppingCart shoppingCart , CurrentAccount currentAccount);

    Order findOrderById(long id, CurrentAccount currentAccount);

    List<Order> listMyOrders(CurrentAccount currentAccount, int page, int limit);

    int countMyOrders(CurrentAccount currentAccount);

}
