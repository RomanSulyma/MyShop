package net.servlet.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import net.form.ProductForm;
import net.model.ShoppingCart;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

//An abstract method for adding and removing a product from the basket, Create a form, get the current basket, do the processing in the abstract method (add, delete)
//and send a response with the quantity and amount of goods
public abstract class AbstractProductController extends AbstractController {
    private static final long serialVersionUID = 5096979151346608146L;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductForm form = createProductForm(req);
        ShoppingCart shoppingCart = getCurrentShoppingCart(req);
        processProductForm(form, shoppingCart, req, resp);
        if(!SessionUtils.isCurrentShoppingCartCreated(req)) {
            SessionUtils.setCurrentShoppingCart(req, shoppingCart);
        }
        sendResponse(shoppingCart, req, resp);
    }
    //get basket object
    private ShoppingCart getCurrentShoppingCart(HttpServletRequest req) {
        ShoppingCart shoppingCart = SessionUtils.getCurrentShoppingCart(req);
        if(shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        return shoppingCart;
    }


    protected abstract void processProductForm(ProductForm form, ShoppingCart shoppingCart, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;

    protected void sendResponse(ShoppingCart shoppingCart, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject cardStatistics = new JSONObject();
        cardStatistics.put("totalCount", shoppingCart.getTotalCount());
        cardStatistics.put("totalCost", shoppingCart.getTotalCost());
        RoutingUtils.sendJSON(cardStatistics, req, resp);
    }
}
