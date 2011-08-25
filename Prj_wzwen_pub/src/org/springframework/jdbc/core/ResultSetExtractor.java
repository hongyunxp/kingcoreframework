/**
 * Copyright (C) 2011 ChangSha XNS Science & Technology CO,.LTD. All rights reserved.
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

package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
 
/**
 * <p>java类文件的说明...</p>
 * @author Zeven/wzw on Mar 3, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor
 * @since	JDK5
 */

public interface  ResultSetExtractor { 
	 
	/** 
	 * Implementations must implement this method to process
	 * all rows in the ResultSet.
	 * @param rs ResultSet to extract data from. Implementations should
	 * not close this: it will be closed by the JdbcTemplate.
	 * @return an arbitrary result object, or <code>null</code> if none
	 * (the extractor will typically be stateful in the latter case).
	 * @throws SQLException if a SQLException is encountered getting column
	 * values or navigating (that is, there's no need to catch SQLException)
	 * @throws DataAccessException in case of custom exceptions
	 */
	Object extractData(ResultSet rs) throws SQLException; //, DataAccessException;

}

