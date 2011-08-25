package com.kingcore.framework.tag ;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class is a custom action for setting response headers
 * that prevent the page from being cached by a browser or
 * proxy server.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class NoCacheTag extends TagSupport {
    /**
     * Sets "no cache" response headers
     */
    public int doEndTag() throws JspException {
        HttpServletResponse response =
            (HttpServletResponse) pageContext.getResponse();
        response.addHeader("Pragma", "No-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addDateHeader("Expires", 1);
        return EVAL_PAGE;
    }
}
