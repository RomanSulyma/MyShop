package net.util;

import net.Constants;
import net.model.CurrentAccount;
import net.model.ShoppingCart;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtils {
    public static ShoppingCart getCurrentShoppingCart(HttpServletRequest req) {
        ShoppingCart shoppingCart = (ShoppingCart) req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            setCurrentShoppingCart(req, shoppingCart);
        }
        return shoppingCart;
    }
    //checks for a basket
    public static boolean isCurrentShoppingCartCreated(HttpServletRequest req) {
        return req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART) != null;
    }
    //sets the basket to this user
    public static void setCurrentShoppingCart(HttpServletRequest req, ShoppingCart shoppingCart) {
        req.getSession().setAttribute(Constants.CURRENT_SHOPPING_CART, shoppingCart);
    }
    //clear basket by deleting cookies
    public static void clearCurrentShoppingCart(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute(Constants.CURRENT_SHOPPING_CART);
        WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, resp);
    }
    //find cookies for this basket
    public static Cookie findShoppingCartCookie(HttpServletRequest req) {
        return WebUtils.findCookie(req, Constants.Cookie.SHOPPING_CART.getName());
    }
    //sets a new cookie for this basket
    public static void updateCurrentShoppingCartCookie(String cookieValue, HttpServletResponse resp) {
        WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), cookieValue,
                Constants.Cookie.SHOPPING_CART.getTtl(), resp);
    }
    //Get the current account data from the session
    public static CurrentAccount getCurrentAccount(HttpServletRequest req) {
        return (CurrentAccount) req.getSession().getAttribute(Constants.CURRENT_ACCOUNT);
    }
    //Sets the current account data to the session
    public static void setCurrentAccount(HttpServletRequest req, CurrentAccount currentAccount) {
        req.getSession().setAttribute(Constants.CURRENT_ACCOUNT, currentAccount);
    }
    //Checks if a user account has been created in the session
    public static boolean isCurrentAccountCreated(HttpServletRequest req) {
        return getCurrentAccount(req) != null;
    }

    private SessionUtils() {
    }
}
