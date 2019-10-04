package net.servlet.page;

import net.Constants;
import net.entity.Order;
import net.model.CurrentAccount;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/my-orders")
public class MyOrdersController extends AbstractController {
    private static final long serialVersionUID = -1782066337808445826L;
    //Requests all user orders, as well as the number of pages and puts them in the request parameters
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrentAccount currentAccount = SessionUtils.getCurrentAccount(req);
        List<Order> orders = getOrderService().listMyOrders(currentAccount, 1, Constants.ORDERS_PER_PAGE);
        req.setAttribute("orders", orders);
        int orderCount = getOrderService().countMyOrders(currentAccount);
        req.setAttribute("pageCount", getPageCount(orderCount, Constants.ORDERS_PER_PAGE));
        RoutingUtils.forwardToPage("my-orders.jsp", req, resp);
    }
}
