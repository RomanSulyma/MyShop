package net.listener;

import net.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
//At any request, the user writes all the request data to the session
@WebListener
@SuppressWarnings("unchecked")
public class AccountRequestStatisticsListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }

    //called when a request is created and writes an arraylist with action to the session
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest req = ((HttpServletRequest)sre.getServletRequest());
        List<String> actions = (List<String>) req.getSession().getAttribute(Constants.ACCOUNT_ACTIONS_HISTORY);
        if(actions == null) {
            actions = new ArrayList<>();
            req.getSession().setAttribute(Constants.ACCOUNT_ACTIONS_HISTORY, actions);
        }
        actions.add(getCurrentAction(req));
    }
    //directly add the current request data to the session
    private String getCurrentAction(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(req.getMethod()).append(" ").append(req.getRequestURI());
        Map<String, String[]> map = req.getParameterMap();
        if(map != null) {
            boolean first = true;
            for(Map.Entry<String, String[]> entry : map.entrySet()) {
                if(first) {
                    sb.append('?');
                    first = false;
                } else {
                    sb.append('&');
                }
                for(String value : entry.getValue()) {
                    sb.append(entry.getKey()).append('=').append(value).append('&');
                }
                sb.deleteCharAt(sb.length()-1);
            }
        }
        return sb.toString();
    }
}
