/*
 * @(#)SetConnBeanTag.java		    1.00 2004/05/11
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

package com.kingcore.framework.tag ;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * @version		1.00 2004.05.11
 * @author		zewen.woo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class SetPcbBeanTag extends BodyTagSupport
{

	public String id = null ;
	public String clazz = null ;
	public String scope = null ;
	private int i_scope  ;
	public String bb = null ;
	public String cla = null ;


    /**
     * Sets the Connection to be used by all nested DB actions.
     */

    public void setId(String id){
    	System.out.print("\nsetId") ;
    	this.id = id ;
    }
    //public String getId(){
    //	return this.id ;
    //}
    /**
     * Sets the Connection to be used by all nested DB actions.
     */

    public void setClass(String clazz){
    	System.out.print("\nsetClass") ;
    	this.clazz =  clazz ;
    	System.out.print("\nsetClassend") ;
    }
    public void setClazz(String cla){
    	System.out.print("\nsetClass") ;
    	this.cla =  cla ;
    	System.out.print("\nsetClassend") ;
    }

    public void setAa(String aa){
    	this.bb =  aa ;
    }
    //public String getClass(){
    //	return this.clazz ;
    //}

    /**
     * Sets the Connection to be used by all nested DB actions.
     */

    public void setScope(String scope){
    	this.scope = scope ;
    }
    public String getScope(){
    	return this.scope ;
    }



    /**
     * Sets the Connection to be used by all nested DB actions.
     */
    public int doStartTag() throws JspException {

        //get a instance
        StringBuffer sb = new StringBuffer() ;
        JspWriter jw= pageContext.getOut() ;

      	sb
      		.append("<%@ page import=\"pcb.myFunc_Pooled\"%>")
      		.append("<%\n")
      		.append("myFunc_Pooled Login = new pcb.myFunc_Pooled() ;\n")
      		.append("Login.getConnection(request); \n")
      		.append("%>\n") ;
      	try{
      		jw.write( sb.toString()) ;
      	}catch(Exception e)
      	{
      		System.out.print("\nexcetpion when jw.write");
      	}


        return EVAL_BODY_INCLUDE;
    }

    /**
     * Commits the transaction, resets the auto commit flag and closes
     * the Connection (so it's returned to the pool).
     */
    public int doEndTag() throws JspException {


		System.out.print("\nSetConnBeanTag-->doEndTag()") ;


        StringBuffer sb = new StringBuffer() ;
        JspWriter jw= pageContext.getOut() ;
        //pageContext.
      	sb
      		.append("<%\n")
      		.append("Login.closeConnection();\n")
      		.append("%>\n") ;
      	try{
      		jw.write( sb.toString()) ;
      	}catch(Exception e)
      	{
      		System.out.print("\nexcetpion when jw.write");
      	}


        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
		id = null ;
		clazz = null ;
		scope = null ;
		i_scope = -1;
    }

}
