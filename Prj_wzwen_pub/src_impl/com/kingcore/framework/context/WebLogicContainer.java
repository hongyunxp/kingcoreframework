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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import com.kingcore.framework.jdbc.ConnectionManager;

/**
 * <p>WebLogic 容器提供的特有的内容。包括DataSource 等。支持多数据源。
 * 		当前针对WebLogic8.1。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class WebLogicContainer implements ServletContainer,DataSourceManager {

	/**
	 * 日志记录对象。 
	 */
	protected static Logger log=Logger.getLogger(com.kingcore.framework.context.WebLogicContainer.class);
    
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

//		get DataSource by different Servlet Container Type.
		DataSource ds = null;
		
//		1.从上下文根据JNDI获取DataSource
		Context initCtx = null;
		try {
	    	log.debug( "begin new InitialContext.");
			initCtx = new InitialContext(); //创建上下文实例

			
			// Weblogic8
			//DataSource ds = (DataSource)(((Context)getRequest().getSession().getServletContext()).lookup( Globals.DATA_SOURCE_KEY ));
			ds = (DataSource)(initCtx.lookup( dataSourceName ));
			dataSourceMap.put(dataSourceName, ds);    // 缓存起来
			return ds ;
			
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
		return getConnection(  getDefaultDataSourceName() );
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
			conn.setAutoCommit(false);		// Weblogic的DataSource的JNDI默认为 true.
			//log.debug("obtain a connection:   "+conn);
			return conn;
			
		}else{
			log.debug("get DateSource from JNDI failed!");
			//return null;
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
	 * WebLogic 容器下的 request.getContextPath() 返回标准化处理。
	 */
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath()+"/";
	}

	/**
	 * WebLogic 容器的included 的子页面不能重复指定 contentType。所以不需要做任何处理。
	 */
	public void setIncludedPageContentType(HttpServletResponse response,String contentType) {
		
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
	 * WebLogic不用多次设置每个页面的ContextType，设置了反而会报错误信息，提示不能多次设置。
	 */
	public void setPageContentTypeIndividually(HttpServletResponse response, String contentType) {
		
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		
	}
	
}
