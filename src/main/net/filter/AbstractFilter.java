package net.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.util.UrlUtils;

public abstract class AbstractFilter implements Filter {
    //check if the resource is a servlet, then the inheritor filters will work, if not, they will be skipped, this method cannot be overridden
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        if(UrlUtils.isMediaUrl(url) || UrlUtils.isStaticUrl(url)) {
            chain.doFilter(request, response);
        } else {
            doFilter(req, resp, chain);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;


}
