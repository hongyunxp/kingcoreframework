/**
 * Copyright (C) 2011 Kingcore Science & Technology CO,.LTD. All rights reserved.
 * XNS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
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

/**
 * <p>java类文件的说明...</p>
 * @author Zeven/wzw on Mar 3, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @since	JDK5
 */

public interface DataSourceManager {


    /**
     * 缺省的DataSource 的名称。
     * @ depr ecated 现在采用默认值和注入设置方式，不再使用 static 值。
     */
    ///public static String DEFAULT_DATASOURCE_NAME="jndi/jdbc";

    /**
     * <p>获取默认的 DataSource.</p>
     * @return
     * @throws SQLException
     */
    //public DataSource getDataSource() throws SQLException;

    /**
     *   屏蔽是为了避免前端直接使用  dataSource.getConnnection。。。
     * 获取名称为dataSourceName的 DataSource.
     * @param dataSourceName
     * @return
     * @throws SQLException
     */
    //public DataSource getDataSource(String dataSourceName) throws SQLException;
    
	/**
	 * 获取指定名称的数据源的数据库连接。
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String dataSourceName) throws SQLException;

	/**
	 * 获取系统默认的数据源的数据库连接。
	 * @return
	 */
	public Connection getConnection() throws SQLException;

	/**
	 * 
	 * @param dataSourceName
	 */
	public void setDefaultDataSourceName(String dataSourceName);
	
	/**
	 * 获取defaultDataSourceName.
	 * @return
	 */
	public String getDefaultDataSourceName();

	
	public void setConfigPath(String configPath);
	

//	public Connection getConnection(String name, long time) throws SQLException;
}
