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

package com.kingcore.framework.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * <p> 新起一个ResultSetExtractor，便于对查询结果到Bean/BeanList做特殊的处理。</p>
 * @author Zeven on Aug 5, 2011
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface ResultSetBeanExtractor {  //extends ResultSetExtractor

	Object extractData(ResultSet rs, Class<?> beanClass, boolean isList) throws SQLException; //extends ResultSetExtractor {

	public String generatePropertyName(String columnLabel);
}
