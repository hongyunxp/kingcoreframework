/**
 * Copyright (C) 2002-2009 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.base.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import wzw.util.DbUtils;

import com.kingcore.framework.exception.InvalidSessionException;
import com.kingcore.framework.exception.NoPrivilegeException;

/**
 * <p>dwr 的前段处理类的基类，符合POJO。
 * 		基类中提供便利的方法，比如会话检查，权限检查等。。
 * 		属于web的controller层，放在controller目录减少package的深度。</p>
 * 
 * @author Zeven on Dec 2, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class BaseDwr {

	private final static Logger log = Logger.getLogger(BaseDwr.class);

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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
