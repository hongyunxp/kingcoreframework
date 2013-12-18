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

package com.kingcore.framework.core.service;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>基类：项目中的 HandlerLocator 对象，调用spring bean对象的入口切入管理类。</p>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ServiceLocator {
	
	private static ApplicationContext applicationContext = null;

//	private static ApplicationContext applicationContext =
//		new ClassPathXmlApplicationContext(
//			new String[] { "classpath*:/conf/spring-config.xml",
//					   "classpath*:/conf/zeven-framework-*.xml" });

	public ServiceLocator() {
	}

	public static synchronized Object getBean(String beanName) {
		return (Object) applicationContext.getBean(beanName);
	}

	public static synchronized Object getService(String beanName) {
		return (Object) applicationContext.getBean(beanName);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据 servletContext 来初始 applicationContext，而不用直接设置application。
	 *    是监听器放置到 servletContext 中去的，原理是先启动监听器，再在Servlet初始中运行本方法。
	 *    
	 *    ?? 使用Liston/Servlet载入Spring放于Context的参数是：WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
	 *    ?? 使用ContextLoaderPlugIn载入Spring放于Context的参数是：ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX+ModuleConfig.getPrefix()（具体请查看源代码）
	 * @param servletContext
	 */
	public static void initApplicationContext(ServletContext servletContext) {
		//WebApplicationContext applContext = DelegatingActionUtils.findRequiredWebApplicationContext( servletContext,  null );	// 这段代码也可以
		//ApplicationContext applContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 这段代码也可以
		WebApplicationContext applContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);	// 这段代码也可以
		applicationContext = applContext;
	}
	
}
