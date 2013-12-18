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

package com.kingcore.framework.base.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.kingcore.framework.base.service.Service;
import com.kingcore.framework.context.ApplicationContext;

/**
 * <p>java类文件的说明...</p>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class ServiceImpl implements Service{


    /**
     * log4j日志对象。
     */
    protected static Logger log=Logger.getLogger( ServiceImpl.class );

    /**
     * 单件的设计模式替换全静态方法，便于spring 构造
     */
	private static ServiceImpl instance;
	

    /**
     * <p>获取单件对象，参考这个方法必须在子类中去定义相应的
     * 		方法getInstance() 以实现单件设计模式。
     * </p>
     * @return 
     */
	public static ServiceImpl getInstanceExample(){
		return instance;
	}

	
	/**
	 * <p>获取数据库的连结，以便根据业务逻辑控制数据库连接事务。</p>
	 * 
	 * @param poolName 连接池对象的名称。
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException{
		
		return ApplicationContext.getInstance().getDataSourceManager().getConnection( );
		
	}
	
	/**
	 * <p>获取数据库的连结，以便根据业务逻辑控制数据库连接事务。</p>
	 * 
	 * 
	 * @param poolName 连接池对象的名称。
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(String poolName) throws SQLException{
		
		return ApplicationContext.getInstance().getDataSourceManager().getConnection(poolName);
		
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
