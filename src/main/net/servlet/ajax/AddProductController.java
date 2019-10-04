package net.servlet.ajax;

import net.form.ProductForm;
import net.model.ShoppingCart;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//processes the addition of the product to the basket, which receives a new cookie value and updates it on the client
@WebServlet("/ajax/json/product/add")
public class AddProductController extends AbstractProductController {
    private static final long serialVersionUID = -3046216247699203961L;

    @Override
    protected void processProductForm(ProductForm form, ShoppingCart shoppingCart, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getOrderService().addProductToShoppingCart(form, shoppingCart);
        String cookieValue = getOrderService().serializeShoppingCart(shoppingCart);
        SessionUtils.updateCurrentShoppingCartCookie(cookieValue, resp);
    }
}
