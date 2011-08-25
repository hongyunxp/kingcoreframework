package com.kingcore.framework.tag ;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;

/**
 * This class is a custom action for sending a redirect request, 
 * with possible parameter values URL encoded and the complete URL
 * encoded for URL rewriting (session tracking).
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class RedirectTag extends TagSupport implements ParamParent {
    
    private String page;
    private Vector params;
    protected String server = null;
    Log log = null ;

    /**
     * Sets the page attribute.
     *
     * @param page the page URL to redirect to
     */
    public void setPage(String page) {
        this.page = page;
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
     * encodes the result for URL rewriting. Clears the out buffer
     * and sets the redirect response headers. Returns SKIP_PAGE
     * to abort the processing of the rest of the page.
     */
    public int doEndTag() throws JspException {
        StringBuffer encodedURL = new StringBuffer("");
        String uri = null ;
        int  pos ;
                
        /*
        //add   
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String serverName = request.getServerName();
        encodedURL.append(request.getScheme());
        encodedURL.append("://");  
        if (this.server != null) {
            serverName = this.server;
        }
        encodedURL.append(serverName);
        if ("http".equals(request.getScheme()) && (80 == request.getServerPort())) {
            ;
        } else if ("https".equals(request.getScheme()) && (443 == request.getServerPort())) {
            ;
        } else {
            encodedURL.append(":");
            encodedURL.append(request.getServerPort());
        }
        encodedURL.append(request.getContextPath());
        System.out.print( "getContentLength:"+request.getContentLength()+
                          "getPathInfo:"     +request.getPathInfo()+
        	              "getRealPath:"     +request.getRealPath("tmp_welcome.jsp")+
        	              "getRemoteAddr"    +request.getRemoteAddr()+
        	              "getRequestURI"    +request.getRequestURI()+
        				  "getServletPath"   +request.getServletPath() ) ;
        encodedURL.append("/");
        */
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        uri = request.getRequestURI() ;
        pos = uri.lastIndexOf("/") ;
        uri = uri.substring(0, pos+1 ) ;        
        encodedURL.append(uri) ;
        
        encodedURL.append(page);

        if (params != null && params.size() > 0) {
            encodedURL.append('?');
            boolean isFirst = true;
            Enumeration e = params.elements();
            while (e.hasMoreElements()) {
                Param p = (Param) e.nextElement();
                if (!isFirst) {
                    encodedURL.append('&');
                }
                encodedURL.append(p.getName()).append('=').append(p.getValue());
                isFirst = false;
            }
        }
        try {
            JspWriter out = pageContext.getOut();
            out.clear();
            
            HttpServletResponse res = (HttpServletResponse) pageContext.getResponse();
            System.out.print( "to:"+res.encodeURL(encodedURL.toString()) );
            res.sendRedirect(res.encodeURL(encodedURL.toString()));
        }
        catch (IOException e) {}
        return SKIP_PAGE;
    }
    
    /**
     * Releases all instance variables.
     */
    public void release() {
        page = null;
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
