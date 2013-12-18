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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;

import com.kingcore.framework.base.dao.DaoJdbc;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.MapResultSetExtractor;
import com.kingcore.framework.jdbc.PlainConnection;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;
import com.kingcore.framework.jdbc.TypeResultSetExtractor;
import com.kingcore.framework.transaction.ConnectionTransactionManager;
import com.kingcore.framework.transaction.TransactionType;

/**
 * <pre>
 *	针对编码控制事务，自己控制事物的方式。
 *		> 适合简单快速开发，不依赖任何其他第三方组件;
 * 		> 不需要配置，需要自己获取连接成员，手动控制事物;
 * 		> 适合的开发结构为两层： Struts + JdbcDao ;
 *		> 含有成员对象conn，涉及到conn使用的地方，都是非线程安全的，一般不要使用单件模式;
 *		> 更多数据库操作方法支持： wzw.util.DbUtils.class
 *
 *		>getConnection() 返回的是 autocommit=true的，供外部自己控制conn的，是一个独立的conn对象；
 *                         而内置方法，首先看是否有事务启用，其次看是否有this.conn成员，再需要的创建新的conn。
 *		>如果需要使用好的事务管理，一个是使用tm.begin,二个是自己获取conn。
 *
 * 	</pre>
 * @author Zeven on 2008-8-20
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DaoJdbcPlainImpl 
			implements DaoJdbc , Serializable {

	
	/* ************************** 下面是@deprecated 的内容 ************************************* */

	public static final String CHANGE_PAGE = "changepage";
	 
//	/**
//	 * <pre>主动释放数据库的连结 wzw on 2006-9-24
//	 * 连接释放之后，该类对象就不能执行数据库操作了，
//	 * 		即便是使用 getConnection()方法之后也不能执行数据库操作了，
//	 * 		所以一个业务操作DAO从开始到结束，如果要主动使用Connection，
//	 * 		则只使用一个Connection，而不是使用多个，而且使用了在一个业务过程中千万不要释放了再获取。
//	 * 		或者完全不管理Connection。</pre>
//	 * @deprecated 直接使用 this.conn.close() 方法关闭连接
//	 * 
//	 */
//	protected void freeConnection() throws SQLException{
//		if(this.conn!=null) {
//			this.conn.close();
//			// Zeven ,主动清除引用。 一般来说掉用了close(),也就不会再执行数据库连接。
//			// 只能使用 conn.close(),不能设置为null,否则将连接池中的对象置为null(空) 了。
//			// 不过如果断开了引用，还可以赋值为null空吗？？？
//			//this.conn = null ;
//		}
//	}


	/**
	 * <pre>执行列表查询处理。</pre>
	 * @deprecated this method is replace by another one，请使用 doQueryDataSet( String sql_datas,String sql_count, int[] pageParams) 方法.
	 * @param request web request object
	 * @param sql_datas SQL statement for query datas
	 * @param sql_count SQL statement for query row number
	 * @throws Exception
	 
	protected NavigableDataSet doQueryDataSet(HttpServletRequest request,
			String sql_datas,
			String sql_count) throws Exception {
		
		return doQueryDataSet( sql_datas, sql_count, getPageParameter(request) );
	}
	 */

