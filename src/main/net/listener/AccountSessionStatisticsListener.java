package net.listener;

import net.Constants;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

//Lists all user actions when destroying a session
@WebListener
public class AccountSessionStatisticsListener implements HttpSessionListener {
    public AccountSessionStatisticsListener() {
        super();
    }
    //выполняет действия при создании сессии
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }
    //performs actions on session destruction
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        List<String> actions = (List<String>) se.getSession().getAttribute(Constants.ACCOUNT_ACTIONS_HISTORY);
        if (actions != null) {
            logCurrentActionHistory(se.getSession().getId(), actions);
        }
    }
    //logs action history
    private void logCurrentActionHistory(String sessionId, List<String> actions) {
        System.out.println(sessionId + " ->\n\t" + String.join("\n\t", actions));
    }
}
