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

import wzw.sql.ResultSetConverter;

/**
 * <p>这是一个 ListResultSetExtractor 对象。 包括List<Map> 和 List<Bean>。</p>
 * @author Zeven on 2008-8-20
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MapResultSetExtractor implements ResultSetExtractor {

	private boolean isList = false;		// 是返回Map,还是返回List<Map>
	
	public MapResultSetExtractor(boolean isList) {
		this.isList = isList;
	}

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		if (this.isList) {
			return ResultSetConverter.toMapList(rs);		// List<Map>
			
		} else {
			return ResultSetConverter.toMap(rs );	// Map

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