//	/**
//	 * 暂时不使用，而是采用直接得到连结 wzw on 2006-9-24
//	 * @deprecated
//	 */
//	protected DataSource getDataSource() throws SQLException{
//		return null;
//	}

	
	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * @deprecated replaced by resultSet2RowSet(ResultSet rs)
	 * @param rs
	 * @return
	 * @throws SQLException 
	protected RowSet populateResultSet(ResultSet rs) throws SQLException{
		return ((PooledConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
	}
	 */

	/* ************************** @deprecated 的内容完毕 ************************************* */

	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志记录对象。
	 */
	protected final static Logger log = Logger.getLogger( DaoJdbcPlainImpl.class);
	
	/// private DataSource dataSource = ApplicationContext.getInstance().getDataSourceProvider().getDataSource(); 

	/// public boolean hasConnection = false;
	//protected DataSource ds;

	/**
	 * 展示调用路径
	 */
    static String showTrace(int maxdepth)
    {
      String stack="调用路径：\n";
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 1; i < Math.min(maxdepth + 1, trace.length); i++)
      {
        stack += "\t[" + trace[i].hashCode() + "]" + trace[i] + "\n";
      }
      return stack;
    }
    
	/**
	 * <pre>获取数据库的连结 wzw on 2006-9-24
	 * 		直接使用了 conn.close() 断开了连接。</pre>
	 * 
	 * @return
	 * @throws SQLException 
	 */
	protected Connection getConnection() throws SQLException{
		return this.getConnection( this.getDataSourceName() );
	}

	/**
	 * 整个对象公用的连接对象，一定要注意事务控制和线程安全。
	 *  >本conn可以属于defaultDataSorceName对应的DS，也可能是其他DataSource。
	 *    this.conn 对应于 this.currentDataSourceName，这里不做成Map<String,Connection>的复杂方式。
	 *  >本conn通过成员共用，实现简单的conn事务管理，如果需要复杂事务管理方式，
	 *  		建议使用TransactionManager或者获取conn自己控制。
	 */
	protected Connection conn = null ;
	
	/**
	 * <pre>获取数据库的连结 wzw on 2006-9-24
	 * 		直接使用了 conn.close() 断开了连接。</pre>
	 * 
	 * @param poolName 连接池对象的名称，没有参数则使用配置的默认连接池
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(String dataSourceName) throws SQLException{
		if(this.conn==null || this.conn.isClosed() ) {
			this.conn = ApplicationContext.getInstance().getDataSourceManager().getConnection(dataSourceName);
		}
		this.conn.setAutoCommit(true);   //默认为自动提交
		this.setDataSourceName(dataSourceName); //同步this.conn设置dataSourceName值
		return this.conn;
	}

	/**
	 * 复杂事务管理方式下的conn获取。
	 * 	> 支持事务嵌套;
	 *  > 多DataSource的复杂事务;
	 *  > 复杂事务管理下的connection获取方法暂时不对外开放。
	 *  
	 *  > 通过上述两种方式获取到的connection根据不同的环境具有不同特点，主要有两种环境，一种是事务环境，另一种是无事务环境，分别描述如下：
	 *  > 如果在事务环境下获取connection，那么connection的事务将与上下文中的事务结合在一起，也就是说connection的事务随着上下文环境事务的提交而提交，也随着上下文事务的回滚而回滚，而且connection的回滚也会导致整个上下文事务的回滚。同时connection的setAutocommit方法和close方法将不会影响上下文事务，就是说不会起作用。
	 *  > 在事务环境下获取数据库链接后，不需显示关闭connection，也不需要显示commit，但是需要显示rollback，事务的提交和链接的关闭由外部事务自动关闭和提交。
	 *  > 在没有事务的环境下获取connection时，connection具有原始的jdbc connection的所有特性和方法。
	 * 
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection(TransactionType transType, String dataSourceName) throws SQLException{
		Connection l_conn = ConnectionTransactionManager.getCurrentConnection(transType, dataSourceName);
		return l_conn;
	}

	private Connection getConnection(TransactionType transType) throws SQLException{
		return getConnection(transType, this.getDataSourceName());
	}
	
	/**
	 * <pre>从外部设置 DAO 对象的 conn 成员值。
	 * 		适用于一个DAO调用另外一个DAO，同时共用Connection的情况。</pre>
	 * 
	 * @param p_conn
	 */
	public void setConnection( Connection p_conn ) {
		this.conn = p_conn ;
	}

	
	/**
	 * <pre>
	 * Execute an SQL SELECT query without any replacement parameters.  The
	 * caller is responsible for connection cleanup.
	 * 	非 Prepared 方式，需要使用 Prepared方式请调用其它方法。 queryForRowSet
	 * </pre>
	 * 
	 * @param sql The query to execute.
	 * @return The object returned by the handler.
	 * @throws SQLException
	public RowSet doQuery(String sql)
	 throws SQLException {

		log.debug("--------------wzw--------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
		RowSet rs = (RowSet) query(sql, null, new RowSetResultSetExtractor() );
		log.debug("--------------wzw--------------"+ ((this.conn==null)?"is null":"not null") );
		
		return rs;

		// drop by Zeven on 2008-11-05
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.doQuery( sql );
//		}else {
//			return DBUtils.doQuery(sql, conn );
//		}
	}
	 */

	
	/**
	 * 
	 * <pre>Execute an SQL INSERT, UPDATE, or DELETE query without replacement
	 *    执行批处理，对于事务问题：
	 * 		1。如果DAO对象当前有conn成员对象，那么本方法不处理任何提交/回滚、关闭事宜；
	 * 		2。如果DAO对象当前并且本身管理事务问题，但是不会设置conn成员对象，
	 * 					所以在DBBean做为静态对象时可以做到不会有conn成员导致的线程冲突问题；
	 * 
	 * 		非 PreparedStatement方式，使用Prepared方式调用其它重载方法。
	 * 	</pre>
	 *
	 * @param conn The connection to use to run the query.
	 * @param sql The SQL to execute.
	 * @return The number of rows updated.
	 * @throws SQLException
	 */
	protected int executeUpdate( String sql ) throws SQLException {
		return executeUpdate(sql, null, null);
	}
	
	/**
	 * @version 3.2
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql, Object arg) throws SQLException {    
		return executeUpdate(sql, new Object[]{arg}, null);
	}
	/**
	 * <pre> doUpdate 的PreparedStatement方式。</pre>
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @return
	 * @throws SQLException
	 */
	protected int executeUpdate(String sql, Object[] args) throws SQLException {    
		return executeUpdate(sql, args, null);
	}
	
	
	/**
	 * <pre> doUpdate 的PreparedStatement方式。
	 *    增加调试内容，使用log4j打开或者关闭调试输出信息。</pre>
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @param argTypes 参数对象数组对应的参数类型, java.sql.Type
	 * @return
	 * @throws SQLException
	 */
	private int executeUpdate(String sql, Object[] args, int[] argTypes) throws SQLException {    

		boolean isConnCreated = false;
		PreparedStatement ps = null;
		Statement stmt = null;
		Connection l_conn = null;
		int intReturn = 0 ;
		try{
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->
			 
			if(args!=null){		// has params or not??
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					log.debug("---------argTypes is null and args.length is:"+args.length);
					SqlUtils.setStatementArg(ps, args);
				}else{
					log.debug("---------argTypes is not null and args.length is:"+args.length);
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
				intReturn = ps.executeUpdate();
		        stmt = ps; //用于后面关闭。
				
			}else{
				stmt = l_conn.createStatement();
				intReturn = stmt.executeUpdate(sql);
			}

			// end : 处理数据库操作部分 --------------------------------------------------<			
			if(isConnCreated && l_conn!=null){		// 提交
				log.debug("---------isConnCreated is true,so commit conn.");
				l_conn.commit();
			}
			log.debug("---------end of all main process.");
			
		}catch(SQLException sqle){			
			if(isConnCreated && l_conn!=null){		// 回滚
				l_conn.rollback();
			}
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + sqle.getMessage() ) ;
			log.debug("debug", sqle);
			throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn, stmt, null);	// 关闭
			}else{
				DbUtils.closeQuietly( stmt );
			}
		}
		
        return intReturn;
	}

	
	/**
	 * 
	 * <pre>执行批处理，对于事务问题：
	 * 		1。如果DAO对象当前有conn成员对象，那么本方法不处理任何提交/回滚、关闭事宜；
	 * 		2。如果DAO对象当前并且本身管理事务问题，但是不会设置conn成员对象，
	 * 					所以在DBBean做为静态对象时可以做到不会有conn成员导致的线程冲突问题；
	 * 
	 * 	</pre>
	 * @param conn 数据库连接对象。
	 * @param allsql 要执行的sql语句组成的数组。
	 * @throws 执行批处理失败。
	 * @return 每个sql语句影响的行数组成的数组。
	 */
	protected int[] executeBatch( List<String> list )
	  throws SQLException {

		int[] returns = null;
		
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		Statement stmt = null;
		Connection l_conn = null;
		try{
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->
        	
        	stmt = l_conn.createStatement();
            addBatch( stmt, list);
            returns = stmt.executeBatch();

			// end : 处理数据库操作部分 --------------------------------------------------<
            if( isConnCreated && l_conn!=null){
            	l_conn.commit() ;	 	//对于外部传入的连接，不提交，不回滚，不关闭
            }
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            if( isConnCreated && l_conn!=null){
            	l_conn.rollback(); 	 //对于外部传入的连接，不提交，不回滚，不关闭
            }
            
        	String info= "Result in doBatch Exception'SQLs is:" ;
        	for(int i=0;i<list.size();i++){
        		info +=list.get(i).toString()+";";
        	}
            log.fatal(DbUtils.class.getName()+" "+ e.getMessage() + ". " + info) ;			
			log.debug("debug", e);
            /// e.pri ntStackTrace();
            throw e;

        } finally {
            if( isConnCreated && l_conn!=null){
            	wzw.util.DbUtils.closeQuietly(l_conn, stmt, null);
            }else{
            	wzw.util.DbUtils.closeQuietly( stmt );
            }
        }
        
	}

	/**
	 * 
	 * <pre>根据表名称和查询条件，获取符合条件的数据的行数。
	 * 	Zeven on 2009-2-11为了解决MySql问题，增加 tableName.toUpperCase()
	 * </pre>
	 * 
	 * @param tableName 要查询的表名称
	 * @param condition 过滤条件，如“WHERE numCol>100”
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	protected int getSize(String tableName, String condition) throws SQLException {
		
		String sql = "SELECT count(*) FROM "+tableName+" "+condition;
		return this.queryForInt(sql);
		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DbUtils.getSize(tableName.toUpperCase(), condition);
//		}else {
//			return DbUtils.getSize(tableName.toUpperCase(), condition, this.conn);
//		}
	}

	
	/**
	 * <pre>根据sql statement，获取符合条件的数据的整数。
	 *   属于 queryForType 查询中 Type.INT 的情况。
	 * 	非PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql sql statement
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	protected int queryForInt(String sql ) throws SQLException {
		
		return queryForInt(sql, null, null);
//		// Zeven: 如何确定调用自己的方法还是 DbUtils里面的方法呢？？
//		// 	 涉及到this.conn的事务统一问题时，最好把实现放在Dao里面，否则可以把实现放在 DbUtils.class里面。
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForInt(sql);
//		}else {
//			return DBUtils.queryForInt(sql, this.conn);
//		}
	}

	protected int queryForInt(String sql, Object arg) throws SQLException {
		Integer iobj = (Integer)query(sql, new Object[]{arg}, null, new TypeResultSetExtractor( Types.INTEGER, false) ); 
		return iobj.intValue();
	}
	
	/**
	 * @version 3.2
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected int queryForInt(String sql, Object[] args ) throws SQLException {
		Integer iobj = (Integer)query(sql, args, null, new TypeResultSetExtractor( Types.INTEGER, false) ); 
		return iobj.intValue();
	}
	
	/**
	 * <pre>
	 * 根据sql statement，获取符合条件的数据的整数。
	 *   属于 queryForType 查询中 Type.INT 的情况。
	 * 	为PreparedStatement方式。
	 * </pre>
	 *
	 * @param sql sql statement
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	private int queryForInt(String sql, Object[] args, int[] argTypes ) throws SQLException {
		Integer iobj = (Integer)query(sql, args, argTypes, new TypeResultSetExtractor( Types.INTEGER, false) ); 
		return iobj.intValue();
	}

	
	/**
	 * <pre>
	 *  根据sql statement，获取符合条件的数据的字符串。
	 *   属于 queryForType 查询中 Type.VARCHAR 的情况。
	 * 	非PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql sql statement
	 * @return 字符串
	 * @throws SQLException 数据库操作异常
	 */
	protected String queryForString(String sql ) throws SQLException {

		return queryForString(sql, null, null);
	}

	/*
	 * @version 3.2
	 */
	protected String queryForString(String sql, Object arg) throws SQLException {
		String str = (String)query(sql, new Object[]{arg}, null, new TypeResultSetExtractor( Types.VARCHAR, false) ); 
		return str;
	}
	
	/**
	 * @version 3.2
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected String queryForString(String sql, Object[] args ) throws SQLException {
		String str = (String)query(sql, args, null, new TypeResultSetExtractor( Types.VARCHAR, false) ); 
		return str;
	}
	
	/**
	 * <pre>
	 *  根据sql statement，获取符合条件的数据的字符串。
	 *   属于 queryForType 查询中 Type.VARCHAR 的情况。
	 * 	为PreparedStatement方式。
	 * </pre>
	 * @param sql sql statement
	 * @return 字符串
	 * @throws SQLException 数据库操作异常
	 */
	private String queryForString(String sql, Object[] args, int[] argTypes ) throws SQLException {
		String str = (String)query(sql, args, argTypes, new TypeResultSetExtractor( Types.VARCHAR, false) ); 
		return str;
	}
		
	/**
	 * 
	 * 根据sql statement，获取符合条件的数据的行数。
	 *
	 * @param sql sql statement
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	protected long queryForLong(String sql ) throws SQLException {
	
		return queryForLong(sql, null,null);
		
//		// Zeven: 如何确定调用自己的方法还是 DbUtils里面的方法呢？？
//		// 	 涉及到this.conn的事务统一问题时，最好把实现放在Dao里面，否则可以把实现放在 DbUtils.class里面。
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForLong(sql);
//		}else {
//			return DBUtils.queryForLong(sql, this.conn);
//		}

	}

	/**
	 * @version 3.2
	 * @param sql
	 * @param arg
	 * @return
	 * @throws SQLException
	 */
	protected long queryForLong(String sql, Object arg) throws SQLException {
	
		Long lobj = (Long)query(sql, new Object[]{arg}, null, new TypeResultSetExtractor( Types.BIGINT, false) ); 
		return lobj.longValue();
	}
	
	/**
	 * @version 3.2
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected long queryForLong(String sql, Object[] args ) throws SQLException {
	
		Long lobj = (Long)query(sql, args, null, new TypeResultSetExtractor( Types.BIGINT, false) ); 
		return lobj.longValue();
	}
	
	private long queryForLong(String sql, Object[] args, int[] argTypes ) throws SQLException {
	
		Long lobj = (Long)query(sql, args, argTypes, new TypeResultSetExtractor( Types.BIGINT, false) ); 
		return lobj.longValue();
	}

	
	/**
	 * <pre>
	 *  执行指定的sql语句，将结果集包装为List<Map>对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。
	 * </pre>   
	 * @param sql_datas 需要执行查询的sql语句
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForList(String sql_datas) throws SQLException {

		return queryForList(sql_datas, null, null );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toMapList(rs);
		
	}
	
	/**
	 * 
	 * @param sql_datas
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForList(String sql_datas, Object arg) throws SQLException {

		return (List<Map<String,Object>>)query(sql_datas, new Object[]{arg}, null, new MapResultSetExtractor(true) );		
	}
	/**
	 * @version 3.2
	 * @param sql_datas
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForList(String sql_datas, Object[] args ) throws SQLException {

		return (List<Map<String,Object>>)query(sql_datas, args, null, new MapResultSetExtractor(true) );		
	}
	
	/**
	 * <pre>采用PrepareStatement方式执行指定的sql语句，将结果集包装为List<Map>对象返回。
	 * </pre>
	 * @param sql_datas 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> queryForList(String sql_datas, Object[] args, int[] argTypes) throws SQLException {

		return (List<Map<String,Object>>)query(sql_datas, args, argTypes, new MapResultSetExtractor(true) );		
	}
	
	
	/**
	 * <pre>
	 *   执行指定的sql语句，将结果集包装为List<Bean>对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。
	 * </pre>   
	 * @param sql_datas 需要执行查询的sql语句
	 * @param modelObject 返回List对象中的Bean对象类型
	 * @return
	 * @throws SQLException 
	 */
	protected List<?> queryForList(Class<?> dataBean, String sql_datas) throws SQLException {

		return queryForList(dataBean, sql_datas, null, null );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
	}

	/**
	 * add in  2013
	 * @param dataBean
	 * @param sql_datas
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(Class<?> dataBean, String sql_datas, Object arg) throws SQLException {

		return (List<?>)queryBean(dataBean, sql_datas, new Object[]{arg}, null, true );	
	}
	
	/**
	 * @version 3.2
	 * @param dataBean
	 * @param sql_datas
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(Class<?> dataBean, String sql_datas, Object[] args ) throws SQLException {

		return (List<?>)queryBean(dataBean, sql_datas, args, null, true );	
	}
	
	/**
	 * 	采用PrepareStatement方式执行指定的sql语句，将结果集包装为List<Bean>对象返回。
	 * @param sql_datas 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @param modelObject 返回List对象中的Bean对象类型
	 * @return
	 * @throws SQLException
	 */
	private List<?> queryForList(Class<?> dataBean, String sql_datas, Object[] args, int[] argTypes ) throws SQLException {

		return (List<?>)queryBean(dataBean, sql_datas, args, argTypes, true );	
	}


	/**
	 * <pre> 执行指定的sql语句，将结果集包装为List<Type>对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。 </pre>
	 * @param sql_datas 需要执行查询的sql语句
	 * @param type java.sql.Types,返回List对象中的基本对象类型
	 * @return
	 * @throws SQLException 
	 */
	protected List<?> queryForList(int type, String sql_datas) throws SQLException {

		return queryForList(type, sql_datas, null, null );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
	}


	/**
	 * @version 3.2
	 * @param type java.sql.Types
	 * @param sql_datas
	 * @param arg
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(int type, String sql_datas, Object arg) throws SQLException {

		return (List<?>)query(sql_datas, new Object[]{arg}, null, new TypeResultSetExtractor(type, true) );	
	}
	
	/**
	 * @version 3.2
	 * @param type java.sql.Types
	 * @param sql_datas
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForList(int type, String sql_datas, Object[] args ) throws SQLException {

		return (List<?>)query(sql_datas, args, null, new TypeResultSetExtractor(type, true) );	
	}
	
	/**
	 * 	采用PrepareStatement方式执行指定的sql语句，将结果集包装为List<Type>对象返回。
	 * @param sql_datas 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @param type java.sql.Types,返回List对象中的基本对象类型
	 * @return
	 * @throws SQLException
	 */
	private List<?> queryForList(int type, String sql_datas, Object[] args, int[] argTypes) throws SQLException {

		return (List<?>)query(sql_datas, args, argTypes, new TypeResultSetExtractor(type, true) );	
	}

	
	/**
	 * <pre>
	 * 执行指定的sql语句，将结果集包装为RowSet对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。
	 * </pre>   
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForRowSet( String sql) throws SQLException {
		
		return queryForRowSet(sql, null, null );
	}


	protected RowSet queryForRowSet( String sql, Object[] args ) throws SQLException {

		return (RowSet) query(sql, args, null, new RowSetResultSetExtractor() );
	}
	/**
	 * <pre>
	 * 采用PrepareStatement方式执行指定的sql语句，将结果集包装为RowSet对象返回。
	 * </pre>
	 * @param sql 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @return
	 * @throws SQLException
	 */
	private RowSet queryForRowSet( String sql, Object[] args, int[] argTypes) throws SQLException {

		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	
	
	/**
	 * 
	 * <pre>获取指定翻页的RowSet。
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @author Zeven on 2008-5-26
	 * @param navigator 翻页信息数组
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );		
		return queryForRowSet(sql);
		
	}

	/**
	 * 
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	protected RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas,
					Object[] args ) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return queryForRowSet(sql, args, null);
		
	}
	/**
	 * PrepareStatement方式的queryForPagedRowSet。
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @return
	 * @throws SQLException
	 */
	private RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas,
					Object[] args, int[] argTypes) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return queryForRowSet(sql, args, argTypes);
		
	}
	
	
	/**
	 * <pre>获取指定翻页的List，每行使用一个Map封装数据，即返回类型为List<Map>。
	 * 	使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @param navigator 翻页信息数组
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @return
	 */
	protected List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql);
	
	}

	/**
	 * @version 3.2
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas,
					Object[] args) throws SQLException {
		
		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, args, null);
	}
	/**
	 * <pre>获取指定翻页的List，每行使用一个Map封装数据，即返回类型为List<Map>。
	 * 	 使用PreparedStatement方式。</pre>
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas,
					Object[] args, int[] argTypes) throws SQLException {
		
		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, args, argTypes);
	}

	
	/**
	 * <pre>获取指定翻页的List<JavaBean>。
	 * 	    使用Statement，而不是PreparedStatement，需要使用PreparedStatement则使用重载的方法。</pre>
	 * @param navigator 翻页信息数组
	 * @param sql_count 获取总数量的sql语句
	 * @param sql_datas 获取数据的sql语句
	 * @param clazz List里面的对象，对应一行数据，可以为null
	 * @return
	 */
	protected List<?> queryForPagedList(Navigator navigator, Class<?> dataBean, String sql_count, String sql_datas ) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(dataBean, sql);
	}
	
	/**
	 * @version 3.2
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @param args
	 * @param argTypes
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	protected List<?> queryForPagedList(Navigator navigator, Class<?> dataBean, 
			String sql_count, String sql_datas,Object[] args ) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(dataBean, sql, args, null);
	}
	
	/**
	 * <pre>获取指定翻页的List<JavaBean>。
	 * 	    使用PreparedStatement方式。</pre>
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	private List<?> queryForPagedList(Navigator navigator, Class<?> dataBean, String sql_count, String sql_datas, 
				Object[] args, int[] argTypes) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(dataBean, sql, args, argTypes );
	}
	
	
	/**
	 * 
	 * <pre>
	 *  获取下一个唯一标识值。
	 * 	Zeven on 2009-2-11为了解决MySql问题，增加 tableName.toUpperCase()
	 * </pre>
	 * 
     * @param tblName 需要使用id的表的名称，如"tsys_flowtype"
	 * @return 序列的下一个值
	 * @throws SQLException 数据库操作失败异常。
	 */
	protected long identity( String tblName ) {
		
		DatabaseManager databaseManager = null;
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->
		
			if(l_conn instanceof PlainConnection){
				log.debug("---------get DatabaseManager from PlainConnection.");
				databaseManager = ((PlainConnection)l_conn).getDatabaseManager();
			}else{
				log.debug("---------get DatabaseManager from ApplicationContext.");
				databaseManager = ApplicationContext.getInstance().getDatabaseManager();
			}
			
			return databaseManager.getIdentityValue(tblName.toUpperCase(), l_conn);

			// end : 处理数据库操作部分 --------------------------------------------------<
			
		}catch(SQLException sqle){
            log.error("debug", sqle);
			// throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn );	// 关闭
			} 
		}
		
		return -1; // exception happen
		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.getIdentityValue( tblName);
