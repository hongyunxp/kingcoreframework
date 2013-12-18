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

package com.kingcore.framework.base.dao.impl;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.kingcore.framework.base.dao.DaoOrm;


/**
 * <p>java data access object  iBATIS implements.
 * 		使用 apache iBATIS 来实现 dao 层。
 * </p>
 * @author Zeven on 2008-11-13
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class DaoOrmIbatisImpl extends SqlMapClientDaoSupport 
		implements DaoOrm, Serializable {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
