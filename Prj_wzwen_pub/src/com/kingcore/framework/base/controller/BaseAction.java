/**
 * @(#)BaseAction..java		    2004/03/21
 *
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Zeven.woo
 */

package com.kingcore.framework.base.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import wzw.util.DbUtils;

import com.kingcore.framework.bean.NavigableDataSet;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.bean.impl.WebPageNavigator;
import com.kingcore.framework.exception.ApplicationException;
import com.kingcore.framework.exception.InvalidSessionException;
import com.kingcore.framework.exception.NoPrivilegeException;
import com.kingcore.framework.exception.SystemException;


/**
 * <pre>
 *   扩展Struts框架中的Action类，覆盖和增加一些方法，其他的Action类扩展此类，<br>
 *       并且至少覆盖此类的executeAction 方法。
 * 
 * 　　** 直接对外提供服务，不需要使用接口。
 * 		> 适合基于 Struts MVC 的开发;
 *		> 属于web的controller层，与ws是同一个层次的，可以直接调用Dao(2层)，也可以调用Service(3层，由Service调用Dao);
 *		> 类本身基本上是线程安全的;
 *		> 更多方法支持查看：	wzw.util.HttpUtils
 *							javax.servlet.http.HttpUtils
 *
 * 
 * </pre>
 * @author	WUZEWEN on 2004.03.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class BaseAction extends Action
{

    public static final String CHANGE_PAGE = "changepage";
    /**
     * 错误信息保存的集合对象
     */
    public final ActionErrors errors = new ActionErrors();
    
    /**
     * log4j日志对象。  建议修 protected 改为 private. 
     */
    protected static Logger log = Logger.getLogger(com.kingcore.framework.base.controller.BaseAction.class);

	/**
     * <p>对Action 以及其子类中错误信息的处理方法。</p>
     * @author WUZEWEN on 2004-3-25
     * @param property 错误属性类别
     * @param error 错误信息对象
     * @throws
     */
    protected void addErrors(String property, ActionError error)
    {
        errors.add(property, error);
    }

    protected void addErrors(ActionError error)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, error);
    }

    protected void addErrors(String key)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, new ActionError(key));
    }

    protected void clearErrors(){
    	errors.clear();
    }

    protected void saveError(HttpServletRequest request)
    {
        this.saveErrors(request, errors);
    }

	/**
     * <pre>this method is to override the same function in struts1.2 frame</pre>
     * @author WUZEWEN on 2004-3-25
     * @param ActionMapping mapping
     * @param ActionForm form
     * @return ActionForward forward
     * @throws Exception
     */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
					throws IOException, ServletException, Exception {
    	clearErrors();

    	try{
    		return executeAction(mapping, form, request, response);

    	} catch (ApplicationException ea) {
    		if(ea.getShowType()==0){
    			request.setAttribute("key.global.exception", ea);
    			return (mapping.findForward("application-exception"));
    		}else{
    			request.setAttribute("key.global.exception", ea);
    			return (mapping.findForward("application-exception-dialog"));
    		}
    		
    	} catch (NoPrivilegeException es) {
			request.setAttribute("key.global.exception", es);
			return (mapping.findForward("application-noPrivilegeException"));

    	}  catch (SystemException es) {
			request.setAttribute("key.global.exception", es);
			return (mapping.findForward("system-exception"));

    	} catch (InvalidSessionException e){

    		if( e.getShowType()==2 ) {	// 不需要自动跳转
    			return mapping.findForward( e.getForwardName() ); //"user-login"
    		}
    		
    		String previousURL=request.getRequestURI()+"?";// +request.getQueryString();
    		
    		Enumeration er = request.getParameterNames();
    		String name=null;
    		while(er.hasMoreElements()){
    			name=er.nextElement().toString() ;
    			previousURL += name + "="+ java.net.URLEncoder.encode(request.getParameter(name), "utf-8") + "&";	//"%26";--针对跟在url后面的情况编码
    		}
    		
    		log.debug( "previousURL is "+ previousURL);
    		request.setAttribute("previousURL", wzw.lang.Base64.encode(previousURL.getBytes("utf-8") ) );
    		return (mapping.findForward(e.getForwardName()));  // "user-login"
    		
    	} catch (Exception e) {
			e.getStackTrace();		// 输出一次。
			throw e;
    	}
	}

	/**
     * <pre>to compatible to old struts frame!
     *		具体实现在execute方法中，详细信息见execute方法。</pre>
     * @author WUZEWEN on 2004-3-25
     */
    public ActionForward perform(ActionMapping mapping,
						         ActionForm form,
						         HttpServletRequest request,
						         HttpServletResponse response)
    						throws IOException, ServletException, Exception
    {
    	return execute(mapping,form,request,response);
    }

	/**
	 * <pre>本方法是被Action的execute or perform方法调用，在Action的子类中
	 *			需要 override this method to do business logic!</pre>
	 * @author WUZEWEN on 2004-3-25
	 * @param mapping 包含request目标的映射
	 * @param form 页面数据bean
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 如果成功，返回一个ActionForward对象；如果不需要页面跳转则返回null
	 * @throws Exception 操作失败时
	 */
    public abstract ActionForward executeAction(
						        ActionMapping mapping,
						        ActionForm form,
						        HttpServletRequest request,
						        HttpServletResponse response)
        					throws Exception ;

	/**
     * <p>以下是封装的对数据库操作的方法：</p>
     * @deprecated 创建一个 DAO 对象，而不在该类中直接操作数据库
     * 
     * @author WUZEWEN on 2004-3-26
     * @return 一个数据库连接对象。
     * @throws 获取连接失败时，抛出SQLException。
    protected Connection getConnection() throws SQLException
    {
    	return wzw.util.DBUtils.getConnection();
    }
     */


	/**
     * <p>以下是封装的对数据库操作的方法：</p>
     * @deprecated 创建一个 DAO 对象，而不在该类中直接操作数据库
     * 
     * @author WUZEWEN on 2004-3-26
     * @param request 请求对象
     * @return 一个数据库连接对象。
     * @throws 获取连接失败时，抛出SQLException。
    protected Connection getConnection(HttpServletRequest request)
        										throws SQLException
    {
    	//wzw on 2005-11-17 不在application中获取DataSource,
    	//而是由系统的DBUtils类统一管理连接，包括上面的以及JNDI，静态类等

    	//wzw on 2006-09-06 采用jndi获取数据库连接
    	return wzw.util.DBUtils.getConnection(request);

    }
     */

    /**
     * @deprecated 页面流转控制层取消本方法
     * @param request
     * @return
    protected String getDatabaseType(HttpServletRequest request){
   		ServletContext context = request.getSession().getServletContext();
   		return (String)context.getAttribute(Constants.DATABASE_TYPE);
   	}
     */


	/**
     * <p>对request,session域中的对象的获取,如果是多个则返回第一个值。</p>
     * @author WUZEWEN on 2004-3-26
     * @param request 请求对象
     * @param parameterName 对象名称
     * @param defaultValue 对象缺省值
     * @return 对象的值。
     */
   	protected String getParameter(
        					HttpServletRequest request,
        					String parameterName,
        					String defaultValue)
    {
        return wzw.util.HttpUtils.getParameter(request, parameterName, defaultValue);
    }
   	
    /**
     * <p>获取Session 中的对象</p>
     * @param request HttpServletRequest
     * @param objectName 对象名称
     * @return Object
     */
    protected Object getObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
    	return wzw.util.HttpUtils.getObjectInSession( request, objectName) ;
    }
    /**
     * <p>移除Session中的对象</p>
     * @param request HttpServletRequest
     * @param objectName 对象名称
     */
    protected void removeObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
    	wzw.util.HttpUtils.removeObjectInSession( request, objectName ) ;
    }

    /**
     * <p>放置对象到Session中去。</p>
     * @param request HttpServletRequest
     * @param objectName 对象名称
     * @param obj 对象的引用
     */
    protected void setObjectInSession(
        HttpServletRequest request,
        String objectName,
        Object obj)
    {
    	wzw.util.HttpUtils.setObjectInSession(request, objectName, obj ) ;
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     * @deprecated 数据库查询，不建议在控制层处理数据库操作，建议在Dao层处理
     * @param sql The query to execute.
     * @return The object returned by the Service.
     * @throws SQLException
    protected RowSet doQuery(String sql)
        throws SQLException {
		return DBUtils.doQuery(sql);
    }
     */

    /**
     * 检查会话时候有效。
     * @param request 请求对象
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request) throws Exception{
    	checkSessionValid( request, "userid");
    }
    
    /**
     * 检查会话时候有效。
     * @param request 请求对象
     * @param userObejctName 标识会话有效的对象的名称，根据此对象确定会话是否有效
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
				String userObejctName) throws Exception{
    	checkSessionValid( request, userObejctName,"user-login");
    }

    /**
     * 检查会话时候有效，如果无效，则跳转到相应的页面。
     * @param request 请求对象
     * @param userObejctName 标识会话有效的对象的名称，根据此对象确定会话是否有效
     * @param forwardName 会话无效跳转到的页面，在struts的config中配置的全局forward值
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
    							String userObejctName,
    							String forwardName ) throws Exception{
    	checkSessionValid(request,
							userObejctName,
							forwardName , 0);
    }

    /**
     * 对于需要判断用户正确登录，而且会话没有结束的地方，调用本方法。
     * @param request HttpServletRequest
     * @param userObejctName 检查对象的名称，默认为 userid
     * @param forwardName 登录页面对应配置的forwardName,默认为user-login
     * @param needBack 登录之后是否需要跳回到当前页面(0为No，默认值; 1为Yes)
     * @throws Exception
     */
    protected void checkSessionValid(HttpServletRequest request,
    							String userObejctName,
    							String forwardName, 
    							int needBack ) throws Exception{
    	//System.out.println("-----------go0 " + request.getQueryString() );
    	if(request.getSession().getAttribute(userObejctName)==null){
    		// 对用户没有登陆，做特殊处理
				//throw new InvalidSessionException("没有登陆或者会话结束！" ,0, forwardName);
    		needBack = (needBack==1)?0:2;
    		throw new InvalidSessionException("没有登陆或者会话结束！" , needBack , forwardName);
    		 
    	} 
    }

	
    /**
     * <pre>
     *   修改为private方法 by wzw，简化开发。
     *   获得要查询页面的翻页信息。 注意：这里有三个系统保留参数rowCount,pageSize,pageNumber。
     * 	 这个方法主要是在控制层获取翻页信息，然后传递->Server->Dao层，由里面处理。
     * 	int[0]：总行数 	rowCount
     * 	int[1]：每页行数	pageSize
     * 	int[2]：总页数	pageNumber
     * </pre>
     * @param request
     * @return
     */
    private int[] getPageParameter(HttpServletRequest request) {

//		String action = request.getParameter("Action");
//		log.debug(" param action is " + action);
		
		// page control parameters.
		int[] pageParams = new int[3];
		String str_rowCount = request.getParameter("rowCount");
		String str_pageSize = request.getParameter("pageSize");
		String str_pageNumber = request.getParameter("pageNumber");
		
		//get rowCount value.
		int rowCount=-1;
		if(str_rowCount!=null && !str_rowCount.trim().equals("") ){
			try{
				rowCount = Integer.parseInt(str_rowCount);
			}catch(Exception te){
				log.warn("warning!! parameter rowCount is " + str_rowCount);
				log.debug("warn", te);
				//te.pri ntStackTrace();   Zeven on 2008-05-21 后台错误信息屏蔽掉，警告而不是致命错误。
				//throw te;
			}
		}
		
		//get pageSize value.
		int pageSize=30;
		if(str_pageSize!=null && !str_pageSize.trim().equals("") ){ 
			try{
				pageSize = Integer.parseInt(str_pageSize);
			}catch(Exception te){
				log.fatal("warning! pageSize is " + str_pageSize);
				log.debug("fatal", te);
				//te.pri ntStackTrace();	//   Zeven on 2008-05-21 后台错误信息屏蔽掉，警告而不是致命错误。
				//throw te;
			} 
		}
		
		//get pageNumber value.
		int pageNumber=1;
		if(str_pageNumber!=null && !str_pageNumber.trim().equals("") ){ 
			try{
				pageNumber = Integer.parseInt(str_pageNumber);
			}catch(Exception te){
				log.fatal("warning! pageNumber is " + str_pageNumber);
				log.debug("fatal", te);
				//te.pri ntStackTrace();	//   Zeven on 2008-05-21 后台错误信息屏蔽掉，警告而不是致命错误。
				
				pageNumber=1;
				//// this.setPageNumber(1);
			} 
		}
		
		pageParams[0] = rowCount;
		pageParams[1] = pageSize;
		pageParams[2] = pageNumber;
		
		return pageParams;
    }


    /**
     * ---------------------------checkPrivilege----------------- 检查权限，以后放到每个项目的xxxBaseAction中处理。
	 * 需要在子类中具体实现权限的检查。
     * 检查用户是否具有权限。
     * 		首先检查会话是否过期，如果过期，抛出会话过期异常，自动跳转到登陆页面。
     * 		如果会话正常，检查是否有操作该功能的权限，如果没有，调到页面显示“对不起，您没有操作该功能的权限！”。
     * @param request HttpServletRequest
     * @param empObejctName 用户名称，默认为 "empid".
     * @param priviCode 权限编号
     * @param forwardName 登录页面对应配置的forwardName,默认为user-login
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,
    								String empObejctName, 
    								String priviCode,
    								String forwardName ) throws Exception{
    	//先检查是否登陆。对于检查权限而需要检查的会话检查，登录之后不需要跳转
    	//checkSessionValid(request,empObejctName);
    	if(request.getSession().getAttribute(empObejctName)==null){
    		// 对用户没有登陆，做特殊处理
    		//if( userObejctName.equals("userid") ){
    			throw new InvalidSessionException("没有登陆或者会话结束！", 2 , forwardName);
    		//}
    	}
    	
    	boolean hasPrivi = hasPrivilege( request, empObejctName ,priviCode);
    	// 检查权限处理
    	
    	if(!hasPrivi){
    		throw new NoPrivilegeException("对不起，您没有操作该功能的权限！");
    	}
    }
    
    /**
	 * 需要在子类中具体实现权限的检查。
     * @param request
     * @param empObejctName
     * @param priviCode
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,
			String empObejctName, 
			String priviCode) throws Exception{
    	checkPrivilege( request, empObejctName, priviCode, "user-login"); 
    }
    /**
	 * 需要在子类中具体实现权限的检查。
     * @param request
     * @param priviCode
     * @throws Exception
     */
    protected void checkPrivilege(HttpServletRequest request,String priviCode) throws Exception{
    	checkPrivilege( request, "empid" ,priviCode);
    }
    
    
    /**
	 * 需要在子类中具体实现权限的检查。
     * 检查用户是否具有当前功能操作的权限。
     * 		调用方式：在BaseAction的子类中，直接调用，如： hasPrivilege(request, "empid", "102001");
     * @param request
     * @param empObejctName 系统员工对象的名称，根据该名称在session中获取值，如"empid"。
     * @param privilageId 系统权限统一的编号。
     * @return return true if the employee has privilage, else return false.
     * @throws Exception
     */
    protected boolean hasPrivilege(HttpServletRequest request,
    		String empObejctName, 
    		String privilageId ) throws Exception{

//  	检查权限处理, 员工、角色、权限。
    	boolean hasPrivi = false;
    	String empid = request.getSession().getAttribute(empObejctName).toString();
    	int count = DbUtils.getSize("Tper_Orgemp T1, Tper_Orgroleemp T2, Tper_Roleprivi T3",
    						"Where T1.Empid = T2.Empid And T2.Roleid = T3.Roleid And T3.Privilegeid = "+privilageId+" And t3.delflag=0 And t1.empid="+empid);
    	
    	if(count>0){
    		hasPrivi = true;
    	}
    	return hasPrivi;
    }
    
    /**
	 * 需要在子类中具体实现权限的检查。
     * 检查是否有权限。
     * @param request
     * @param privilageId
     * @return
     * @throws Exception
     */
    protected boolean hasPrivilege(HttpServletRequest request,String privilageId) throws Exception{
    	return hasPrivilege( request, "empid" ,privilageId);
    }
    
	
	/**
	 * 需要在子类中具体实现权限的检查。
	 * ajax请求获得员工是否有权限,ajax调用专用方法。
	 * @param request
	 * @param response
	 * @param empObejctName
	 * @param priviCode
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean checkPrivilege(
									HttpServletRequest request,
									HttpServletResponse response,
									String empObejctName, 
									String priviCode) throws Exception{
		
		
		String msg ="您没有操作该功能的权限!";
		// 检查权限处理
		boolean hasPrivi = true ;
		try{
			// 如果会话有效则检查权限，否则不需要检查权限
			if(request.getSession().getAttribute(empObejctName)==null){
	    		hasPrivi = false;
				msg="您还没有登录或者登录间隔时间太长，请重新登录!";
	    	}else {
				hasPrivi = hasPrivilege( request, empObejctName ,priviCode);
	    	}
			
		}catch(Exception e2){
			hasPrivi = false;			
			msg="产生其他异常，["+e2.getMessage()+"]请重新登陆或者联系管理员!";
		}

		if( hasPrivi==false ){

			response.setContentType("text/xml; charset=UTF-8");
			PrintWriter printWriter=response.getWriter();
			printWriter.println("<response>");
			printWriter.println("<content>false</content>");
			printWriter.println("<content>"+msg+"</content>");
			printWriter.println("</response>");
			printWriter.close();
		}

		return hasPrivi;
	}

	/**
	 * <pre>
	 * 需要在子类中具体实现权限的检查。建议在具体业务的Action基类去覆盖。
	 * </pre>
	 * @param request
	 * @param response
	 * @param priviCode
	 * @return
	 * @throws Exception
	 */
	protected boolean checkPrivilege(HttpServletRequest request,HttpServletResponse response,String priviCode) throws Exception {
		return checkPrivilege( request,response, "empid" ,priviCode);
	}
	
//	/**
//	 * 返回Web页面使用的可翻页导航信息。
//	 * @deprecated 建议使用 getNavigableDataSet(int[] pageParams, List dataList) or getNavigableDataSet(int[] pageParams, RowSet rowSet)。
//	 * @param pageParams
//	 * @param datas
//	 * @return
//	 */
//	protected NavigableDataSet getNavigableDataSet(int[] pageParams, Object datas){
//		if( datas instanceof RowSet){
//			return new QueryDataSet(pageParams , (RowSet)datas );
//		
//		}else if( datas instanceof List){
//			log.fatal("not complete!!!!");
//			return new QueryDataSet(pageParams , (List)datas ); //(List)datas
//		}
//		
//		return null;
//	}
	
	/**
	 * <pre>
	 * 返回Web页面使用的带有可翻页带导航信息和数据集的对象。
	 * 	你也可以使用　getNavigator　方法，单独获取导航信息对象．
	 * 
	 * 使用介绍--------------------------：
	 * 	 后台控制层代码：
	 * 		...
	 * 		Navigator navigator = this.createNavigator(...);
	 * 		Object stockOrders = this.getNavigableDataSet(navigator, dataList);
	 * 		...
	 * 		request.setAttribute("stockOrders", stockOrders);
	 * 		...
	 * 
	 *   页面View层代码：
	 * 		...
	 * 		<td><woo:navigator name="stockOrders" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 * </pre>
	 * @param pageParams 翻页控制信息，可使用 getPageParameter(HttpServletRequest) 方法获取，并且可被修改
	 * @param dataList 数据集对象
	 * @return
	 */
	protected NavigableDataSet getNavigableDataSet(HttpServletRequest request, String path, List dataList){
		Navigator navigator = this.createNavigator(request, path);
		return new QueryDataSet(navigator , path, dataList ); //(List)datas
	}

	/**
	 * 
	 * <pre>
	 * 返回Web页面使用的带有可翻页带导航信息的和数据集的对象。
	 * 	你也可以使用　getNavigator　方法，单独获取导航信息对象．
	 * 
	 * 使用介绍--------------------------：
	 * 	 后台控制层代码：
	 * 		...
	 * 		Navigator navigator = this.createNavigator(...);
	 * 		Object stockOrders = this.getNavigableDataSet(navigator, crs);
	 * 		...
	 * 		request.setAttribute("stockOrders", stockOrders);
	 * 		...
	 * 
	 *   页面View层代码：
	 * 		...
	 * 		<td><woo:navigator name="stockOrders" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 *  </pre>
	 * @param pageParams 翻页控制信息，可使用 getPageParameter(HttpServletRequest) 方法获取，并且可被修改
	 * @param dataList 数据集对象
	 * @return
	 */
	protected NavigableDataSet getNavigableDataSet(HttpServletRequest request, String path, RowSet rowSet){
		Navigator navigator = this.createNavigator(request, path);
		return new QueryDataSet(navigator , path, rowSet ); //(List)datas
	}
	

	/**
	 * <pre>
	 * 返回Web页面导航翻页对象。 注意：这里有三个系统保留参数rowCount,pageSize,pageNumber。
	 *    这里是放回单独的导航对象,与数据对象分离。
	 *         而getNavigableDataSet方法则是整合导航对象和数据对象为一个对象返回.
	 * 		可以根据情况选用 getNavigator 与 getNavigableDataSet 方法。
	 * 	   如果有自己的导航器，可以写自己的导航器(继承WebPageNavigator)并覆盖本方法。
	 * 使用介绍--------------------------：
	 * 	 后台控制层代码：
	 * 		...
	 * 		Navigable nvg = this.createNavigator(pageParams);
	 * 		...
	 * 		...
	 * 		request.setAttribute("stockOrdersNvg", nvg);
	 * 		...
	 * 
	 *   页面View层代码：
	 * 		...
	 * 		<td><woo:navigator name="stockOrdersNvg" path="${path}" type="pnfl" scope="request"/></td>
	 * 		...
	 *  </pre>
	 * @param path 请求的url路径
	 * @vision 2.0
	 * @return
	 */
	protected Navigator createNavigator(HttpServletRequest request, String path ){
		int[] pageParams = this.getPageParameter(request); //wzw：放入内部
		return new WebPageNavigator( pageParams, path ); //(List)datas
	}
    
}
