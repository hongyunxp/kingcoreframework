/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.util;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 

/**
 * <p>java类文件的说明...</p>
 * @author Zeven on 2008-1-31
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class HttpUtils {


	/**
     * <p>对request,session域中的对象的获取,如果是多个则返回第一个值。</p>
     * @author WUZEWEN on 2004-3-26
     * @param request 请求对象
     * @param parameterName 对象名称
     * @param defaultValue 对象缺省值
     * @return 对象的值。
     */
   	public static String getParameter(
        					HttpServletRequest request,
        					String parameterName,
        					String defaultValue)
    {
        String[] parameterValues = null;
        String paramValue = null;
        if (request != null)
        {
            parameterValues = request.getParameterValues(parameterName);
            if (parameterValues != null)
                paramValue = parameterValues[0];
            if (paramValue == null)
                paramValue = defaultValue;
        }
        return paramValue;
    }
	/**
	 *	将Session域中的对象移除
	 */	 
	public static void removeObjectInSession( HttpServletRequest request, String name )
	{
		HttpSession session = request.getSession(true) ;
		Object obj = session.getAttribute( name ) ;
		if (obj!=null)
			session.removeAttribute( name ) ;		
	} 
	
	
	/**
	 *	将Application域中的对象移除
	 */
	public static void removeObjectInApplication( HttpServletRequest request, String name )
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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
