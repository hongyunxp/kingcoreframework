/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework;

/**
 * @author WUZEWEN
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public final class Constants
{

//	/**
//	 * servlet 容器类型，便于支持多种servlet容器；  被注入方式替代了
//	 */
//	public final static int Servlet_Container_Type_Tomcat5    = 1;
//	public final static int Servlet_Container_Type_Weblogic8  = 2;
//	public final static int Servlet_Container_Type_Websphere6 = 3;
//	// 设置默认Servlet容器类型
//	public static int Servlet_Container_Type = Constants.Servlet_Container_Type_Weblogic8;
//
//	/**
//	 * DBMS 类型，便于支持多种 DBMS。  被注入方式替代了
//	 */
//	public final static int DBMS_Type_Oracle10 = 1;
//	public final static int DBMS_Type_MSS2000  = 2;
//	public final static int DBMS_Type_MySQL5   = 3;
//	//设置默认数据库类型
//	public static int DBMS_Type = Constants.DBMS_Type_Oracle10;
	
	// 应用名称及版本
	public final static String applicationName = "wpub framework";
	public final static String version = "v2.0";
	
	/**
	 * 登录后存放用户登录姓名的bean的名称
	 */
	public final static String USERLOGINBEAN = "UserLoginBean";
	public final static String LOGIN_USERBEAN_KEY = "LoginUserBean";
	/**
	 * 
	 */
	public final static String USER_LOGIN_KEY = "UserLoginBean";
	/**
	 * 存储数据库类型的名称
	 */
	public final static String DATABASE_TYPE = "DataBaseType";
	/**
	 * 存储角色列表的名称
	 */
	public final static String ROLESLIST = "com.wuzewen.pub.RolesList";
	/**
	 * 角色存储的名称的前缀
	 */
	public final static String ROLEPREFIX = "com.wuzewen.pub.roles.";
}


