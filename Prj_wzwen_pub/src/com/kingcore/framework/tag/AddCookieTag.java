package com.kingcore.framework.tag ;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.util.CookieUtils;

/**
 * This class is a custom action for adding a cookie header
 * to the response.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class AddCookieTag extends TagSupport {
    private String name;
    private String value;
    private String maxAgeString;

    /**
     * Sets the cookie name attribute.
     *
     * @param name the name of the cookie
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the cookie value attribute.
     *
     * @param value the value of the cookie
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the cookie maxAge attribute.
     *
     * @param maxAgeString the max age (in seconds) of the cookie
     */
    public void setMaxAgeString(String maxAgeString) {
        this.maxAgeString = maxAgeString;
    }

    /**
     * Creates a cookie and adds it to the response
     */
    public int doEndTag() throws JspException {
        int maxAge = -1;
        if (maxAgeString != null) {
            try {
                maxAge = Integer.valueOf(maxAgeString).intValue();
            }
            catch (NumberFormatException e) {
                throw new JspException("Invalid maxAge: " + e.getMessage());
            }
        }

        CookieUtils.sendCookie(name, value, maxAge,
            (HttpServletResponse) pageContext.getResponse());
        return EVAL_PAGE;      //return SKIP_PAGE ;

    }

    /**
     * Releases all instance variables.  释放所有对象的引用。
     */
    public void release() {
        super.release();
        name = null;
        value = null;
        maxAgeString = null;
        super.release();
    }
}
