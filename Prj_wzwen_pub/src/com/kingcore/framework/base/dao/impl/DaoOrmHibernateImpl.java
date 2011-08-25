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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.kingcore.framework.base.dao.DaoOrm;

/**
 * <pre>
 * 	基类：Dao Hibernate Base Object.
 *	public class DaoHibernateImpl<T, PK extends Serializable> extends HibernateDaoSupport {
 *		> 基于 Spring HibernateDaoSupport，需要配置;
 *		> 可以使用Spring控制事物或者Hibernate控制事物;
 * 		> 适合的开发结构为三层： Struts + Spring + HibernateDao;
 *      
 * </pre>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class DaoOrmHibernateImpl extends HibernateDaoSupport 
		implements DaoOrm, Serializable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
