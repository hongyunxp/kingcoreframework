/*
 * @(#)AbsolutePathTag.java		    1.00 2004/04/16
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
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.bean.Param;

/**
 * <p>本类的目的是放在jsp页面，生成当前页面的绝对url值。
 * 		例如：<img src='<woo:absolutePath/>/../images/logo.gif />   
 * 	 也可以不用写到每个引用的元素上，而是整个指定当前页面的base href，如：
 * 			<base href="<woo:absolutePath/>" tager="_self">
 *    或者：
 * 		<%
 * 		//wzw:2010-01 页面直接使用${path}，不使用此标签。
 * 		String path = request.getContextPath(); 
 * 		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;//+"/";
 * 		%>
 * 		<head>
 *    		 <base href="<%=basePath%>" tager="_self">
 * 		</head>
 *
 * 		</p>
 * 
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
 
public class AbsolutePathTag extends TagSupport implements ParamParent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String path = null;
	
    protected String server = null;
    
    protected String action = null;
    
    protected Vector params  ;
    
    
	public String getPath() {
        return (this.path);
    }

    public void setPath(String path) {
        this.path = path;
    }
    
	public String getServer() {
        return (this.server);
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    public String getAction(){
    	return (this.action);
    }
    
    public void setAction(String action){
    	this.action = action;
    }
    
    public void setParam(String name, String value){
    
    	if (params==null){
    		params = new Vector();
    	}
    	Param param = new Param(name, value);
    	params.addElement( param ) ;
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
        
        if( getAction() != null && !getAction().equals("") ){
        	sb_uri.append("?Action=");
        	sb_uri.append(getAction());
        	//sb_uri.append("&");        
        }
        //else params
        if (params != null && params.size() > 0) {
            boolean isFirst = false ;
        	if (sb_uri.indexOf("?")<0 )
        	{
        		sb_uri.append('?');
        		isFirst = true ;
        	}
            Enumeration e = params.elements();
            while (e.hasMoreElements()) {
                Param p = (Param) e.nextElement();
                if (!isFirst) {
                    sb_uri.append('&');
                }
                sb_uri.append(((String)p.getName())).append('=').append(((String)p.getValue()));
                isFirst = false;
            }
        }
        
        JspWriter out = pageContext.getOut();
        try {
            out.write(sb_uri.toString());
        } catch (IOException e) {
        }
        
        //log.debug("\nAbsolutePathTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }
    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
		path = null;	
		action = null;
		server = null;
		params = null;
	    super.release();
	}   
}
