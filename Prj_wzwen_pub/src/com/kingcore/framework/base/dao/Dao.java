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

package com.kingcore.framework.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>基类：Dao Jdbc first interface.</p>
 * @author Zeven on 2008-6-4
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface Dao<T, PK extends java.io.Serializable> {
	 
		T insert(T model, Map<String,Object> param) throws Exception;

		T delete(PK modelPK, Map<String,Object> param) throws Exception;
		
		T update(T model, Map<String,Object> param) throws Exception;
		
		T selectOne(PK modelPK, Map<String,Object> param) throws Exception; //load
		
		List<T> selectList(Map<String,Object> param) throws Exception;   //findAll
	 
		//Pagination selectPage(T model, int pageNo, int pageSize, Map<String,Object> param) throws Exception;
		//int selectPagedCount(T model) throws Exception;

}
