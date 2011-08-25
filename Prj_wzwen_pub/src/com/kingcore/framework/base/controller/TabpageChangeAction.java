/*
 * @(#)QueryAction.java		    2004/03/13
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

package com.kingcore.framework.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @version		1.01 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class TabpageChangeAction extends BaseAction
{
	
	static {
		System.out.print("this is execute only when first load") ;
	}
    /**
     * 定义一些常量
     */
    
    public ActionForward executeAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
    	//System.out.print("TabpageChangeAction")  ;
        doInitialize(request);
        doExecute(mapping, form, request, response);
        return doForword(mapping, form, request, response);
    }

    public void doInitialize(HttpServletRequest request) throws Exception
    {

    }

    public void doExecute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse respone)
        throws Exception
    {
        /*
        String tabpage = request.getParameter("tabpage");
        
        tabpage = request.getRequestURI() +"?tabpage="+ tabpage ; 
        System.out.print("TabpageChangeAction: "+tabpage ) ; 
        
        respone.sendRedirect(respone.encodeURL( tabpage ));
        */
        //System.out.print("TabpageChangeAction: doExecute ")  ;
        return ;
    }


    public ActionForward doForword(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {   
        String ation_name = "" ;
    	
    	ation_name = request.getParameter("requestpage") ;
    	ation_name = ation_name + request.getParameter("tabpage") ;
    	
    	//String uri = null ;
    	//int pos ;
        //pos = uri.lastIndexOf("/") ;
        //uri = uri.substring(pos, uri.length() ) ;
        //uri = uri +  request.getParameter("tabpage");  ;
        //                           nullnull?
        /*
        System.out.print( "getContentLength:"+request.getContentLength()+
                          "getPathInfo:"     +request.getPathInfo()+
        	              "getRealPath:"     +request.getRealPath("come.jsp")+
        	              "getRemoteAddr"    +request.getRemoteAddr()+
        	              "getRequestURI"    +request.getRequestURI()+
        				  "getServletPath"   +request.getServletPath() ) ;
        
        
        if( this.getObjectInSession(request, "dataSetDetailView") !=null){
			
				QueryDataSet dataSet = (QueryDataSet)this.getObjectInSession(request, "dataSetDetailView");
				//System.out.print("CRGRKH_KHBH="+(String)v.get(0,"CRGRKH_KHBH") ) ; 
				
        }
        */
        
        return (mapping.findForward( ation_name ));
    }
    /**
     */
     
    public String getTabpage(HttpServletRequest request)
    {
        String tabpage = "";
        tabpage = getParameter(request, "tabpage", "1");
        return tabpage;
    }
    
}
