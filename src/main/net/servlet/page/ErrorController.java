package net.servlet.page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.servlet.AbstractController;
import net.util.RoutingUtils;

import java.io.IOException;

//Set the error code and redirects to error.jsp
@WebServlet("/error")
public class ErrorController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        RoutingUtils.forwardToError("error.jsp", req, resp);

    }
}