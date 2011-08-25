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

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.util.RequestUtils;

import com.kingcore.framework.bean.IConnection;


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


public class SetConnBeanTag extends BodyTagSupport
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
    	System.out.print("\nSetConnBeanTag--->doStartTag") ;
        IConnection Login = null ;
        try{
        	Login = (IConnection)RequestUtils.applicationInstance("ConnectionPool") ;
        }catch( ClassNotFoundException e )
        {
        	System.out.print("\n没有找到类"+ this.clazz) ;
        }catch( IllegalAccessException e )
        {
        	System.out.print("\nIllegalAccessException"+ e.getMessage() +this.clazz) ;
        }catch( InstantiationException e )
        {
        	System.out.print("\nInstantiationException"+ e.getMessage() +this.clazz) ;
        }
    	System.out.print("\nSetConnBeanTag--->doStartTag22") ;

        Login.getConnection((HttpServletRequest)pageContext.getRequest()) ;



        //set the instance in scope
        if(scope.equals("page"))
        	this.i_scope = pageContext.PAGE_SCOPE ;
        else if(scope.equals("request"))
        	this.i_scope = pageContext.REQUEST_SCOPE ;
        else if(scope.equals("session"))
        	this.i_scope = pageContext.SESSION_SCOPE  ;
        else if(scope.equals("application"))
        	this.i_scope = pageContext.APPLICATION_SCOPE ;
        pageContext.setAttribute( "Login", (Connection)Login ,pageContext.PAGE_SCOPE) ;

        JspWriter jw= pageContext.getOut() ;

        //conn = getTransactionConnection();
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Commits the transaction, resets the auto commit flag and closes
     * the Connection (so it's returned to the pool).
     */
    public int doEndTag() throws JspException {


		System.out.print("\nSetConnBeanTag-->doEndTag()") ;

        IConnection Login = null ;
        pageContext.getOut() ;
		Login = (IConnection)pageContext.getAttribute(this.id, this.i_scope) ;
        Login.freeConnection() ;
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
