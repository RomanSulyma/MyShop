package net.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.Constants;
import net.entity.Order;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

//processes ajax request to load the list of orders per page and adds them to the request
@WebServlet("/ajax/html/more/my-orders")
public class MyOrdersMoreController extends AbstractController {
    private static final long serialVersionUID = -2651974520717714088L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orders = getOrderService().listMyOrders(SessionUtils.getCurrentAccount(req), getPage(req), Constants.ORDERS_PER_PAGE);
        req.setAttribute("orders", orders);
        RoutingUtils.forwardToPage("my-orders.jsp", req, resp);
    }
}
