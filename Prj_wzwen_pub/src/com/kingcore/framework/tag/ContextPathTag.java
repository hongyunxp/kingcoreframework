/*
 * @(#)ContextPathTag.java		    1.00 2004/04/16
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

/**
 * @version		1.00 2004.04.18
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
 
public class ContextPathTag extends TagSupport
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String path = null ;
	
	public void setPath(String path)
	{
		this.path = path ;
	}
	public String getPath()
	{
		return (this.path) ;
	}
	
	public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String contextpath = request.getContextPath();
        JspWriter out = pageContext.getOut();
        try {
            out.write(contextpath + (getPath().startsWith("/")?"":"/") + getPath() );
            //log.debug("\n"+contextpath + "/" + getPath() );
        } catch (IOException e) {
        }		
        return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * Releases all instance variables.
	 */
	public void release() {
	    path = null;
	    super.release();
	}
}