//		}else {
//			return DBUtils.getIdentityValue( tblName);
//			//return DBUtils.getIdentityValue( tblName, conn);	//, conn 避免扰乱事务，为了性能还是加上去，根据具体情况处理。
//			
//			/// return ((PooledConnection)conn).getDatabaseManager().getSequenceValue( seqName, conn);
//			// return DBUtils.getIdentityValue( tblName, conn);
//		}
		
	}
	

//	/**
//	 * Zeven set 'public ' to 'protected'.
//     * @deprecated replaced by getNextUniqueIDValue(String tblName)
//	 * 
//	 * 获取Oracle数据库指定序列的下一个序列值。
//	 * 
//     * @param tblName 需要使用id的表的名称，如"tsys_flowtype"
//	 * @return 序列的下一个值
//	 * @throws SQLException 数据库操作失败异常。
//	 */
//	protected int getNextUniqueIDValue( String tblName ) throws SQLException{
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.getIdentityValue( tblName);
//		}else {
//			return DBUtils.getIdentityValue( tblName);
//		}
//		
//	}


	/**
	 * <pre>获取最近插入的主键值。</pre>
	 * just for MySQL,get id that insert last.
	 * 
	 * @return
	 */
	protected int getLastInsertIdentity(DatabaseManager databaseManager ) throws SQLException {

		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->
				
			return databaseManager.getLastInsertIdentity( l_conn );

			// end : 处理数据库操作部分 --------------------------------------------------<
		}catch(SQLException e){
			log.error(e);
			throw e;
			
		}finally{
			if (isConnCreated ) {
				wzw.util.DbUtils.closeQuietly(l_conn);
			}
		}
	}
	

	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * </pre>   
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.resultSet2RowSet(rs);
	}
	
	/**
	 * <pre>
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * </pre>   
	 * @param rs
	 * @param dbm 特定的数据库管理者
	 * @return
	 * @throws SQLException 
	 */
	protected RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException{
		return dbm.resultSet2RowSet(rs);
	}


	/**
	 * 
	 * add sql statements to a java.sql.Statement Object.
	 * @author Zeven on 2007-04-24.
	 * @param stmt
	 * @param slq_list
	 * @throws SQLException
	 */
	protected void addBatch(Statement stmt, List<String> slq_list) throws SQLException{
		DbUtils.addBatch( stmt, slq_list);
		
	}
	
	
	/**
	 * <pre>
	 * 根据当前的数据库的特殊规则对需要插入的内容做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		escape2Sql 替换 convertString 方法。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * 
	 * DBUtils.convertString("ab'cd")			="ab''cd"
	 * DBUtils.convertString("ab'c'd")			="ab''c''d"
	 * DBUtils.convertString("ab''cd")			="ab''''cd"
	 * </pre>
	 * @param src 需要保存到数据库的一个字段。
	 * @return
	 */
	protected String escape2Sql(String str) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.escape2Sql(str);
	}


	/**
	 * <pre>
	 * 	
	 * 根据当前的数据库语法，返回连接两个字符串的SQL 片断。
	 *   改进要求： public 类型 修改为 protected 类型。
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
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.concat( str1, str2 );
	}


	/**
	 * <pre>
	 * 	
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
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.switchNull( exp1, exp2 );
	}

	
//	/**
//	 * 获取唯一标识符的表达式。
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName 需要使用标识符的表，如"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	protected String nextUniqueID(String tblName) {
//		return ((PooledConnection)conn).getDatabaseManager().identity( tblName );
//	}


	/**
	 * <pre>
	 * 	
	 * 获取唯一标识符的表达式。
	 * 	Zeven on 2009-2-11为了解决MySql问题，增加 tableName.toUpperCase()
	 * </pre>
	 * 
	 * @param tblName 需要使用标识符的表，如"tsys_flow","employee","demo_table"
	 * @return
	 */
	protected String getIdentityValue(String tblName) {
		return this.identity(tblName)+"";
	}
	

	/**
	 * <pre>
	 * 获取系统时间的函数表达式。
	 * </pre>
	 * 
	 * @return
	 */	
	protected String sysDatetime() {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.sysDatetime() ;
		//return ((PlainConnection)this.conn).getDatabaseManager().sysDatetime() ;
	}
	
	
	protected DatabaseManager getCurrentDatabaseManager() { 
		
		DatabaseManager databaseManager = null;
		boolean isConnCreated = false;
		//PreparedStatement ps = null;
		//Statement stmt = null;
		Connection l_conn = null;
		//int intReturn = 0 ;
		try{
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION );
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->
		
			if(l_conn instanceof PlainConnection){
				log.debug("---------get DatabaseManager from PlainConnection.");
				databaseManager = ((PlainConnection)l_conn).getDatabaseManager();
			}else{
				log.debug("---------get DatabaseManager from ApplicationContext.");
				databaseManager = ApplicationContext.getInstance().getDatabaseManager();
			}
			
			// end : 处理数据库操作部分 --------------------------------------------------<
			
		}catch(SQLException sqle){
            log.error("debug", sqle);
			// throw sqle;
		
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(l_conn );	// 关闭
			} 
		}
		
        return databaseManager;
	}


	/**
	 * <pre>返回将数据库日期类型转为字符类型的sql片段的方法。主要在 select 语句中使用。
	 * 
	 * 	如 Oracle 返回  to_char(birthday,'yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colName 列名称
	 * @return
	 */
	protected String date2Char(String colName) {

		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.date2Char( colName );
	}

	/**
	 * <pre>返回将java String值转为数据库日期类型的sql片段的方法。主要在 insert,update 中使用。
	 * 
	 * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd')
	 * </pre>
	 * 
	 * @param colValue 要插入列的值
	 * @return
	 */
	protected String char2Date(String colValue) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.char2Date( colValue );
	}

	/**
	 * <pre>返回将数据库日期时间类型转为字符类型的sql片段的方法。主要在 select 语句中使用。
	 * 
	 * 	如 Oracle 返回  to_char( beginTime,'yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colName 列名称
	 * @return
	 */
	protected String datetime2Char(String colName) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.datetime2Char( colName );
	}

	/**
	 * <pre>返回将java String值转为数据库日期时间类型的sql片段的方法。主要在 insert,update 中使用。
	 * 
	 * 	如 Oracle 返回  to_date('2007-01-01','yyyy-mm-dd hh24:mi:ss')
	 * </pre>
	 * 
	 * @param colValue 要插入列的值
	 * @return
	 */
	protected String char2Datetime(String colValue) {
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.char2Datetime( colValue );
	}

	
	/*
	 protected void populate(Object bean, ResultSet rs) throws SQLException {
	 ResultSetMetaData metaData = rs.getMetaData();
	 int ncolumns = metaData.getColumnCount();
	 
	 HashMap properties = new HashMap();
	 // Scroll to next record and pump into hashmap
	  for (int i=1; i<=ncolumns ; i++) {
	  properties.put(sql2javaName(metaData.getColumnName(i)), rs.getString(i));
	  }
	  // Set the corresponding properties of our bean
	   try {
	   BeanUtils.populate(bean, properties);
	   } catch (InvocationTargetException ite) {
	   throw new SQLException("BeanUtils.populate threw " + ite.toString());
	   } catch (IllegalAccessException iae) {
	   throw new SQLException("BeanUtils.populate threw " + iae.toString());
	   }
	   }
	   
	   public int getSize(String tableName, String condition) throws SQLException {
	   Connection conn = null;
	   PreparedStatement pstmt = null;
	   ResultSet rs = null;
	   try {
	   String sql = "SELECT count(*) FROM "+tableName+" "+condition;
	   conn = ds.getConnection();
	   pstmt = conn.prepareStatement(sql);
	   rs = pstmt.executeQuery();
	   rs.next();
	   int size = rs.getInt(1);
	   close(rs);
	   close(pstmt);
	   return size;
	   } catch (SQLException sqle) {
	   close(rs);
	   close(pstmt);
	   rollback(conn);
	   log.debug("debug", sqle);
	   /// sqle.pri ntStackTrace();
	   throw sqle;
	   } finally {
	   close(conn);
	   }
	   }
	   
	   */
	
	 
