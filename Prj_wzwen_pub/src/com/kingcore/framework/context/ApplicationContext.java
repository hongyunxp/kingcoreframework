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

import org.apache.log4j.Logger;

import com.kingcore.framework.jdbc.ResultSetBeanExtractor;




/**
 * <p> 管理整个框架的运行环境设置，包括如下几点：
 * 		1。DataSource 的提供者；
 * 		2。数据库管理系统；
 *    采用抽象接口、注入方式，不依赖于平台。
 * 	</p>
 * 
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ApplicationContext {

    /**
     * log4j日志对象。
     */
    protected static Logger log=Logger.getLogger( com.kingcore.framework.context.ApplicationContext.class );

    /**
     * 单件的设计模式替换全静态方法，便于spring 构造
     */
	private static ApplicationContext instance;
	
	/**
	 * 默认为WebLogic 容器提供数据源，其他还有Tomcat容器、自己编写的ConnectionPool，采用注入方式。
	 */
	private DataSourceManager dataSourceManager = null;
	

	private String defaultDataSourceName = null;
	
	/**
	 * 存放配置文件的根目录。
	 */
	private String configPath = null;

	/**
	 * 默认使用的数据源名称。
	 */
	//private String defaultDataSourceName = null;

	/**
	 * @deprecated databaseManager不建议跟ApplicationContext关联，而是与DataSource关联。
	 * 默认为 Oracle10i 容器提供数据源，其他还有MySQL，采用注入方式。
	 */
	private DatabaseManager databaseManager = null;
	

	/**
	 * 默认为Tomcat 容器，其他还有WebLogic等容器。
	 */
	private ServletContainer servletContainer = new TomcatContainer();
	
	
	public static ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	/**
	 * 构造对象并且赋予默认值。 会被spring 调用，导致两次构造!!!!!!!!!!!!
	 *
	 */
	private ApplicationContext() {
		//注销这里是实现纯接口，不依赖于具体实现，故要求系统一定要初始这些属性
		//或者注入，或者编写代码调用setter。
		//this.databaseManager = new OracleDatabase();
		//WebLogicContainer webLogic = new WebLogicContainer();
		//this.dataSourceManager = webLogic;
		//this.servletContainer = webLogic;

		instance = this ;
	}
	
	/**
	 * getter for dataSourceProvider.
	 * @return
	 */
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	/**
	 * setter for dataSourceProvider.
	 * @return
	 */
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	/**
	 * 
	 *  保留这个是为了兼容部分代码，比如spring jdbc实现。
	 * @deprecated databaseManager不建议跟ApplicationContext关联，而是与DataSource关联。
	 * getter.
	 * @return
	 */
	public DatabaseManager getDatabaseManager() {
		return this.databaseManager;
	}
	
	/**
	 * setter. 
	 * @deprecated databaseManager不建议跟ApplicationContext关联，而是与DataSource关联。
	 * @param databaseManager
	 */
	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ServletContainer getServletContainer() {
		return servletContainer;
	}

	public void setServletContainer(ServletContainer servletContainer) {
		this.servletContainer = servletContainer;
	}

//	public String getDefaultDataSourceName() {
//		return defaultDataSourceName;
//	}
//
//	private void setDefaultDataSourceName(String defaultDataSourceName) {
//		this.defaultDataSourceName = defaultDataSourceName;
//	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		if (this.dataSourceManager!=null) {
			this.dataSourceManager.setConfigPath(configPath);
		}
	}
	
	public void initContext(DataSourceManager dataSourceManager,	
							String configPath,
							String defaultDataSourceName){
		//初始 DataSourceManager的配置。
		this.setDataSourceManager(dataSourceManager);
		this.setConfigPath(configPath);
		this.defaultDataSourceName = defaultDataSourceName;

		this.dataSourceManager.setConfigPath(configPath);
		this.dataSourceManager.setDefaultDataSourceName(defaultDataSourceName);
	}

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	private ResultSetBeanExtractor resultSetBeanExtractor = null;
	public ResultSetBeanExtractor getResultSetBeanExtractor() {
		return this.resultSetBeanExtractor;
	}

	public void setResultSetBeanExtractor(
			ResultSetBeanExtractor resultSetBeanExtractor) {
		this.resultSetBeanExtractor = resultSetBeanExtractor;
	} 

}
