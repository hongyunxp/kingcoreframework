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

package com.kingcore.framework.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.kingcore.framework.jdbc.DataSourceProxy;
import com.kingcore.framework.jdbc.PlainConnection;


/**
 * <p>对于多个数据库联接DataSource的管理对象，管理所有的DataSource，并且有默认的DataSource名字。
 * 			采用注入方式(结合使用PlainDataSourceProvider 与其他独立的DataSource bean. 这样的好处是不用配置服务器的jndi)：
 * 
 *    提供DataSource的对象需要实现的接口。
 *    特点：> *提供多数据源(DataSouce)；
 *    	   > *提供默认的DataSource，不需要每个Dao对象都注入/设置DataSource，设置DataSourceProvider即可；
 *    	   > 多数据源可以是不同类型的，
 *    		比如多数据源有的采用服务器的jndi提供、有的采用Spring管理的bean提供；或者都采用ConnectionManager提供。</p>
 *    
 * 		<!-- Construct DataSourceProvider -->
 * 		<bean id="dataSourceProvider" class="com.kingcore.framework.context.PlainDataSourceProvider">
 * 			<property name="defaultDataSourceName">
 * 				<value>jndi/jdbc</value>
 * 			</property>
 * 			<property name="dataSources">
 * 				<ref local="dataSourceMap" />
 * 			</property>
 * 		</bean>
 * 
 * 		<!-- Construct Map -->
 *		<bean id="dataSourceMap" class="java.util.HashMap">
 * 			<constructor-arg>
 *				<map>
 *					<entry key="jndi/jdbc" value-ref="dataSource-jndi/jdbc" />
 *					<entry key="jdbc2" value-ref="dataSource-jdbc2" />
 * 				</map>
 * 			</constructor-arg>
 *		</bean>
 *
 * 		<!-- Construct DataSource 1 -->
 *		<bean id="dataSource-jndi/jdbc" class="org.apache.commons.dbcp.BasicDataSource">  
 *			<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>   
 *			<property name="url" value="jdbc:mysql://127.0.0.1:3306/wzwdb"></property>   
 *			<property name="username" value="root"></property>   
 *			<property name="password" value="root"></property> 
 *		</bean> 
 * 		<!-- Construct DataSource 2 -->
 *		<bean id="dataSource-jdbc2" class="org.apache.commons.dbcp.BasicDataSource">  
 *			<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>   
 *			<property name="url" value="jdbc:mysql://127.0.0.1:3306/wzwdb2"></property>   
 *			<property name="username" value="root"></property>   
 *			<property name="password" value="root"></property> 
 *		</bean> 
 * 
 * 	总结三种配置方式：
 * 		1。直接使用web容器的jndi方式，在spring中只需要配置一个 dataSourceProvider 即可，其它每个dataSource对象在 server.xml, web.xml 中配置，
 * 						--- 主要用于使用jndi获取dataSource的情况；
 * 		2。使用 DataSourceManager 方式，在spring中只需要配置一个 dataSourceProvider 即可，其它每个dataSource对象在 db.property 中配置，
 * 						--- 主要用于使用 com.kingcore.framework.jdbc.DataSourceManager 来调试，并且配置简单；
 * 		3。使用 PlainDataSourceProvider 方式，在spring中只需要配置一个 dataSourceProvider、一个dataSourceMap，
 * 						--- 其它具体每个dataSource也是在spring中配置，用于整合各种现有的dataSource bean对象 
 * 							//Zeven:似乎有点多此一举，为什么不直接使用spring的getBean？为什么多一层bean之上的dataSource bean概念？有意义么；
 * 							//强烈建议使用 PlainDataSourceProvider 方式配置。
 * 
 * </p>
 * 
 * @author Zeven on 2007-6-9
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class PlainDataSourceManager implements DataSourceManager {

	private String defaultDataSourceName = null;
	private Map<String, DataSourceProxy> dataSources= null;
	private String configPath = null;

	
	/* (non-Javadoc)
	 *   这里实际返回的是一个带有dataSource引用的Connection
	 *   每个DataSource对应一个DatabaseManager。
	 * @see com.kingcore.framework.context.DataSourceProvider#getConnection(java.lang.String)
	 */
	public Connection getConnection(String dataSourceName) throws SQLException {
		DataSourceProxy plainDataSource = this.getPlainDataSource(dataSourceName);
		return new PlainConnection(plainDataSource.getDataSource().getConnection(),
				plainDataSource.getDatabaseManager());
		//return this.getDataSource(dataSourceName).getConnection();
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource(java.lang.String)
	 */
	protected DataSourceProxy getPlainDataSource(String dataSourceName) throws SQLException {
		return this.getDataSources().get(dataSourceName);  //(DataSource)
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return this.getConnection(this.defaultDataSourceName);
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource()
	 */
	protected DataSource getDataSource() throws SQLException {
		return this.getDataSource( this.defaultDataSourceName );
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDataSource(java.lang.String)
	 */
	protected DataSource getDataSource(String dataSourceName) throws SQLException {
		return this.getDataSources().get(dataSourceName).getDataSource();  //(DataSource)
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#getDefaultDataSourceName()
	 */
	public String getDefaultDataSourceName() {
		return this.defaultDataSourceName;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DataSourceProvider#setDefaultDataSourceName(java.lang.String)
	 */
	public void setDefaultDataSourceName(String dataSourceName) {
		this.defaultDataSourceName = dataSourceName;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public Map<String, DataSourceProxy> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Map<String, DataSourceProxy> dataSources) {
		this.dataSources = dataSources;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
		
	}

}