//	/**
//	 * @deprecated
//	 */
//	protected String java2sqlName(String name) {
//		String column = "";
//		for (int i = 0; i < name.length(); i++) {
//			if (i < name.length() - 1 && (name.charAt(i) >= 'a' && name.charAt(i) <= 'z') &&
//					(name.charAt(i + 1) >= 'A' && name.charAt(i + 1) <= 'Z')) {
//				column += name.charAt(i) + "_";
//			} else {
//				column += name.charAt(i);
//			}
//		}
//		return column.toLowerCase();
//	}
 
	
//	/**
//	 * Zeven set 'public ' to 'protected'.
//	 * 
//	 * 查询翻页导航数据, just for xxxJdbcDaoImpl。
//	 * @param sql_datas
//	 * @param sql_count
//	 * @param pageParams 请求的页面信息参数 {rowCount, pageSize, pageNumber}
//	 * @return
//	 * @throws Exception
//	 */
//	protected NavigableDataSet queryForNavigableRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new RowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableSqlRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams, Class elementType) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, RowMapper rowMapper) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, ResultSetExtractor rse) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	protected NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams ) throws Exception
//	{
//		// 默认导航数据对象是 RowSet，高性能。
//		return doQueryDataSet( sql_datas, sql_count,pageParams );
//	}
	
