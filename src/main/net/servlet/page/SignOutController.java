package net.servlet.page;

import net.servlet.AbstractController;
import net.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//on logout, deletes the session and redirects to the main page
@WebServlet("/sign-out")
public class SignOutController extends AbstractController {
    private static final long serialVersionUID = -8146770694377066438L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        RoutingUtils.redirect("/products", req, resp);
    }
}
