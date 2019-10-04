package net.servlet.page;

import net.Constants;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sign-in")
public class SignInController extends AbstractController {
    private static final long serialVersionUID = -8146770694377066438L;
    //It processes the user’s login, if logged in it will transfer control to my-orders, if not, then to the login page
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtils.isCurrentAccountCreated(req)) {
            RoutingUtils.redirect("/my-orders", req, resp);
        } else {
            RoutingUtils.forwardToPage("sign-in.jsp", req, resp);
        }
    }

    //After clicking the button, the Sign-in either redirects to your personal account, or if it is not logged in, then to the login via the social network, get the target parameter and send it to the session
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtils.isCurrentAccountCreated(req)) {
            RoutingUtils.redirect("/my-orders", req, resp);
        } else {
            String targetUrl = req.getParameter("target");
            if (targetUrl != null) {
                req.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN, targetUrl);
            }
            RoutingUtils.redirect(getSocialService().getAuthorizeUrl(), req, resp);
        }
    }
}
