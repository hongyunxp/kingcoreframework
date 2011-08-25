/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

/**
 * <p> 数据库管理系统提供者特殊的操作需要实现的接口。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface DatabaseManager {
	

	/**
	 * 根据当前的数据库的特殊规则对需要插入的内容采用正则表达式做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * 		-- 针对 MySQL数据库，"'","\"两个都是转义字符，都要变为"\'","\\"
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''cd")			="ab''''cd"
	 * </pre>
	 * @param src 需要保存到数据库的一个字段。
	 * @return
	 */
	public String escape2Sql(String val);
	
	/**
	 * 根据当前的数据库语法，转换可能为null的表达式(返回sql片段)。
	 * 
	 * <pre>
	 * 	
	 * 		Oracle 数据库返回 nvl(exp1, exp2);
	 * 		MySQL  数据库返回 coalesce( exp1, exp2);
	 * 		SQLServer  数据库返回 isNull(exp1, exp2);
	 * </pre>
	 * @param str1 字符串或者列名
	 * @param str2 字符串或者列名
	 * @return
	 */
	public String switchNull( String exp1, String exp2 );
	
	/**
	 * 根据当前的数据库语法，返回连接两个字符串的sql片段。
	 * 
	 * <pre>
	 * 	
	 * 		Oracle 数据库返回 str1 +"||"+ str2;
	 * 			concat( userid, username) = userid||username;
	 * 			concat( '001',  'admin' ) = '001'||'admin';
	 * 		MySQL  数据库返回 concat(str1, str2);
	 * 			concat( userid, username) = concat(userid,username);
	 * 			concat( '001',  'admin' ) = concat('001' ,'admin');
	 * 		SQLServer  数据库返回 str1 +"+"+ str2;
	 * 			concat( userid, username) = userid + "+" + username);
	 * 			concat( '001',  'admin' ) = '001' + "+" + 'admin');
	 * </pre>
	 * @param str1 字符串或者列名
	 * @param str2 字符串或者列名
	 * @return
	 */
	public String concat(String str1, String str2) ;
	
	/**
	 * 获取唯一标识符的表达式(返回sql片段)。
	 * Zeven on 2009-01-17: 本sql片段最好是不支持事务的，
	 * 		比如使用Oracle的seq，MySql的MyISAM类型的表记录序列值。 --ALTER TABLE `tsys_sequence` ENGINE=MYISAM;
	 * 		如果位于事务中，则会发生并发下锁表问题。
	 * @param tblName
	 * @return
	 */
	public String identity(String tblName) ;
	
	/**
	 * 获取系统时间的函数表达式(返回sql片段)。
	 * @return
	 */	
	public String sysDatetime() ;
	
	/**
	 * 提供将日期时间类型的列转为字符类型返回的方法(返回sql片段)
	 * @return
	 */
	
	public String datetime2Char( String colName ) ;
	
	/**
	 * 提供讲字符类型的列转为日期时间类型返回的方法(返回sql片段)
	 * @return
	 */
	public String char2Datetime( String colValue) ;
	
	/**
	 * 提供将日期(不含时间)类型的列转为字符类型返回的方法(返回sql片段)
	 * @return
	 */
	public String date2Char( String colName ) ;
	
	/**
	 * 提供讲字符类型的列转为日期(不含时间)类型返回的方法(返回sql片段)
	 * @return
	 */
	public String char2Date( String colValue) ;

	/**
	 *  根据需要查询的某段数据行，包装当前的sql statement and return.
	 * @param sql 原sql statement
	 * @param offset 起始位置
	 * @param row_count 需要获取的行数
	 * @return 包装之后的 sql statement
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count);

	/**
	 * <p>将 jdbc 中的 ResultSet 对象转化为 RowSet 对象。</p>
	 * @param rs 需要转为RowSet的ResultSet对象
	 * @return RowSet对象
	 * @throws SQLException
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException  ;

	
	/**
	 * <p>每个一般会有一个主键PK，根据传入的名称(如该表的名称)返回当前可用的序列值。
	 * 		对于Oracle 可以使用建立序列对象，使用 seq_tblName.nextVal 获取。
	 * 		对于其他的类型的数据库，采用相应的适合的方法。</p>
	 * 
	 * @param tblName
	 * @return
	 * @throws SQLException
	 */
	public long getIdentityValue( String tblName, Connection conn ) throws SQLException  ;

	
	/** 
	 * 使用自动增长列插入之后，获取刚才插入的id值。
	 *  是针对 MSS, MySQL 的另外一种序列解决方法。
	 *
	 */
	public int getLastInsertIdentity( Connection conn ) throws SQLException  ;


	/**
	 * 将二进制大对象保存到数据库的表中。
	 *   BLOB(Binary   Large   Object)   
	 *     可用来存储无结构的二进制数据。（类似于row和long   row）
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txt","D:\\upload\\a.txt"
	 * @param conn
	 * @return
	 */	
	public  boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath,
								Connection conn ) throws Exception;
	

	/**
	 * 将二进制大对象保存到数据库的表中。
	 *   BLOB(Binary   Large   Object)   
	 *     可用来存储无结构的二进制数据。（类似于row和long   row）
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txt","D:\\upload\\a.txt"
	 * @return
	 */	
	public  boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception;
	

	/**
	 * 将字符型大对象保存到数据库的表中。
	 *   CLOB(Character   Large   Object)   
	 *     用于存储对应于数据库定义的字符集的字符数据。（类似于long类型）   
	 *      
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param Content 要放入数据库的内容
	 * @return
	 */	
	public boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception;

	/**
	 * 将字符型大对象保存到数据库的表中。
	 *   CLOB(Character   Large   Object)   
	 *     用于存储对应于数据库定义的字符集的字符数据。（类似于long类型）   
	 *      
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param Content 要放入数据库的内容
	 * @param conn 处理数据提交/回滚事务控制的 Connection对象；
	 * @return
	 */
	public boolean updateClobColumn(String tablename, 
				String picField, 
				String sqlWhere,
				String content,
				Connection conn) throws Exception;

	/**
	 * 获取字符型大对象的内容。
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public String getClobColumn(String sql) throws Exception;	   


}
