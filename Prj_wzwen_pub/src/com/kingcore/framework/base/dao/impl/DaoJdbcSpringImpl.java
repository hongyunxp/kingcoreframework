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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jca.cci.InvalidResultSetAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import wzw.util.DbUtils;

import com.kingcore.framework.base.dao.DaoJdbc;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.PlainResultSetBeanExtractor;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;

/**
 * <pre>
 * 	基类：Dao Jdbc Base Object.
 * 		> 基于 Spring JdbcDaoSupport，需要配置;  
 * 		> 可以使用Spring控制事物或者手动控制事物;
 * 	   	> 适合的开发结构为三层： Struts + Spring + JdbcDao;
 *		> 更多数据库操作方法支持： wzw.util.DbUtils.class
 * 	
 * 	DaoJdbcSpringImpl.java
 * </pre>
 * @author Zeven on 2008-5-31
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DaoJdbcSpringImpl  extends JdbcDaoSupport 
		implements DaoJdbc, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 日志对象
	 */
	protected static Logger log = Logger.getLogger( DaoJdbcSpringImpl.class );

	
	/**
	 * 
	 * <pre>获取系统序列错误。</pre>
	 * @author Zeven on 2008-6-4
	 * @param tblName
	 * @return
	 */
	protected long getIdentityValue(String tblName){
    	try {
			return ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( tblName.toUpperCase(), this.getConnection());
			
    	} catch (SQLException e) {
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("获取名称为["+tblName+"]的标的系统序列产生错误!", e);
		}
		
	}

	/**
	 * <pre>
	 * 		为了兼容程序，再改为 public 类型，最好是 protected 类型。
	 * 
	 * 将字符型大对象保存到数据库的表中。
	 *   CLOB(Character   Large   Object)   
	 *     用于存储对应于数据库定义的字符集的字符数据。（类似于long类型） 
	 * </pre>  
	 *      
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param Content 要放入数据库的内容
	 * @return
	 */	
	protected boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content){

		try {
			return ApplicationContext.getInstance().getDatabaseManager().updateClobColumn(  tablename, 
					  picField, 
					  sqlWhere,
					  content,
					  this.getConnection() );
			
		} catch (CannotGetJdbcConnectionException e) {
			throw e;
			
		} catch (Exception e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("更新表["+tablename+"]的Clob字段["+picField+"]产生错误!", e); 
		}
		
	} 

	/**
	 * 
	 * <pre>相当于以前的 doQuery 方法。
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @author Zeven on 2008-5-26
	 * @param sql
	 * @return
	 */
	protected RowSet queryForRowSet(String sql) {
		return (RowSet)this.getJdbcTemplate().query(sql,  new RowSetResultSetExtractor() );
	}

	
	/**
	 * <pre>
	 * 采用PrepareStatement方式执行指定的sql语句，将结果集包装为RowSet对象返回。
	 * 
	 * </pre>
	 * 
	 * @param sql 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForRowSet(String sql, Object[] args, int[] argTypes ) {
		//this.getJdbcTemplate().query(String sql, Object[] args, int[] argTypes, new RowSetResultSetExtractor()));
		return (RowSet)this.getJdbcTemplate().query( sql,  args, argTypes, new RowSetResultSetExtractor() );
	}

	
	/**
	 * 
	 * <pre>获取指定翻页的RowSet。
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @return
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) {

		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		
		return this.queryForRowSet( sql_datas );	// not PreparedStatement
		
	}

	
	/**
	 * 
	 * <pre>获取指定翻页的RowSet。</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @param args 参数数组
	 * @param argTypes 参数类型数组
	 * @return
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes) {

		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		
		return this.queryForRowSet( sql_datas , args, argTypes);	//PreparedStatement
		
	}


	/**
	 * <pre>
	 *  执行指定的sql语句，将结果集包装为List<Map>对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。 
	 * </pre>
	 *  
	 * @param sql 需要执行查询的sql语句
	 * @return
	 * @throws SQLException
	 */
	protected List queryForList(String sql ) {
		return this.getJdbcTemplate().queryForList(sql);
	}

	
	/**
	 * <pre>
	 * 采用PrepareStatement方式执行指定的sql语句，将结果集包装为List<Map>对象返回。
	 * </pre>
	 * 
	 * @param sql 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @return
	 * @throws SQLException
	 */
	protected List queryForList(String sql, Object[] args, int[] argTypes) {
		return this.getJdbcTemplate().queryForList(sql, args, argTypes);
	}
	
	
	/**
	 * <pre>获取指定翻页的List<ModelObject>。
	 * 	    使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @param clazz List里面的对象，对应一行数据，可以为null
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Class clazz) {

		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.queryForList(sql_datas, clazz);	// return List<Bean>
		
	}

	
	/**
	 * <pre>获取指定翻页的List<ModelObject>。 是PreparedStatement方式。</pre>
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @param args 参数数组 
	 * @param argTypes 参数类型数组
	 * @param clazz List中每行对象的模型对象javaBean
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes, Class clazz) {

		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.queryForList(sql_datas, args, argTypes, clazz);	// return List<Bean>
		
	}
	
	/**
	 * <pre>
	 * 执行指定的sql语句，并返回List作为结果，每一行映射一个beanClass对象。 
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。
	 * </pre>
	 * 
	 * @param sql_datas 需要执行的sql statement
	 * @param beanClass map one row to beanClass
	 * @return
	 */
	protected List<?> queryForList(String sql_datas, Class beanClass) {
		return (List<?>)this.getJdbcTemplate().query(sql_datas,  new PlainResultSetBeanExtractor(beanClass,true) );
	}
 
	 
	/**
	 * <pre>
	 * 执行指定的sql语句，并返回List作为结果，每一行对应一个beanClass对象。
	 * </pre>
	 * 
	 * @param sql_datas 需要执行的sql statement
	 * @param args 参数数组
	 * @param argTypes 参数类型数组
	 * @param beanClass map one row to beanClass
	 * @return
	 */
	protected List queryForList(String sql_datas, Object[] args, int[] argTypes, Class beanClass) {
		return (List)this.getJdbcTemplate().query(sql_datas, args, argTypes, new PlainResultSetBeanExtractor(beanClass,true) );
	}

	
	/**
	 * <pre>获取指定翻页的List，每行使用一个Map封装数据。
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas) {
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.getJdbcTemplate().queryForList(sql_datas);		// return List<Map>
		
	}

	/**
	 * <pre>获取指定翻页的List，每行使用一个Map封装数据，即List<Map>。
	 * 		PreparedStatement方式。</pre>
	 * @param navigator 翻页信息数组[Paginated]
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @param args 参数数组
	 * @param argTypes 参数类型数组
	 * @return
	 */
	protected List queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Object[] args, int[] argTypes) {
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas);
		
		return this.getJdbcTemplate().queryForList(sql_datas, args, argTypes);		// return List<Map>
		
	}
	
	/**
	 * <pre>
	 * 根据表名称和查询条件，获取符合条件的数据的行数。
	 *
	 * </pre>
	 * 
	 * @param tableName 要查询的表名称
	 * @param condition 过滤条件，如“WHERE numCol>100”
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	protected int getSize(String tableName, String condition) {
		//return DBUtils.getSize(tableName, condition, this.getConnection() );

		return this.getJdbcTemplate().queryForInt( "SELECT count(*) FROM "+tableName.toUpperCase()+" "+condition ) ;
		
	}
	

	/**
	 * <pre>获取最近插入的主键值。</pre>
	 * just for MySQL,get id that insert last.
	 * 
	 * @return
	 */
	protected int getLastInsertIdentity() {
		try {
			return ApplicationContext.getInstance().getDatabaseManager().getLastInsertIdentity( this.getConnection() );
			
		} catch (CannotGetJdbcConnectionException e) {
			throw e;

		} catch (SQLException e) {

			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("执行 getLastInsertIdentity 时产生错误!", e);
		
		}
	}


	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * </pre>
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		try{
			return ApplicationContext.getInstance().getDatabaseManager().resultSet2RowSet(rs);
			
		} catch (SQLException e) {
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("ResultSet 转化为 RowSet 时产生异常!!", e);
		}
		
	}

	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * </pre>
	 * 
	 * @param rs
	 * @param dbm 特定的数据库管理者
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm){
		try {
			return dbm.resultSet2RowSet(rs);
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidResultSetAccessException("ResultSet 转化为 RowSet 时产生异常!!", e);
		
		}
	}


	/**
	 * <pre>
	 * add sql statements to a java.sql.Statement Object.
	 * </pre>
	 * 
	 * @author Zeven on 2007-04-24.
	 * @param stmt
	 * @param slq_list
	 * @throws SQLException
	 */
	protected void addBatch(Statement stmt, List slq_list) {
		try {
			DbUtils.addBatch( stmt, slq_list);
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new DataAccessResourceFailureException("Exception when add list of sql to Statement Object!!!",e);
		
		}
		
	}

	
	/**
	 * <pre>
	 * 根据当前的数据库的特殊规则对需要插入的内容做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		escape2Sql 替换 convertString 方法。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * DBUtils.convertString("ab'cd")			="ab''cd"
	 * DBUtils.convertString("ab'c'd")			="ab''c''d"
	 * DBUtils.convertString("ab''cd")			="ab''''cd"
	 * </pre>
	 * 
	 * @param src 需要保存到数据库的一个字段。
	 * @return
	 */
	protected String escape2Sql(String src) {
		
		return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
	}

