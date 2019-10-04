package net.servlet.page;

import net.entity.Order;
import net.model.ShoppingCart;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order")
public class OrderController extends AbstractController {


        private static final String CURRENT_MESSAGE = "CURRENT_MESSAGE";
        //It finds the order ID and the current basket, then adds the order to the database and clears the current basket and cookies in the session, after which the redirect to doGet
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ShoppingCart shoppingCart = SessionUtils.getCurrentShoppingCart(req);
            long idOrder = getOrderService().makeOrder(shoppingCart, SessionUtils.getCurrentAccount(req));
            SessionUtils.clearCurrentShoppingCart(req, resp);
            req.getSession().setAttribute(CURRENT_MESSAGE, "Order created successfully. Please wait for our reply.");
            RoutingUtils.redirect("/order?id=" + idOrder, req, resp);
        }
        //extracts the message from the session and puts it in request, order.jsp goes, also collects the order from the database and adds it to the request for the current user
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String message = (String) req.getSession().getAttribute(CURRENT_MESSAGE);
            req.getSession().removeAttribute(CURRENT_MESSAGE);
            req.setAttribute(CURRENT_MESSAGE, message);
            Order order = getOrderService().findOrderById(Long.parseLong(req.getParameter("id")), SessionUtils.getCurrentAccount(req));
            req.setAttribute("order", order);
            RoutingUtils.forwardToPage("order.jsp", req, resp);
        }

    }

