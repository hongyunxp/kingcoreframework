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

package com.kingcore.framework.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;

import com.kingcore.framework.base.dao.impl.DaoJdbcPlainImpl;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.PlainConnection;


/**
 * <p>一个数据库操作的Bean。 
 * 		> 用于 纯jsp或者jsp+javaBean的快速开发方式；
 * 		> 可以控制事务；
 * 		> 与DAO唯一的区别是：DAO的数据库操作的基本方法都是 private的，只能在子类中调用，严格的区分各层(tiers)；
 * 			DbBean 中的数据库方法都是 public 的，可以在外部直接调用，方便开发。
 * 	
 * 		暴露出来的方法从新设置了名称，对于 do* 名称的方法都是 public 类型的，
 * 							相应的名称修改为 execute* 名称。
 * Zeven， 对于子类 override 父类的方法：
 * 			用 public 覆盖 private 是合法的；
 * 
 * 	  --- 主要是把Dao的方法变为public类型，可以不需要增加Dao类。并增加了下面的个方法：
 *	
 	public static void main(String[] args) throws SQLException {
 		DbBean dbBean = new DbBean();
 		//dbBean.getConnection();
 
 	}
 * 	 </p>
 * 
 * @author Zeven on 2007-7-7
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DbBean extends DaoJdbcPlainImpl implements Serializable {
 	
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws SQLException {
// 		DbBean dbBean = new DbBean();
 		//dbBean.getConnection();
 
 	}
	public void addBatch(Statement stmt, List<String> slq_list) throws SQLException {
		
		super.addBatch(stmt, slq_list);
	}

	public String char2Date(String colValue) {
		
		return super.char2Date(colValue);
	}

	public String char2Datetime(String colValue) {
		
		return super.char2Datetime(colValue);
	}

	public String concat(String str1, String str2) {
		
		return super.concat(str1, str2);
	}

	public String createSqlForCount(String sql_datas) {
		
		return super.createSqlForCount(sql_datas);
	}

	public String createSqlForPage(Navigator navigator, String sql_count,
			String sql_datas) throws SQLException {
		
		return super.createSqlForPage(navigator, sql_count, sql_datas);
	}

	public String date2Char(String colName) {
		
		return super.date2Char(colName);
	}

	public String datetime2Char(String colName) {
		
		return super.datetime2Char(colName);
	}

	public String escape2Sql(String str) {
		
		return super.escape2Sql(str);
	}

	public int[] executeBatch(List<String> list) throws SQLException {
		
		return super.executeBatch(list);
	}


	public int executeUpdate(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.executeUpdate(sql, args, argTypes);
	}

	public int executeUpdate(String sql, Object[] args) throws SQLException {
		
		return super.executeUpdate(sql, args);
	}

	public int executeUpdate(String sql) throws SQLException {
		
		return super.executeUpdate(sql);
	}

//	public void freeConnection() throws SQLException {
//		
//		super.freeConnection();
//	}

	public String getClobColumn(String sql) throws Exception {
		
		return super.getClobColumn(sql);
	}

	public Connection getConnection() throws SQLException {
		
		return super.getConnection();
	}

	public Connection getConnection(String poolName) throws SQLException {
		
		return super.getConnection(poolName);
	}

//	public DataSource getDataSource() throws SQLException {
//		
//		return super.getDataSource();
//	}

	public String getIdentityValue(String tblName) {
		
		return super.getIdentityValue(tblName);
	}

	public int getLastInsertIdentity(Connection conn) throws SQLException {
		
		return super.getLastInsertIdentity(((PlainConnection)conn).getDatabaseManager());
	}

	public int getSize(String tableName, String condition)
			throws SQLException {
		
		return super.getSize(tableName, condition);
	}

	public long identity(String tblName) {
		
		return super.identity(tblName) ;
	}

	public Object queryForBean(String sql, Class<?> beanClass)
			throws SQLException {
		
		return super.queryForBean(sql, beanClass);
	}

	public Object queryForBean(String sql, Object[] args, int[] argTypes,
			Class<?> beanClass) throws SQLException {
		
		return super.queryForBean(sql, args, argTypes, beanClass);
	}

	public int queryForInt(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForInt(sql, args, argTypes);
	}

	public int queryForInt(String sql) throws SQLException {
		
		return super.queryForInt(sql);
	}

	public List<?> queryForList(String sql_datas, Class<?> beanClass)
			throws SQLException {
		
		return super.queryForList(sql_datas, beanClass);
	}

	public List<?> queryForList(String sql_datas, int type) throws SQLException {
		
		return super.queryForList(sql_datas, type);
	}

	public List<?> queryForList(String sql_datas, Object[] args,
			int[] argTypes, Class<?> beanClass) throws SQLException {
		
		return super.queryForList(sql_datas, args, argTypes, beanClass);
	}

	public List<?> queryForList(String sql_datas, Object[] args,
			int[] argTypes, int type) throws SQLException {
		
		return super.queryForList(sql_datas, args, argTypes, type);
	}

	public List<Map<String,Object>> queryForList(String sql_datas, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForList(sql_datas, args, argTypes);
	}

	public List<Map<String,Object>> queryForList(String sql_datas) throws SQLException {
		
		return super.queryForList(sql_datas);
	}

	public long queryForLong(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForLong(sql, args, argTypes);
	}

	public long queryForLong(String sql) throws SQLException {
		
		return super.queryForLong(sql);
	}

	public Map<String, ?> queryForMap(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForMap(sql, args, argTypes);
	}

	public Map<String, ?> queryForMap(String sql) throws SQLException {
		
		return super.queryForMap(sql);
	}

	public List<?> queryForPagedList(Navigator navigator, String sql_count,
			String sql_datas, Class<?> clazz) throws SQLException {
		
		return super.queryForPagedList(navigator, sql_count, sql_datas, clazz);
	}

	public List<?> queryForPagedList(Navigator navigator, String sql_count,
			String sql_datas, Object[] args, int[] argTypes, Class<?> clazz)
			throws SQLException {
		
		return super.queryForPagedList(navigator, sql_count, sql_datas, args, argTypes,
				clazz);
	}

	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count,
			String sql_datas, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForPagedList(navigator, sql_count, sql_datas, args, argTypes);
	}

	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count,
			String sql_datas) throws SQLException {
		
		return super.queryForPagedList(navigator, sql_count, sql_datas);
	}

	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count,
			String sql_datas, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForPagedRowSet(navigator, sql_count, sql_datas, args,
				argTypes);
	}

	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count,
			String sql_datas) throws SQLException {
		
		return super.queryForPagedRowSet(navigator, sql_count, sql_datas);
	}

	public RowSet queryForRowSet(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForRowSet(sql, args, argTypes);
	}

	public RowSet queryForRowSet(String sql) throws SQLException {
		
		return super.queryForRowSet(sql);
	}

	public String queryForString(String sql, Object[] args, int[] argTypes)
			throws SQLException {
		
		return super.queryForString(sql, args, argTypes);
	}

	public String queryForString(String sql) throws SQLException {
		
		return super.queryForString(sql);
	}

	public Object queryForType(String sql, int type) throws SQLException {
		
		return super.queryForType(sql, type);
	}

	public Object queryForType(String sql, Object[] args, int[] argTypes,
			int type) throws SQLException {
		
		return super.queryForType(sql, args, argTypes, type);
	}

	public RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm)
			throws SQLException {
		
		return super.resultSet2RowSet(rs, dbm);
	}

	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {
		
		return super.resultSet2RowSet(rs);
	}

	public String switchNull(String exp1, String exp2) {
		
		return super.switchNull(exp1, exp2);
	}

	public String sysDatetime() {
		
		return super.sysDatetime();
	}
	
	/**
	 * add by wzw on 2013
	 * 在多数据源的情况下，根据需要设置当前要使用的数据源。如果不设置则使用系统默认的数据源。
	 */
	public void setDataSourceName(String dataSourceName) {
		
		super.setDataSourceName(dataSourceName);
	}
	
	public boolean updateBlobColumn(String tablename, String picField,
			String sqlWhere, String strPath) throws Exception {
		
		return super.updateBlobColumn(tablename, picField, sqlWhere, strPath);
	}

	public boolean updateClobColumn(String tablename, String picField,
			String sqlWhere, String content) throws Exception {
		
		return super.updateClobColumn(tablename, picField, sqlWhere, content);
	}
	
}
