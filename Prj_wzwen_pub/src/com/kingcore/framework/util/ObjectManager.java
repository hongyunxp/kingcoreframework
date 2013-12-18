/*
 * @(#)QueryAction.java		    2004/04/14
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

package com.kingcore.framework.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @version		1.00 2004.04.14
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
public class ObjectManager {
	
	/**
	 *	将Session域中的对象移除
	 */	 
	public static void reMoveObjectInSession( HttpServletRequest request, String name )
	{
		HttpSession session = request.getSession(true) ;
		Object obj = session.getAttribute( name ) ;
		if (obj!=null)
			session.removeAttribute( name ) ;		
	} 
	
	
	/**
	 *	将Application域中的对象移除
	 */
	public static void reMoveObjectInApplication( HttpServletRequest request, String name )
	{
		HttpSession session = request.getSession(true) ;
		ServletContext context = session.getServletContext() ;
		Object obj = context.getAttribute( name ) ;
		if (obj!=null)
			session.removeAttribute( name ) ;		
	} 
	
	
	
	/**
	 *	放置对象在Session域中
	 */
	public static void setObjectInSession( HttpServletRequest request ,String name ,Object obj)
	{
		HttpSession session = request.getSession(true) ;
		Object o = session.getAttribute( name ) ;
		if (o!=null)
			session.removeAttribute( name ) ;
			
		session.setAttribute( name, obj ) ;		
	} 
	
	
	/**
	 *	放置对象在Application 域中
	 */
	public static void setObjectInApplication( HttpServletRequest request, String name ,Object obj )
	{
		HttpSession session = request.getSession(true) ;
		ServletContext context = session.getServletContext() ;
		Object o = context.getAttribute( name ) ;
		if (o!=null)
			context.removeAttribute( name ) ;
			
		context.setAttribute( name, obj ) ;		
	} 
	/**
	 *	将Session域中的对象移除
	 */	 
	public static Object getObjectInSession( HttpServletRequest request, String name )
	{
		HttpSession session = request.getSession(true) ;
		return session.getAttribute( name ) ;		 	
	} 
	
	
	/**
	 *	将Application域中的对象移除
	 */
	public static Object getObjectInApplication( HttpServletRequest request, String name )
	{
		HttpSession session = request.getSession(true) ;
		ServletContext context = session.getServletContext() ;
		return context.getAttribute( name ) ;
			
	} 
	
	
}
