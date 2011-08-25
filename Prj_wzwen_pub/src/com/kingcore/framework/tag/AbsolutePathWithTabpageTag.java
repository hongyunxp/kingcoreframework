/*
 * @(#)AbsolutePathWithTabpageTag.java		    1.00 2004/04/16
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.kingcore.framework.core.filter.EncodeFilter;

/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
 
public class AbsolutePathWithTabpageTag extends TagSupport
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger( EncodeFilter.class);
	protected String path = null;	
    protected String server = null;
    protected String tabpage = null;
    protected String requestpage = null;
    
    /*
     * get() and set() method for tabpage
     */     
	public String getTabpage() {
        return (this.tabpage);
    }
    public void setTabpage(String tabpage) {
        this.tabpage = tabpage;
    }
	
    /*
     * get() and set() method for requestpage
     */
	public String getRequestpage() {
        return (this.requestpage);
    }
    public void setRequestpage(String requestpage) {
        this.requestpage = requestpage;
    }
    
    /*
     * get() and set() method for path
     */
    public String getPath() {
        return (this.path);
    }
    public void setPath(String path) {
        this.path = path;
    }
    
    /*
     * get() and set() method for path
     */
	public String getServer() {
        return (this.server);
    }
    public void setServer(String server) {
        this.server = server;
    }
   
    public int doStartTag() throws JspException {    	
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer sb_uri = new StringBuffer();
        String serverName = request.getServerName();
        sb_uri.append(request.getScheme());
        sb_uri.append("://");        

        if (this.server != null) {
            serverName = this.server;
        }
        sb_uri.append(serverName);
        if ("http".equals(request.getScheme()) && (80 == request.getServerPort())) {
            ;
        } else if ("https".equals(request.getScheme()) && (443 == request.getServerPort())) {
            ;
        } else {
            sb_uri.append(":");
            sb_uri.append(request.getServerPort());
        }
        sb_uri.append(request.getContextPath());
        sb_uri.append("/");
        sb_uri.append(path);
        sb_uri.append("?");
        
        if( getTabpage() != null && !getTabpage().equals("") ){
        	sb_uri.append("tabpage=");
        	sb_uri.append(getTabpage());
        	sb_uri.append("&");        
        }
        if( getRequestpage() != null && !getRequestpage().equals("") ){
        	sb_uri.append("requestpage=");
        	sb_uri.append(getRequestpage());
        	sb_uri.append("&");        
        }
        
        JspWriter out = pageContext.getOut();
        try {
            out.write(sb_uri.toString());
        } catch (IOException e) {
        }
        
        log.info("\nAbsolutePathWithTabpageTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }
    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
		path = null;	
		server = null;
		tabpage = null;
		requestpage = null;
	    super.release();
	}
}
