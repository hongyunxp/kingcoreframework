package com.kingcore.framework.tag ;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class is a custom action for checking if a session
 * contains information about an authenticated user.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0.1
 */
public class ValidateSessionTag extends TagSupport {
    private String name;
    private String errorMsg;
    private String loginPage;

    /**
     * Sets the name of the session object to look for.
     *
     * @param name the name of the session object to look for
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the error message to display on the login page.
     *
     * @param errorMsg the error message to display on the login page
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Sets the login page URL
     *
     * @param loginPage the login page URL
     */
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    /**
     * Looks for the specified session bean. If not found, forwards the
     * request to the specifed error page with the parameters origURL and
     * errorMsg.
     */
    public int doEndTag() throws JspException {
        if (pageContext.getAttribute(name, PageContext.SESSION_SCOPE) == null)
        {
            HttpServletRequest request =
                 (HttpServletRequest) pageContext.getRequest();
            String origURL =
                 HttpUtils.getRequestURL(request).toString();
	        String queryString = request.getQueryString();
	    if (queryString != null) {
		    origURL += "?" + queryString;
	        }
	    String loginURL = loginPage + "?origURL=" +
                URLEncoder.encode(origURL) +
                "&errorMsg=" + URLEncoder.encode(errorMsg);
            try {
                pageContext.forward(loginURL);
            }
            catch (IOException e) {
                throw new JspException("IOException when forwarding to loginPage: " +
                    e.getMessage());
            }
            catch (ServletException e) {
                throw new JspException("ServletException when forwarding to loginPage: " +
                    e.getMessage());
            }
        // 不再继续处理页面其他内容
            return SKIP_PAGE;
        }
        // 继续处理页面其他内容
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        name =  null;
        errorMsg = null;
        loginPage = null;
        super.release();
    }
}
