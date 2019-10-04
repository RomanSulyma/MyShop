package net.servlet.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.form.ProductForm;
import net.model.ShoppingCart;
import net.util.SessionUtils;
//remove the product from the basket, if after removing the basket does not contain products, then delete the basket
//if not empty, then serializes the state of the basket and updates the cookie from the client
@WebServlet("/ajax/json/product/remove")
public class RemoveProductController extends AbstractProductController {
    private static final long serialVersionUID = -3046216247699203961L;


    @Override
    protected void processProductForm(ProductForm form, ShoppingCart shoppingCart, HttpServletRequest req, HttpServletResponse resp) {
        getOrderService().removeProductFromShoppingCart(form, shoppingCart);
        if (shoppingCart.getItems().isEmpty()) {
            SessionUtils.clearCurrentShoppingCart(req, resp);
        } else {
            String cookieValue = getOrderService().serializeShoppingCart(shoppingCart);
            SessionUtils.updateCurrentShoppingCartCookie(cookieValue, resp);
        }
    }
}
