package com.kingcore.framework.tag ;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class is a custom action for encoding URLs for URL rewriting
 * (session tracking) with possible parameter values URL encoded.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class EncodeURLTag extends TagSupport implements ParamParent {
    private String url;
    private Vector params;


    /**
     * Sets the url attribute.
     * 
     * @param url the page URL value
     */
    public void setUrl(String url) {
        this.url = url;
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
     * Override the default implementation so that possible 
     * param actions in the body are processed.
     */
    public int doStartTag() {
        // Reset per-use state set by nested elements
        params = null;

        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * Appends possible URL encoded parameters to the main URL,
     * encodes the result for URL rewriting and writes the result
     * to the JspWriter.
     */
    public int doEndTag() throws JspException {
        StringBuffer encodedURL = new StringBuffer(url);
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
        try {
            HttpServletResponse res = 
                (HttpServletResponse) pageContext.getResponse();
            JspWriter out = pageContext.getOut();
            out.print(res.encodeURL(encodedURL.toString()));
        }
        catch (IOException e) {}
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        url = null;
        params = null;
        super.release();
    }
    
    /**
     * This is a helper class that holds the name and value of a
     * parameter.
     */
    class Param {
        private String name;
        private String value;
        
        public Param(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return name;
        }
        
        public String getValue() {
            return value;
            
        }
    }
}
