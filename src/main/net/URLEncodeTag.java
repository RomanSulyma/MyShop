package net;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

//Decodes the current URL and stores the result in a PageContext
public class URLEncodeTag extends SimpleTagSupport {
    private String var;
    private String url;

    @Override
    public void doTag() throws JspException, IOException {
        String encodedUrl = URLEncoder.encode(url, "UTF-8");
        getJspContext().setAttribute(var, encodedUrl);
    }
    public void setVar(String var) {
        this.var = var;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
