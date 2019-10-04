package net.servlet.page;

import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//processes the basket of orders, if empty, then redirects to the main page
@WebServlet("/shopping-cart")
public class ShowShoppingCartController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtils.isCurrentShoppingCartCreated(req)) {
            RoutingUtils.forwardToPage("shopping-cart.jsp", req, resp);
        } else {
            RoutingUtils.redirect("/products", req, resp);
        }
    }
}

