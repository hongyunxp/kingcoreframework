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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * <p>自定义更好的实现方式，按照规则根据数据库列明美化找到Bean的属性名。</p>
 * @author Zeven on 2006-9-16
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class SmartResultSetBeanExtractor implements ResultSetBeanExtractor {


    protected static Logger log=Logger.getLogger( SmartResultSetBeanExtractor.class );

	public Object extractData(ResultSet rs, Class<?> beanClass, boolean isList)
		           throws SQLException{
		try {
			return bindDataToDto(rs, beanClass, isList);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	/*
	 * 从ResultSet绑定到JavaBean  
	 *   
	 * @param ResultSet  
	 * @param Class（JavaBean）  
	 * @return bean  
	 */   
	private Object bindDataToDto(ResultSet rs, Class<?> bean, boolean isList) throws SQLException, InstantiationException, IllegalAccessException {  // throws Exception  
 
	    //取得Method方法   
	    Method[] methods = bean.getClass().getMethods();  

	    //取得ResultSet的列名   
	    ResultSetMetaData rsmd = rs.getMetaData();   
	    int columnsCount = rsmd.getColumnCount();   
	    String[] columnNames = new String[columnsCount];   
	    for (int i = 0; i < columnsCount; i++) {   
	        columnNames[i] = formatColumnLabel(rsmd.getColumnLabel(i + 1));   
	    }  

	    Object obj = null;
	    List<Object> objList = new ArrayList<Object>();
	    //遍历ResultSet   
	    while (rs.next()) {   
	        //反射, 从ResultSet绑定到JavaBean   
	    	obj = bean.newInstance();
	        for (int i = 0; i < columnNames.length; i++) {   
	            //取得Set方法   
	            String setMethodName = "set" + columnNames[i];   
	            //遍历Method   
	            for (int j = 0; j < methods.length; j++) {   
	                if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
	                    setMethodName = methods[j].getName();   
	                    Object value = rs.getObject(columnNames[i]);  

	                    //实行Set方法   
	                    try {   
	                        //JavaBean内部属性和ResultSet中一致时候   
	                        Method setMethod = bean.getClass().getMethod(   
	                                setMethodName, value.getClass());   
	                        setMethod.invoke(obj, value);   
	                    } catch (Exception e) {   
	                        //JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。   
	                        Method setMethod;
							try {
								setMethod = bean.getClass().getMethod(   
								        setMethodName, String.class);
		                        setMethod.invoke(obj, value.toString());   
		                        
							} catch (Exception e1) {
								log.warn("设置属性失败：beanName="+bean.getName()+" 属性="+setMethodName,
												e1);
							}
	                    }   
	                }   
	            }   
	        }
	        if (isList) {
				objList.add(obj);
			} else {
				return obj ;
			}
	    }
	    return objList;   
	}

	/**
	 * 将数据库的列明格式为Bean的属性名，可以根据自己的规则over write本方法。
	 *    比如数据库列名 user_name -->属性名 userName
	 *                user_name -->属性名 user_name
	 * @param columnLabel
	 * @return
	 */
	public String formatColumnLabel(String columnLabel) {
		return columnLabel;
	}
	

}
