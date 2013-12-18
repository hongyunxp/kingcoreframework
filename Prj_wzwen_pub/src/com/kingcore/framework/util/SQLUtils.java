/**
 * Copyright (C) 2002-2005 ChangSha WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.kingcore.framework.bean.Value;
import com.kingcore.framework.bean.value.BigDecimalValue;
import com.kingcore.framework.bean.value.BooleanValue;
import com.kingcore.framework.bean.value.ByteValue;
import com.kingcore.framework.bean.value.BytesValue;
import com.kingcore.framework.bean.value.DateValue;
import com.kingcore.framework.bean.value.DoubleValue;
import com.kingcore.framework.bean.value.FloatValue;
import com.kingcore.framework.bean.value.IntValue;
import com.kingcore.framework.bean.value.LongValue;
import com.kingcore.framework.bean.value.ObjectValue;
import com.kingcore.framework.bean.value.ShortValue;
import com.kingcore.framework.bean.value.StringValue;
import com.kingcore.framework.bean.value.TimeValue;
import com.kingcore.framework.bean.value.TimestampValue;
import com.kingcore.framework.exception.UnsupportedConversionException;


/**
 * <BLOCKQUOTE>SQL相关的工具类。</BLOCKQUOTE>
 * @author	zewen.wu on 2004.03.18
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 */

public class SQLUtils {

    /**
     * log是一个定义日志的变量
     */
    public static final Logger log = Logger.getLogger(com.kingcore.framework.util.SQLUtils.class);

    /**
     * <p>Fill PreparedStatement with Object[]</p>
     * @param stmt PreparedStatement
     * @param params Object[]
     * @throws SQLException
     */
    public static void fillStatement(PreparedStatement stmt, Object[] params)
        throws SQLException  {

        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {

            	try{

	            	//1.
	            	if (params[i] instanceof StringValue)
	            	{
	            		//log.fatal("aaaaa");
	            		stmt.setString(i + 1, ((Value)params[i]).getString());
	            		//log.fatal("bbbb");
	            	}
	            	//2.
	            	else if (params[i] instanceof BigDecimalValue)
	            	{
	            		stmt.setBigDecimal(i + 1, ((Value)params[i]).getBigDecimal());
	            	}
	            	//3.
	            	if (params[i] instanceof BooleanValue)
	            	{
	            		stmt.setBoolean(i + 1, ((Value)params[i]).getBoolean());
	            	}
	            	//4.
	            	if (params[i] instanceof BytesValue)
	            	{
	            		stmt.setBytes(i + 1, ((Value)params[i]).getBytes());
	            	}
	            	//5.
	            	if (params[i] instanceof ByteValue)
	            	{
	            		stmt.setByte(i + 1, ((Value)params[i]).getByte());
	            	}
	            	//6.
	            	if (params[i] instanceof DateValue)
	            	{
	            		stmt.setDate(i + 1, ((Value)params[i]).getDate());
	            	}
	            	//7.
	            	if (params[i] instanceof DoubleValue)
	            	{
	            		stmt.setDouble(i + 1, ((Value)params[i]).getDouble());
	            	}
	            	//8.
	            	if (params[i] instanceof FloatValue)
	            	{
	            		stmt.setFloat(i + 1, ((Value)params[i]).getFloat());
	            	}
	            	//9.
	            	if (params[i] instanceof IntValue)
	            	{
	            		stmt.setInt(i + 1, ((Value)params[i]).getInt());
	            	}
	            	//10.
	            	if (params[i] instanceof LongValue)
	            	{
	            		stmt.setLong(i + 1, ((Value)params[i]).getLong());
	            	}
	            	//11.
	            	if (params[i] instanceof ObjectValue)
	            	{
	            		stmt.setObject(i + 1, ((Value)params[i]).getObject());
	            	}
	            	//12.
	            	if (params[i] instanceof ShortValue)
	            	{
	            		stmt.setShort(i + 1, ((Value)params[i]).getShort());
	            	}
	            	//13.
	            	if (params[i] instanceof TimestampValue)
	            	{
	            		stmt.setTimestamp(i + 1, ((Value)params[i]).getTimestamp());
	            	}
	            	//14.
	            	if (params[i] instanceof TimeValue)
	            	{
	            		stmt.setTime(i + 1, ((Value)params[i]).getTime());
	            	}

	            }catch(UnsupportedConversionException e)
				{
					log.fatal("\n类型转换失败!!") ;
				}
			} else {
				log.fatal("\n参数为null !!") ;
            	stmt.setNull(i + 1, Types.OTHER);
			}
        }
    }

    /**
     * Fill PreparedStatement with List
     * @param stmt PreparedStatement
     * @param list List
     * @throws SQLException
     */
    public static void fillStatement(PreparedStatement stmt, List list)
        throws SQLException  {

        if (list == null) {
            return;
        }
        Object obj[] = list.toArray() ;
        fillStatement(stmt, obj) ;

	}

    /**
     * Fill PreparedStatement with Stack
     * @param stmt PreparedStatement
     * @param stack Stack
     * @throws SQLException
     */
    public static void fillStatement(PreparedStatement stmt, Stack stack)
        throws SQLException  {

        if (stack == null) {
            return;
        }

        Object obj[] = stack.toArray() ;
        fillStatement(stmt, obj) ;

	}
}
