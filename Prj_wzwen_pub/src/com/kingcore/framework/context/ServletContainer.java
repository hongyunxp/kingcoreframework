/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.context;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Servlet 容器对象统一接口。</p>
 * @author Zeven on 2007-9-3
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface ServletContainer {
	
	/**
	 * 根据不同的Servlet容器，返回 request.getContextPath() 统一标准的值。
	 * Zeven，实际上这个与配置应用程序有关系，比如Tomcat下面，配置 path="" 和 path="/" 
	 * 		都认为是根目录，但是一个返回空，一个返回“/”。
	 *    WebLogic下面的配置：
	 *      ...
	 *      <context-root>/</context-root>
	 *    </weblogic-web-app>
	 * 	
	 * 	建议配置为“” 为最好。
	 *      当前servlet容器版本下基本上是与容器无关，但是与配置有关。
	 *      总结：对于通常path属性的值，一般返回的都不以“/”结尾。
	 *     
	 * @deprecated 当前servlet容器版本下基本上是与容器无关，但是与配置有关。
	 * @param request
	 * @return
	 */
	public String getContextPath(HttpServletRequest request) ;
	

	public String getServletPath(HttpServletRequest request) ;
	
	
	/**
	 * 根据不同的Servlet容器，在被include的子页面输出 contentType的值。
	 * 		如：text/html;charset=utf-8
	 * 		   text/xml;charset=utf-8
	 *	       text/html;charset=gb2312
	 * @param request
	 * @return
	 */
	public void setPageContentTypeIndividually(HttpServletResponse response,String contentType) ;
	
	/**
	 * @deprecated replaced by setPageContentTypeIndividually.
	 * @param response
	 * @param contentType
	 */
	public void setIncludedPageContentType(HttpServletResponse response,String contentType) ;
	
}