//	/**
//	 * @deprecated replaced by escape2Sql method.
//	 * @param src
//	 * @return
//	 */
//	protected String convertString(String src) {
//		
//		return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
//	}


	/**
	 * <pre>
	 * 根据当前的数据库语法，返回连接两个字符串的SQL 片断。
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
	protected String concat(String str1, String str2) {
		return ApplicationContext.getInstance().getDatabaseManager().concat( str1, str2 );
	}


	/**
	 * <pre>
	 * 根据当前的数据库语法，转换可能为null的表达式。
	 * 
	 * 		Oracle 数据库返回 nvl(exp1, exp2);
	 * 		MySQL  数据库返回 coalesce( exp1, exp2);
	 * 		SQLServer  数据库返回 isNull(exp1, exp2);
	 * </pre>
	 * @param str1 字符串或者列名
	 * @param str2 字符串或者列名
	 * @return
	 */
	protected String switchNull(String exp1, String exp2) {
		return ApplicationContext.getInstance().getDatabaseManager().switchNull( exp1, exp2 );
	}

	
//	/**
//	 * 获取唯一标识符的表达式。
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName 需要使用标识符的表，如"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	protected String nextUniqueID(String tblName) {
//		return ApplicationContext.getInstance().getDatabaseManager().identity( tblName );
//	}


	/**
	 * <pre>
	 * 获取唯一标识符的表达式。
	 * </pre>
	 * 
	 * @param tblName 需要使用标识符的表，如"tsys_flow","employee","demo_table"
	 * @return
	 */
	protected String identity(String tblName) {
		return ApplicationContext.getInstance().getDatabaseManager().identity( tblName.toUpperCase() );
	}
	

	/**
	 * <pre>
	 * 获取系统时间的函数表达式。
	 * </pre>
	 * @return
	 */	
	protected String sysDatetime() {
		return ApplicationContext.getInstance().getDatabaseManager().sysDatetime() ;
	}
	
	
	/**
	 * <pre>
	 *  返回将数据库日期类型转为字符类型的方法。主要在 select 语句中使用。
	 * 
	 * 	如 Oracle 返回  to_char(birthday,'yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colName 列名称
	 * @return
	 */
	protected String date2Char(String colName) {
		return ApplicationContext.getInstance().getDatabaseManager().date2Char( colName );
	}

	/**
	 * <pre>
	 *  返回将java String值转为数据库日期类型的方法。主要在 insert,update 中使用。
	 * 
	 * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colValue 要插入列的值
	 * @return
	 */
	protected String char2Date(String colValue) {
		return ApplicationContext.getInstance().getDatabaseManager().char2Date( colValue );
	}


	/**
	 * <pre>
	 * 返回将数据库日期时间类型转为字符类型的方法。主要在 select 语句中使用。
	 * 
	 * 	如 Oracle 返回  to_char( beginTime,'yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colName 列名称
	 * @return
	 */
	protected String datetime2Char(String colName) {
		return ApplicationContext.getInstance().getDatabaseManager().datetime2Char( colName );
	}

	/**
	 * <pre>
	 * 返回将java String值转为数据库日期时间类型的方法。主要在 insert,update 中使用。
	 * 
	 * <pre>
	 * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colValue 要插入列的值
	 * @return
	 */
	protected String char2Datetime(String colValue) {
		return ApplicationContext.getInstance().getDatabaseManager().char2Datetime( colValue );
	}

	/**
	 * <pre>
	 * 获取字符型大对象的内容。
	 * </pre>
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected String getClobColumn(String sql) {
		try {
			return ApplicationContext.getInstance().getDatabaseManager().getClobColumn( sql );
			
		} catch (Exception e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new InvalidDataAccessResourceUsageException("获取表的Clob字段产生错误，sql语句为:["+sql+"]!", e);
		}	
	}

	
	/**
	 *  <pre>根据翻页信息，修改查询数据的 sql statement.
	 *  	对所有支持的数据库作统一的处理。 不同的数据库类型处理不相同。
	 *     --- 设置为protected而不是使用private，是为了便于后面灵活的后面的翻页控制放在什么位置。</pre>
	 * @param sql 包装之前的数据查询 statement
	 * @param dataSet 数据载体，包含了取数的信息
	 * @return 根据取数信息进行包装之后的 sql statement.
	 */
	protected String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) {

		// 是否更新总行数信息
		int rowCount = navigator.getRowCount();
		int pageSize = navigator.getPageSize();
		int pageNumber = navigator.getPageNumber();		
		//String action = "query";
		
		//处理翻页操作
		if ( rowCount<0 )
		{
			// 先查出行数
			log.debug("get rowCount and"
					+"\n\tpageSize="  +pageSize
					+"\n\tpageNumber="+pageNumber
					+"\n\tsql="+sql_count);

			rowCount = this.getJdbcTemplate().queryForInt( sql_count );	
			navigator.setRowCount(rowCount);	// 更改总行数
		}
		
		String sql = ApplicationContext.getInstance().getDatabaseManager().getSubResultSetSql( sql_datas, 
				pageSize*(pageNumber-1)+1,  pageSize  );

		return sql;
	}

	/**
	 *  <p>wzw:创建获取总行数的sql，这里提供一个直接根据查询数据的sql生成查询总行数的简单方法，
	 *  	适合快速实现功能，但是运行效率低，需要优化。
	 *     在传入翻页查询的参数生成时使用。
	 *  </p>  
	 * @param sql_datas
	 * @return
	 */
	protected String createSqlForCount( String sql_datas){
		return "Select Count(*) from ("+sql_datas+")";
	}

	/**
	 * <pre>
	 * Zeven: 使用Aop为主控制数据库事务，不要使用这个方法，避免混淆，如果共用Connection，采用参数传递conn的方式。
	 * </pre>
	 * @deprecated 不要使用这个方法。
	 */
	public void setConnection(Connection conn) {
		System.out.println("*** Use Aop for Jdbc translate, not use this setConnection method!!!!!");
	}

	protected String currentDataSourceName = ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName();
	public String getDataSourceName() {
		return this.currentDataSourceName;
	}
	
}
