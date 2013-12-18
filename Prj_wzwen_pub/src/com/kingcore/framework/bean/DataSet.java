/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.bean;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * <p>数据集合，是RowSet接口的简化接口。方法完全按照ResutSet设计，方便使用。</p>
 * @author Zeven on 2006-6-29
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface DataSet {
	
	public boolean absolute( int row) throws SQLException;
	
	public void beforeFirst() throws SQLException;
	
	public void afterLast() throws SQLException;
	
	public boolean first() throws SQLException;
	
	public boolean last() throws SQLException;
	
	public boolean previous() throws SQLException;
	
	public boolean next() throws SQLException;

	/**
	 * 获得当前行号
	 * @return int
	 */
	public int getRow()  throws SQLException;
	/**
	 *	返回总的行数
	 */
	public int size();
	
	
	/**
	 *	BigDecimal 的geter 方法。
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException;
	
	public BigDecimal getBigDecimal( String colName ) throws SQLException;
	
	/**
	 *	Blob 的geter 方法。
	 */
	public Blob getBlob( int colNum) throws SQLException;
	
	public Blob getBlob( String colName ) throws SQLException;
	
	/**
	 *	Boolean 的geter 方法。
	 */
	public boolean getBoolean( int colNum) throws SQLException;
	
	public boolean getBoolean( String colName) throws SQLException;
	
	
	/**
	 *	Byte 的geter 方法。
	 */
	public byte getByte( int colNum ) throws SQLException;
	public byte getByte ( String colName ) throws SQLException;
	
	
	/**
	 *	Byte[] 的geter 方法。
	 */
	public byte[] getBytes( int colNum ) throws SQLException;
	public byte[] getBytes( String colName )  throws SQLException;
	
	/**
	 *	CharacterStream 的geter 方法。
	 
	 public CharacterStream getCharacterStream( int colNum)
	 {
	 return ( crs.getCharacterStream( colNum) ) ;
	 }
	 
	 public CharacterStream getCharacterStream( String colName)
	 {
	 return ( crs.getCharacterStream( colName) ) ;
	 }
	 */
	
	
	/**
	 *	Clob 的geter 方法。
	 */
	public Clob getClob( int colNum) throws SQLException;
	public Clob getClob ( String colName)  throws SQLException;
	
	/**
	 *	Date 的geter 方法。
	 */
	public Date getDate( int colNum) throws SQLException;
	
	public Date getDate( String colName) throws SQLException;
	
	/**
	 *	Double 的geter 方法。
	 */
	public double getDouble( int colNum ) throws SQLException;
	public double getDouble( String colName ) throws SQLException;
	
	/**
	 *	Double 的geter 方法。
	 */
	public float getFloat( int colNum) throws SQLException;
	public float getFloat( String colName) throws SQLException;
	
	/**
	 *	Integer 的geter 方法。
	 */
	public int getInt( int colNum) throws SQLException;
	public int getInt( String colName) throws SQLException;
	
	/**
	 *	Long 的geter 方法。
	 */
	public long getLong( int colNum ) throws SQLException;
	public long getLong( String colName ) throws SQLException;
	
	/**
	 *	Object 的geter 方法。
	 */
	public Object getObject( int colNum) throws SQLException;
	public Object getObject( String colName)  throws SQLException;
	
	/**
	 *	抽象的 的geter 方法。
	 */
//	public Object get( int colNum) throws SQLException;
//	public Object get( String colName)  throws SQLException;
	
	
	/**
	 *	Short 的geter 方法。
	 */
	public short getShort( int colNum ) throws SQLException;
	public short getShort( String colName )  throws SQLException;
	
	/**
	 *	String 的geter 方法。
	 */
	public String getString( int colNum) throws SQLException;
	public String getString( String colName ) throws SQLException;
	
	/**
	 *	Time 的geter 方法。
	 */
	public Time getTime( int colNum ) throws SQLException;
	public Time getTime( String colName ) throws SQLException;
	
	/**
	 *	Timestamp 的geter 方法。
	 */
	public Timestamp getTimestamp( int colNum ) throws SQLException;	
	public Timestamp getTimestamp( String colName ) throws SQLException;
	
	//说明：其他的get方法需要再添加。
	
	// ---------------------------------------------------- 2007-07-25
	

}

