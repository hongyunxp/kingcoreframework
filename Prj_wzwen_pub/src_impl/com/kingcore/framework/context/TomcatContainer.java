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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import com.kingcore.framework.jdbc.ConnectionManager;

/**
 * <p>Tomcat 容器提供的特有的内容。包括DataSource 等。支持多数据源。
 * 		当前针对Tomcat5.5。 支持主流版本，也可以向下兼容其他版本。还可以自己编写并注入。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TomcatContainer implements ServletContainer,DataSourceManager {

	/**
	 * 日志对象
	 */
	protected static Logger log=Logger.getLogger(com.kingcore.framework.context.TomcatContainer.class);
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 保存DataSource的引用，支持多数据源，不用每次获取
	 */
	private Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

    /**
     * 获取默认的 DataSource.
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException{
    	return getDataSource(  getDefaultDataSourceName() );
    }

    /**
     * 获取名称为dataSourceName的 DataSource.
     * @param dataSourceName
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource(String dataSourceName) throws SQLException{

    	// 是否是已经获取的DataSource对象。
    	DataSource ods = dataSourceMap.get(dataSourceName);
		if(ods!=null){
			return ods;
		}

		DataSource ds = null;
//		1.从上下文根据JNDI获取DataSource
		Context initCtx = null;
		try {
			initCtx = new InitialContext(); //创建上下文实例
		
//			get DataSource by different Servlet Container Type.
			// Tomcat5 需要做特殊的处理
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			
			ds = (DataSource)envCtx.lookup( dataSourceName );
			dataSourceMap.put(dataSourceName, ds);    // 缓存起来
			return ds ;
			
		}catch (NamingException ex) {
			log.fatal("cant get the new InitialContext()") ;
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw new SQLException( ex.getMessage() );

		}catch (Exception ex) {
			log.fatal("cant get the new InitialContext()") ;
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw new SQLException( ex.getMessage() );
		}

    }

	/**
	 * 获取系统默认的数据源的数据库连接。
	 * @return
	 */
	public Connection getConnection() throws SQLException {
		return getConnection( getDefaultDataSourceName() );
	}

	/**
	 * 获取指定名称的数据源的数据库连接。
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String dataSourceName)
				throws SQLException {
		
		Connection conn = null ;
		
		//	1.从上下文根据JNDI获取DataSource
		DataSource ds = getDataSource( dataSourceName );
					
		if (ds != null){
			conn = ds.getConnection();
			if( conn==null ){
				log.debug("使用jndi方式获取名称为[" +dataSourceName+ "]的DataSource中的Connection为null。");
			}
			conn.setAutoCommit(false);		// Weblogic的DataSource的JNDI默认为 true.
			//log.debug("obtain a connection:   "+conn);
			return conn;
			
		}else{
			log.debug("get DateSource from JNDI failed!");
			log.debug("使用jndi方式获取名称为[" +dataSourceName+ "]的DataSource失败");
		}
			
		
		//	2.如果jndi中没有，从静态类中获取
		if(conn==null){
			try{
				conn = ConnectionManager.getInstance().getConnection() ;
			}catch(SQLException ex){
				log.debug("debug", ex);
				/// ex.pri ntStackTrace();
				throw new SQLException("DBUtils.getConnection():"+ex.getMessage());

			}
//			java.sql.Connection conn1 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn2 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn3 = ConnectionManager.getConnection() ;
//			java.sql.Connection conn4 = ConnectionManager.getConnection() ;
			
//			conn1.close() ;
//			conn2.close() ;
//			conn3.close() ;
//			conn4.close() ;
		}
		return conn ; 
		
	}

	/**
	 * Tomcat 容器下的 request.getContextPath() 返回的标准化处理。
	 */
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	/**
	 * Tomcat 容器的included 的子页面要指定 contentType。
	 */
	public void setIncludedPageContentType(HttpServletResponse response,String contentType) {
		response.setContentType( contentType );
	}
	
    /**
     * 缺省的DataSource 的名称。
     */
	protected String defaultDataSourceName = "jndi/jdbc";
	private String configPath;
 

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	public void setDefaultDataSourceName(String dataSourceName) {
		this.defaultDataSourceName = dataSourceName;
		
	}

	public String getServletPath(HttpServletRequest request) {
		return request.getServletPath();
	}
	
	/**
	 * Tomcat5.5要求设置每个页面的ContextType，不设置那么页面会报错。
	 */
	public void setPageContentTypeIndividually(HttpServletResponse response, String contentType) {
		response.setContentType( contentType );
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		
	}

}
