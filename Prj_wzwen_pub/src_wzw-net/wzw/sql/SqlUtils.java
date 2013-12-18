/**
 * Copyright (C) 2002-2005 ChangSha WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.sql;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import wzw.beans.Value;
import wzw.beans.value.BigDecimalValue;
import wzw.beans.value.BooleanValue;
import wzw.beans.value.ByteValue;
import wzw.beans.value.BytesValue;
import wzw.beans.value.DateValue;
import wzw.beans.value.DoubleValue;
import wzw.beans.value.FloatValue;
import wzw.beans.value.IntValue;
import wzw.beans.value.LongValue;
import wzw.beans.value.ObjectValue;
import wzw.beans.value.ShortValue;
import wzw.beans.value.StringValue;
import wzw.beans.value.TimeValue;
import wzw.beans.value.TimestampValue;


/**
 * <BLOCKQUOTE>SQL相关的工具类。</BLOCKQUOTE>
 * @author	zewen.wu on 2004.03.18
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 */

public class SqlUtils {

    /**
     * log是一个定义日志的变量
     */
    public static final Logger log = Logger.getLogger( SqlUtils.class);

    /**
     * <p>Fill PreparedStatement with Object[]</p>
     * @param stmt PreparedStatement
     * @param params Object[]
     * @throws SQLException
     */
    
    public static void setStatementArg(PreparedStatement ps,
    		int paramIndex, int sqlType, Object inValue) throws SQLException {	//, String typeName
    
    	if(log.isDebugEnabled()){
		    	log.debug("paramIndex="+paramIndex 
		    				+ ",sqlType="+sqlType 
		    				+ ",inValue="+inValue );
    	}
    	if (inValue==null) {
        	ps.setNull(paramIndex, sqlType);
        	
		} else if (sqlType == SqlTypeValue.TYPE_UNKNOWN) {	// 不带有argType 的情况，需要判断参数类型
			if (inValue instanceof String || inValue instanceof StringBuffer || inValue instanceof StringWriter) {
				ps.setString(paramIndex, inValue.toString());
			}
			else if (inValue instanceof Integer) {
				ps.setInt(paramIndex, (Integer) inValue);
			}
			else if (inValue instanceof Long) {
				ps.setLong(paramIndex, (Long) inValue);
			}
			else if (inValue instanceof Float) {
				ps.setFloat(paramIndex, (Float) inValue);
			}
			else if (inValue instanceof Double) {
				ps.setDouble(paramIndex, (Double) inValue);
			}
			else if (inValue instanceof BigDecimal) {
				ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
			}
			else if (inValue instanceof java.util.Date && !(inValue instanceof java.sql.Date ||
					inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp)) {
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
			}
			else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()));
			}
			else {
		    	if(log.isInfoEnabled()){
			    	log.debug("no matched Types found," 
			    				+ ",SqlTypeValue=UNKNOWN"
			    				+ ",inValue="+inValue );
		    	}
				// Fall back to generic setObject call without SQL type specified.
				ps.setObject(paramIndex, inValue);
			}
			
		} else if (sqlType == Types.VARCHAR) {
			ps.setString(paramIndex, inValue.toString());
			
		} else if (sqlType == Types.INTEGER) {
			ps.setInt(paramIndex, Integer.parseInt(inValue.toString()));
			
		} else if (sqlType == Types.BIGINT) {
			ps.setLong(paramIndex, Long.parseLong(inValue.toString()));
			
		} else if (sqlType == Types.FLOAT) {
			ps.setFloat(paramIndex, Float.parseFloat(inValue.toString()));
			
		}else if (sqlType == Types.DOUBLE) {
			ps.setDouble(paramIndex, Double.parseDouble(inValue.toString()));
			
		} else if (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) {
			if (inValue instanceof BigDecimal) {
				ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
			}
			else {
				ps.setObject(paramIndex, inValue, sqlType);
			}
			
		} else if (sqlType == Types.DATE) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Date) {
					ps.setDate(paramIndex, (java.sql.Date) inValue);
				}
				else {
					ps.setDate(paramIndex, new java.sql.Date(((java.util.Date) inValue).getTime()));
				}
			}
			else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
			}
			else {
				ps.setObject(paramIndex, inValue, Types.DATE);
			}
			
		} else if (sqlType == Types.TIME) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Time) {
					ps.setTime(paramIndex, (java.sql.Time) inValue);
				}
				else {
					ps.setTime(paramIndex, new java.sql.Time(((java.util.Date) inValue).getTime()));
				}
			} else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()), cal);
			} else {
				ps.setObject(paramIndex, inValue, Types.TIME);
			}
			
		} else if (sqlType == Types.TIMESTAMP) {
			if (inValue instanceof java.util.Date) {
				if (inValue instanceof java.sql.Timestamp) {
					ps.setTimestamp(paramIndex, (java.sql.Timestamp) inValue);
				}
				else {
					ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
				}
			}
			else if (inValue instanceof Calendar) {
				Calendar cal = (Calendar) inValue;
				ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
			}
			else {
				ps.setObject(paramIndex, inValue, Types.TIMESTAMP);
			}
			
		} else {
	    	if(log.isInfoEnabled()){
		    	log.debug("no matched Types found," 
		    				+ ",sqlType="+sqlType 
		    				+ ",inValue="+inValue );
	    	}
			// Fall back to generic setObject call with SQL type specified.
			ps.setObject(paramIndex, inValue, sqlType);
		}
	}

    
    /**
     * 
     * @param pstmt
     * @param params
     * @throws SQLException
     */
    public static void setStatementArg(PreparedStatement pstmt, 
    		Object[] params )throws SQLException {

    	for (int i = 0; i < params.length; i++) {
    		setStatementArg(pstmt, i+1, SqlTypeValue.TYPE_UNKNOWN, params[i]);
		}
	
	}

    public static void main(String[] args) {
    	Object[] ids=new Object[]{"abc"};
    	System.out.println(ids==null);
    	System.out.println(ids.length);
    }
    
    /**
     * 
     * @param pstmt
     * @param params
     * @param argTypes
     * @throws SQLException
     */
    public static void setStatementArg(PreparedStatement pstmt, 
    		Object[] params, int[] argTypes)throws SQLException {

    	for (int i = 0; i < params.length; i++) {
    		setStatementArg(pstmt, i+1, argTypes[i], params[i]);
		}
	
	}
    
    /**
     * @deprecated by setStatementArg method
     * @param stmt
     * @param params
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

	            }catch(Exception e) //UnsupportedConversionException
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
    public static void fillStatement(PreparedStatement stmt, List<?> list)
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
    public static void fillStatement(PreparedStatement stmt, Stack<?> stack)
        throws SQLException  {

        if (stack == null) {
            return;
        }

        Object obj[] = stack.toArray() ;
        fillStatement(stmt, obj) ;

	}
}