//	/**
//	 * <pre>
//	 * 查询翻页导航数据。
//	 * 
//	 * </pre>
//	 * @deprecated wzw:建议使用 queryForPaged... 方法，再在页面与导航器等组合。
//	 * @param pageParams 请求的页面信息参数 {rowCount, pageSize, pageNumber}
//	 * @param sql_count 查询总行数的sql语句
//	 * @param sql_datas 查询数据的sql语句
//	 * 
//	 */
//	protected NavigableDataSet executeQueryDataSet( Navigator navigator,
//			String sql_count, String sql_datas) throws Exception
//	{
//		
//		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
//		RowSet crs = this.queryForRowSet( sql_datas );
//		NavigableDataSet dataSet = new QueryDataSet(navigator,null,crs);	//
//		
//		return dataSet;	// not PreparedStatement
//
////		-------------------------- drop by Zeven on 2008-09-19		
////		int rowCount = pageParams[0];
////		int pageSize = pageParams[1];
////		int pageNumber = pageParams[2];
////		
////		NavigableDataSet dataSet = new QueryDataSet();	//
////		dataSet.setPageSize( pageSize );
////		dataSet.setCurrentPageIndex( pageNumber );
////		
////		//String action = "query";
////		
////		//处理翻页操作
////		String sql = null;
////		if ( rowCount<0 )
////		{
////			// 先查出行数
////			sql = sql_count ;
////			log.debug("get rowCount and"
////					+"\n\tpageSize="  +pageSize
////					+"\n\tpageNumber="+pageNumber
////					+"\n\tsql="+sql);
////
////			//.out.println("-------------------- this -3------1 ");
////			RowSet rs=this.doQuery(sql);
////			if(rs.next()){
////				rowCount = rs.getInt(1);
////			}	
////		}
////		
////		// 进一步设置 dataSet 的信息
////		dataSet.setRowCount(rowCount);
////		dataSet.setPageCount( (rowCount - 1) / pageSize + 1 );
////		
////		//搜索查询
////		//排序处理
////		//缺省的查询
////
////		// 再查询数据集
////		sql = this.createQuerySql(sql_datas, dataSet) ;
////		log.debug("get datas rowCount="+rowCount+" and \n\tsql="+sql );
////
////		dataSet.addData( doInnerQuery( sql) );
////		return dataSet ;
////		/// this.executeQuery(mapping, request, sql);
//		
//	}
 

	/**
	 *  <pre>根据翻页信息，修改查询数据的 sql statement.
	 *  	对所有支持的数据库作统一的处理。 不同的数据库类型处理不相同。
	 *      navigator 参数是in/out类型，其中的值可能被修改，比如总行数信息。
	 *      >>>注意，这里的参数 (navigator,sql_count,sql_datas) 与 doQueryDataSet(sql_datas,sql_count,navigator) 相反了。
	 *     --- 设置为protected而不是使用private，是为了便于后面灵活的后面的翻页控制放在什么位置。</pre>
	 * @param sql 包装之前的数据查询 statement
	 * @param dataSet 数据载体，包含了取数的信息
	 * @return 根据取数信息进行包装之后的 sql statement.
	 * @throws SQLException 
	 */
	protected String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) throws SQLException {

		// 是否更新总行数信息
		int rowCount = navigator.getRowCount();
		int pageSize = navigator.getPageSize();
		int pageNumber = navigator.getPageNumber();
		//String action = "query";
		
		//处理翻页操作
		if ( rowCount<1 )   //include 0
		{
			// 先查出行数
			log.debug("get rowCount and"
					+"\n\tpageSize="  +pageSize
					+"\n\tpageNumber="+pageNumber
					+"\n\tsql="+sql_count);

			rowCount = this.queryForInt( sql_count );	
			//pageParams[0] = rowCount;	// 更改总行数
			navigator.setRowCount(rowCount);
		}

		DatabaseManager dbm = getCurrentDatabaseManager();
		String sql = dbm.getSubResultSetSql( sql_datas, pageSize*(pageNumber-1)+1,  pageSize  );

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
	 * 将二进制大对象保存到数据库的表中。
	 *   BLOB(Binary   Large   Object)   
	 *     可用来存储无结构的二进制数据。（类似于row和long   row）
	 * </pre>
	 * 
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	protected boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		
		DatabaseManager dbm = getCurrentDatabaseManager();
		if(this.conn==null || this.conn.isClosed()) {
			return dbm.updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
					null);
		}else {
			return dbm.updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
 					this.conn );
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
			   					String content) throws Exception{

		DatabaseManager dbm = getCurrentDatabaseManager();
		if(this.conn==null || this.conn.isClosed()) {
			return dbm.updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  null );
		}else {
			return dbm.updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  this.conn );
		}
		
	} 

	
	/**
	 * <pre>
	 * 获取字符型大对象的内容。
	 * 
	 * </pre>
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected String getClobColumn(String sql) throws Exception{
		DatabaseManager dbm = getCurrentDatabaseManager();
		return dbm.getClobColumn( sql );	
	}

//	/**
//	 *  <p>根据翻页信息，修改查询数据的 sql statement.
//	 *  	对所有支持的数据库作统一的处理。 不同的数据库类型处理不相同。 </p>
//	 * @param sql 包装之前的数据查询 statement
//	 * @param dataSet 数据载体，包含了取数的信息
//	 * @return 根据取数信息进行包装之后的 sql statement.
//	 */
//	private String createQuerySql(String sql, NavigableDataSet dataSet) {
//
////		//String orderByString="";
////		sql =
////			"Select t.* " +
////			"  From (select Rownum As rowseq,t.* From ( "+sql+") t ) t " +
////			" WHERE rowseq BETWEEN "+(dataSet.getPageSize()*(dataSet.getCurrentPageIndex()-1)+1)+" AND "+dataSet.getPageSize()*(dataSet.getCurrentPageIndex());
////		log.debug("---"+sql);
//
//		sql = ((PooledConnection)conn).getDatabaseManager().getSubResultSetSql( sql, 
//							dataSet.getPageSize()*(dataSet.getCurrentPageIndex()-1)+1, 
//							dataSet.getPageSize()  );
//
//		return sql;
//	} 

	/** 
	 * <pre>
	 * ******************** 执行查询可调用的类 ***************************
	 * doQuery 的 Prepared 方式。
	 * </pre>
	 * 
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @return
	 * @throws SQLException
	protected RowSet doQuery(String sql, Object[] args) throws SQLException {
		return (RowSet) query(sql, args, new RowSetResultSetExtractor() );
	}
	 */
	
	/**
	 * <pre>
	 * doQuery 的 Prepared 方式。
	 * </pre>
	 * 
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @param argTypes 参数对象数组对应的参数类型, 取值为java.sql.Type的内容
	 * @return
	 * @throws SQLException
	private RowSet doQuery(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	 */
		
	//map 保存多个conn，支持多dataSource同时操作
	///protected Map<String,Connection> connsMap = new HashMap<String,Connection>(); 
	
	/** 
	 * <pre>
	 * ******************** 执行查询的最终执行类 ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws SQLException
	 */
	private Object query(String sql, Object[] args, int[] argTypes, ResultSetExtractor rse) throws SQLException {
		
		boolean isConnCreated = false;
		Connection l_conn = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION ); //有事务则先加入事务
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}
			// begin : 处理数据库操作部分 -------------------------------------------------->

			log.debug("--------------wzw--2------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--2------------"+ ((l_conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = l_conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();
		        stmt = ps; //用于后面关闭。
			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  rse.extractData(rs);

			// begin : 处理数据库操作部分 --------------------------------------------------<
	        
		}finally{

			//DbUtils.closeQuietly( ps );
			if(isConnCreated){				// 查询，只需要关闭，不需要提交or回滚
				log.debug("-----------1---wzw--需要关闭conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly(l_conn, stmt, rs);
			}else{
				log.debug("-----------1---wzw--不需要关闭conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly( null, stmt , rs);
			}
		}
		
        return objReturn;
	}

	

	/** 
	 * <pre>
	 *   对应Bean/BeanList的查询，为什么重新做一个底层方法？其主要目的是可以使用一个固定的
	 *  ResultSetExtractor对象，这样可以在系统中注入，而不需要每次新建一个，便于传递参数。
	 *     
	 * ******************** 执行查询的最终执行类 ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param rse
	 * @return
	 * @throws SQLException
	 */
	private Object queryBean(Class<?> dataBean, String sql, Object[] args, int[] argTypes, boolean isList) throws SQLException {  //ResultSetExtractor rse
		
		boolean isConnCreated = false;
		PreparedStatement ps = null;
		Connection l_conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			//先查找是否有事务管理，有则加入现有事务，没有这是用this.conn成员对象
			l_conn = this.getConnection( TransactionType.MAYBE_TRANSACTION ); //有事务则先加入事务
			if(l_conn==null){
				if( this.conn==null || this.conn.isClosed() ) {
					this.conn = this.getConnection();
					isConnCreated = true;
					log.debug("---------get new conn and assign to this.conn");
				}
				l_conn = this.conn;
			}

			log.debug("--------------wzw--2------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
			//log.debug("--------------wzw--2------------"+ ((l_conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = l_conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = l_conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();
		        stmt = ps; //用于后面关闭。
			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  ApplicationContext.getInstance().getResultSetBeanExtractor().extractData(rs, dataBean, isList );

		}finally{

			//DbUtils.closeQuietly( ps );
			if(isConnCreated){				// 查询，只需要关闭，不需要提交or回滚
				log.debug("-----------1---wzw--需要关闭conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly(l_conn, stmt, rs);
			}else{
				log.debug("--------------wzw--不需要关闭conn begin------------"+ ((l_conn==null||l_conn.isClosed())?"not connected":"connected") );
				DbUtils.closeQuietly( null, stmt , rs);
			}
		}
		
        return objReturn;
	}

	
	/** 
	 * <pre>
	 * ******************** 执行查询的最终执行类 ***************************
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param rse
	 * @return
	 * @throws SQLException
	 */ 
	@SuppressWarnings("unused")
	private Object query(String sql, Object[] args, ResultSetExtractor rse) throws DataAccessException, SQLException {
		return query(sql, args, null, rse);
	}
	

	/**
	 * <pre>
	 * 执行指定的sql statement，并返回指定的类型，
	 * 	如 Integer, Long, Float, Double, Timestamp, Date, Time等。
	 *  非 PreparedStatement方式。
	 * </pre>
	 * @param type java.sql.Types,返回List对象中的基本对象类型
	 * @param sql sql statement
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	protected Object queryForType(int type ,String sql) throws SQLException {
		return queryForType(type, sql, null, null);
	}

	/**
	 * @version 3.2
	 * @param type java.sql.Types,返回List对象中的基本对象类型
	 * @param sql
	 * @param arg
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForType(int type ,String sql, Object arg) throws SQLException {

		return query(sql, new Object[]{arg}, null, new TypeResultSetExtractor(type, false) ); 
	}
	/**
	 * @version 3.2
	 * @param type java.sql.Types,返回List对象中的基本对象类型
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForType(int type ,String sql, Object[] args ) throws SQLException {

		return query(sql, args, null, new TypeResultSetExtractor(type, false) ); 
	}
	
	/**
	 * <pre>
	 * 执行指定的sql statement，并返回指定的类型，
	 * 	如 Integer, Long, Float, Double, Timestamp, Date, Time等。
	 *    PreparedStatement方式。
	 * </pre>
	 * 
	 * @param type java.sql.Tpyes 里面的值，表示返回类型
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	private Object queryForType(int type ,String sql, Object[] args, int[] argTypes) throws SQLException {

		return query(sql, args, argTypes, new TypeResultSetExtractor(type, false) ); 
	}
	


	/**
	 * <pre>
	 * 	执行指定的sql statement，并返回指定的类型的JavaBean对象。
	 *    非PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql
	 * @param Class 返回对象的类型
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForBean(Class<?> dataBean, String sql) throws SQLException {
		return queryForBean(dataBean, sql, null, null);
	}
	
	/**
	 * a
	 * @param sql
	 * @param args
	 * @param dataBean
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForBean(Class<?> dataBean, String sql, Object arg) throws SQLException {
		return queryForBean(dataBean, sql, new Object[]{arg}, null );
	}
	
	/**
	 * add on 2013
	 * @param sql
	 * @param args
	 * @param dataBean
	 * @return
	 * @throws SQLException
	 */
	protected Object queryForBean(Class<?> dataBean, String sql, Object[] args ) throws SQLException {
		return queryForBean(dataBean, sql, args, null );
	}
	
	/**
	 * <pre>
	 * 	执行指定的sql statement，并返回指定的类型的JavaBean对象。
	 *    PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param Class 返回对象的类型
	 * @return
	 * @throws SQLException
	 */
	private Object queryForBean(Class<?> dataBean, String sql, Object[] args, int[] argTypes ) throws SQLException {
		return queryBean(dataBean, sql, args, argTypes, false );  
	}	


	/**
	 * <pre>
	 * 	执行指定的sql statement，并返回指定的Map对象。
	 *    非PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql
	 * @return 对一行数据封装的Map对象
	 * @throws SQLException
	 */
	protected Map<String, ?> queryForMap(String sql ) throws SQLException {
		return queryForMap(sql, null, null);
	}

	/**
	 * 
	 * @param sql
	 * @param arg
	 * @return
	 * @throws SQLException
	 */
	protected Map<String, ?> queryForMap(String sql, Object arg ) throws SQLException {
		return (Map<String, ?>) query(sql, new Object[]{arg}, null, new MapResultSetExtractor(false) );  
	}
	/**
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return
	 * @throws SQLException
	 */
	protected Map<String, ?> queryForMap(String sql, Object[] args ) throws SQLException {
		return (Map<String, ?>) query(sql, args, null, new MapResultSetExtractor(false) );  
	}
	
	/**
	 * <pre>
	 * 	执行指定的sql statement，并返回指定的Map对象。
	 *    PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @return 对一行数据封装的Map对象
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, ?> queryForMap(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (Map<String, ?>) query(sql, args, argTypes, new MapResultSetExtractor(false) );  
	}

	//目前的dataSourceName，初始默认为系统级别的默认值。
	protected String currentDataSourceName = ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName();
	protected void setDataSourceName(String dataSourceName) {
		this.currentDataSourceName = dataSourceName;
		
	}
	public String getDataSourceName() {
		return this.currentDataSourceName;
	}
	private void setDataSourceNameToDefault() {
		this.setDataSourceName(ApplicationContext.getInstance().getDataSourceManager().getDefaultDataSourceName());		
	}
	
}

