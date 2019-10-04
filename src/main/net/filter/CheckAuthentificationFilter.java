package net.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.Constants;
import net.util.RoutingUtils;
import net.util.SessionUtils;
import net.util.UrlUtils;
import net.util.WebUtils;

//checks if user is logged in to all requests in the system, also saves the requested URL to the session
@WebFilter(filterName = "CheckAuthentificationFilter")
public class CheckAuthentificationFilter extends AbstractFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (SessionUtils.isCurrentAccountCreated(req)) {
            chain.doFilter(req, resp);
        } else {
            String requestUrl = WebUtils.getCurrentRequestUrl(req);
            if (UrlUtils.isAjaxUrl(requestUrl)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("401");
            } else {
                req.getSession().setAttribute(Constants.SUCCESS_REDIRECT_URL_AFTER_SIGNIN, requestUrl);
                RoutingUtils.redirect("/sign-in", req, resp);
            }
        }
    }
}
