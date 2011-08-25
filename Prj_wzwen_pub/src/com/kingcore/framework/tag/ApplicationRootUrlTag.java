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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>本类的目的是放在jsp页面，生成当前页面的绝对url值。
 * 		例如：<img src='<woo:absolutePath/>/../images/logo.gif />   
 * 	 也可以不用写到每个引用的元素上，而是整个指定当前页面的base href，如：
 * 			<base href="<woo:applicationRootUrl/>" tager="_self">
 * 				返回如：http://192.168.0.2:8080/pda/
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
 
 
public class ApplicationRootUrlTag extends TagSupport
{

    public int doStartTag() throws JspException {    	
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuffer sb_uri = new StringBuffer();


    	String path = request.getContextPath();  //如:"","/pda","/cms",...
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"
    			+request.getServerPort()+path ; //+"/";

    	
        JspWriter out = pageContext.getOut();
        try {
            out.write( basePath );
        } catch (IOException e) {
        }
        
        //System.out.print("\nAbsolutePathTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }
    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
	    super.release();
	}

}
