/**
 * Copyright (C) 2008 ChangSha WangWin Science & Technology CO,.LTD. All rights reserved.
 * WangWin PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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

package com.kingcore.framework.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.kingcore.framework.context.DatabaseManager;

/**
 * <p>本类的作用是转换ResultSet对象，转换为RowSet，
 * 			有机结合Spring 的 JdbcTemplate 与 展示层可以使用的、高效的RowSet对象，
 * 			采用各个数据库自己的、最优的RowSet实现。</p>
 * @author Zeven on 2008-5-23
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class RowSetResultSetExtractor implements ResultSetExtractor {

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	public Object extractData(ResultSet rs, DatabaseManager database) throws SQLException,
			DataAccessException {
		return database.resultSet2RowSet(rs);
		//Zeven, 把ResultSet转为相应数据库的RowSet对象。 不同的数据库jdbc实现RowSet可能不同。
		//return ApplicationContext.getInstance().getDatabaseManager().resultSet2RowSet(rs);
	}

	/**
	 * <p>java方法的说明...</p>
	 * @author Zeven on 2008-5-23
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Object extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
