package com.kingcore.framework.tag ;


import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kingcore.framework.bean.Param;

/**
 * This class is a custom action for inserting HTML references
 * in a navigation menu. If the action is used in a page
 * with requested with a URI that corresponds to the page
 * attribute, only the HTML text is included, otherwise an
 * HTML reference (<a>...</a>) element is used to enclose the
 * HTML text. The action also encodes the page URI for URL rewriting
 * with possible parameter values URL encoded.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0.1
 */
public class MenuItemTag extends BodyTagSupport implements ParamParent {
    private String page;
    private String html;
    private Vector params;

    /**
     * Sets the page attribute.
     *
     * @param page the page URI value
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Sets the html attribute.
     *
     * @param html the HTML to use for the item
     */
    public int doAfterBody() {
        html = bodyContent.getString();
        return SKIP_BODY;
    }

    /**
     * Adds a parameter name and value. This method is called by param
     * tags in the action body.
     *
     * @param name the parameter name
     * @param value the URL encoded parameter value
     */
    public void setParam(String name, String value) {
        if (params == null) {
            params = new Vector();
        }
        Param param = new Param(name, value);
        params.addElement(param);
    }

    /**
     * Writes the HTML to the current output, enclosed in an
     * HTML reference element (<a>...</a>) if the page attribute
     * doesn't correspond to the current page. If a reference
     * is used, appends possible URL encoded parameters to the main URL,
     * encodes the result for URL rewriting and writes the result
     * to the JspWriter.
     */
    public int doEndTag() throws JspException {
        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();
        String requestURI = request.getServletPath();
        // Convert the specified page URI to a context-relative URI
        String pageURI = toContextRelative(page, requestURI);

        String text = null;
        if (requestURI.equals(pageURI)) {
            text = html;
        }
        else {
            // Add the text as an HTML reference if page is not current page
            String contextPath = request.getContextPath();
            StringBuffer encodedURL =
                new StringBuffer(contextPath + pageURI);
            if (params != null && params.size() > 0) {
                encodedURL.append('?');
                boolean isFirst = true;
                Enumeration e = params.elements();
                while (e.hasMoreElements()) {
                    Param p = (Param) e.nextElement();
                    if (!isFirst) {
                        encodedURL.append('&');
                    }
                    encodedURL.append(p.getName()).append('=').
                        append(p.getValue());
                    isFirst = false;
                }
            }
            HttpServletResponse res =
                (HttpServletResponse) pageContext.getResponse();
            text = "<a href=\"" + res.encodeURL(encodedURL.toString()) +
                "\">" + html + "</a>";
        }
        try {
            JspWriter out = pageContext.getOut();
            out.print(text);
        }
        catch (IOException e) {}
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        page = null;
        html = null;
        params = null;
        super.release();
    }

    /**
     * Returns a page-relative or context-relative path URI as
     * a context-relative URI.
     *
     * @param relURI the page or context-relative URI
     * @param currURI the context-relative URI for the current request
     * @exception JspException if the relURI is invalid
     */
    private String toContextRelative(String relURI, String currURI)
        throws JspException {

        if (relURI.startsWith("/")) {
            // Must already be context-relative
            return relURI;
        }

        String origRelURI = relURI;
        if (relURI.startsWith("./")) {
            // Remove current dir characters
            relURI = relURI.substring(2);
        }

        String currDir = currURI.substring(0, currURI.lastIndexOf("/") + 1);
        StringTokenizer currLevels = new StringTokenizer(currDir, "/");

        // Remove and count all parent dir characters
        int removeLevels = 0;
        while (relURI.startsWith("../")) {
            if (relURI.length() < 4) {
                throw new JspException("Invalid relative URI: " + origRelURI);
            }
            relURI = relURI.substring(3);
            removeLevels++;
        }

        if (removeLevels > currLevels.countTokens()) {
            throw new JspException("Invalid relative URI: " + origRelURI +
                " points outside the context");
        }
        int keepLevels = currLevels.countTokens() - removeLevels;
        StringBuffer newURI = new StringBuffer("/");
        for (int j = 0; j < keepLevels; j++) {
            newURI.append(currLevels.nextToken()).append("/");
        }
        return newURI.append(relURI).toString();
    }

}
