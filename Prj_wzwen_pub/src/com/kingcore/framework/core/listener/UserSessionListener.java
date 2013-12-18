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

package com.kingcore.framework.core.listener;


import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import wzw.util.DbUtils;



/**
 * <p>一个会话开始和结束时的时间监听器！</p>
 * @author	WUZEWEN on 2005-11-16
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */


public class UserSessionListener implements HttpSessionListener {
	
	/**
	 * Method sessionCreated
	 *
	 *
	 * @param event
	 *
	 */
	public void sessionCreated(HttpSessionEvent event) {
		//System.out.println ("session begin!");	
		java.text.SimpleDateFormat sdf =new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime=sdf.format(new java.util.Date());

		System.out.println (" session sessionCreated -------aa----- "+ event.getSession().getCreationTime() + " at " + strTime);
	}
	
	/**
	 * Method sessionDestroyed
	 *
	 *
	 * @param event
	 *
	 */
	public void sessionDestroyed(HttpSessionEvent event){
		//System.out.println ("session end!");
		
		Connection conn=null;
		Statement stmt=null;
		/// String sql;
		java.text.SimpleDateFormat sdf =new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strTime=sdf.format(new java.util.Date());
		try{

			System.out.println (" session timeout ------aa------ " + event.getSession().getCreationTime()  + " at " + strTime);
			
		}catch(Exception e){
			System.out.println (""+e.getMessage());
		}finally{
			DbUtils.closeQuietly(conn, stmt, null);
		}
		
	}	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
}
