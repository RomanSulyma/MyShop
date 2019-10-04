package net.servlet.page;

import net.Constants;
import net.model.CurrentAccount;
import net.model.SocialAccount;
import net.servlet.AbstractController;
import net.util.RoutingUtils;
import net.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet("/from-social")
public class FromSocialController extends AbstractController {
    private static final long serialVersionUID = -8146770694377066438L;

    //It receives a response from the social network and either registers the user or not, by obtaining the code parameter from the social network and, if so, it proceeds to check for the presence of the user in the database
    //Then it writes everything to the session and redirects to the URL that the user wanted to go to
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if (code != null) {
            SocialAccount socialAccount = getSocialService().getSocialAccount(code);
            CurrentAccount currentAccount = getOrderService().authentificate(socialAccount);
            SessionUtils.setCurrentAccount(req, currentAccount);
            redirectToSuccessPage(req, resp);
        } else {
            if(req.getSession().getAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN) != null){
                req.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN);
            }
            RoutingUtils.redirect("/sign-in", req, resp);
        }
    }
    //takes the URL from the session and in case of a successful login, redirects the user to the given URL by decoding it
    protected void redirectToSuccessPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String targetUrl = (String) req.getSession().getAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN);
        if (targetUrl != null) {
            req.getSession().removeAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN);
            RoutingUtils.redirect(URLDecoder.decode(targetUrl, "UTF-8"), req, resp);
        } else {
            RoutingUtils.redirect("/my-orders", req, resp);
        }
    }
}
