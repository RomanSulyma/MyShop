package net.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.json.JSONObject;

import net.exception.AbstractException;
import net.exception.AccessDeniedException;
import net.exception.InternalServerErrorException;
import net.exception.ResourceNotFoundException;
import net.exception.ValidationException;
import net.util.RoutingUtils;
import net.util.UrlUtils;
//error handler, starts first and makes logs, in case of an error redirects to error.jsp
@WebFilter(filterName = "ErrorHandlerFilter")
public class ErrorHandlerFilter extends AbstractFilter {
    private static final String INTERNAL_ERROR = "Internal error";

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(req, new ThrowExceptionInsteadOnSendErrorResponse(resp));
        } catch (Throwable th) {
            String requestUrl = req.getRequestURI();

            handleException(requestUrl, th, req, resp);
        }
    }
    //determines the status error code
    private int getStatusCode(Throwable th) {
        if (th instanceof AbstractException) {
            return (((AbstractException) th).getCode());
        } else {
            return (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    //determines the type of exception and sends the corresponding error response by the status code
    private void handleException(String requestUrl, Throwable th, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int statusCode = getStatusCode(th);
        resp.setStatus(statusCode);
        if (UrlUtils.isAjaxJsonUrl(requestUrl)) {
            JSONObject json = new JSONObject();
            json.put("message", th instanceof ValidationException ? th.getMessage() : INTERNAL_ERROR);
            RoutingUtils.sendJSON(json, req, resp);
        } else if (UrlUtils.isAjaxHtmlUrl(requestUrl)) {
            RoutingUtils.sendHTMLFragment(INTERNAL_ERROR, req, resp);
        } else {
            req.setAttribute("statusCode", statusCode);
            RoutingUtils.forwardToError("error.jsp", req, resp);
        }
    }
    // overrides response from server to spoof output page
    private static class ThrowExceptionInsteadOnSendErrorResponse extends HttpServletResponseWrapper {
        public ThrowExceptionInsteadOnSendErrorResponse(HttpServletResponse response) {
            super(response);
        }
        //Выбрасываем код ошибки
        @Override
        public void sendError(int sc) throws IOException {
            sendError(sc, INTERNAL_ERROR);
        }
        //Throw an error code with a message
        @Override
        public void sendError(int sc, String msg) throws IOException {
            switch (sc) {
                case 403: {
                    throw new AccessDeniedException(msg);
                }
                case 404: {
                    throw new ResourceNotFoundException(msg);
                }
                case 400: {
                    throw new ValidationException(msg);
                }
                default:
                    throw new InternalServerErrorException(msg);
            }
        }
    }
}
