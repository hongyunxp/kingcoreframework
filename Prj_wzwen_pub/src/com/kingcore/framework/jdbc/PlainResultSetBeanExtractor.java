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
 * <p>将ResuletSet转为相应的java bean对象，针对一行N列，内部会运行rs.next()方法。
 *    实现ResultSetExtractor是为了DaoJdbcSpringImpl的需要。
 * </p>
 * @author Zeven on 2008-11-6
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class PlainResultSetBeanExtractor implements ResultSetBeanExtractor, ResultSetExtractor {


	public Object extractData(ResultSet rs, Class<?> beanClass, boolean isList)
		           throws SQLException{
		if (isList) {
			return ResultSetConverter.toBeanList(rs, beanClass );	// List<Bean>
			
		} else {
			return ResultSetConverter.toBean(rs, beanClass );	// Bean

		}
	}
	
	private Class<?> beanObject = null;		// Bean的类类型
	private boolean isList = false;		// 是返回Bean,还是返回List<Bean>

	public PlainResultSetBeanExtractor() {
	}
	
	public PlainResultSetBeanExtractor(Class<?> beanObject, boolean isList) {
		this.setBeanObject(beanObject);
		this.isList = isList;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		if (this.isList) {
			return ResultSetConverter.toBeanList(rs, this.getBeanObject() );	// List<Bean>
			
		} else {
			return ResultSetConverter.toBean(rs, this.getBeanObject() );	// Bean

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Class getBeanObject() {
		return beanObject;
	}

	public void setBeanObject(Class beanObject) {
		this.beanObject = beanObject;
	}

	public String formatColumnLabel(String columnLabel) {
		return columnLabel;
	}

}
