 
package wzw.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;

import com.kingcore.framework.bean.NavigableDataSet;
import com.kingcore.framework.bean.Navigator;
import com.kingcore.framework.bean.QueryDataSet;
import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DataSourceManager;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.MapResultSetExtractor;
import com.kingcore.framework.jdbc.PlainConnection;
import com.kingcore.framework.jdbc.RowSetResultSetExtractor;
import com.kingcore.framework.jdbc.TypeResultSetExtractor;


/**
 * <p>封装了一些对数据的操作：
 * 		获取数据库连接池的连接，执行批量更新，提交事务等等。
 *   最好的DbBean类，通过设置 ApplicationContext, 支持多数据源注入，并提供数据库事务支持；
 *    参考：DaoJdbcPlainImpl.java  DbUtils.java </p>
 * @author	WUZEWEN on 2006-4-13
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @since	JDK1.4
 */

public class  DbBean {
	
	private Connection conn = null;
	private Logger log = Logger.getLogger(DbBean.class);
	private static DataSourceManager dataSourceManager = null;
//	private  ResultSet rs = null;
//	public  Exception sqlexp=null;
//	private ResultSetMetaData resultmetadata = null;
//	private int ok=0;

	//private String dbpool;
	
	/**
	 * 因为DbBean可以创建多个，所以poolName使用私有，而不是static，以免产生混乱。
	 * 这样此DbBean可以放到b/s结构中，多用户使用一个VM，各自有自己的poolName。
	 *    ComponentUtil.java则只能在C/S结构中的GUI系统，一般不会多用户使用一个
	 * VM，所以即使其poolName是static的，也只会有一个用户使用。
	 * 
	 */
	public static String DefaultPoolName="main";
	
	public static String getDefaultPoolName() {
		return DefaultPoolName;
	}

	/**
	 * you can set defaultPoolName for all DbBean instances.
	 * @param defaultPoolName
	 */
	public static void setDefaultPoolName(String defaultPoolName) {
		DefaultPoolName = defaultPoolName;
	}
	
	private String currentPoolName = DefaultPoolName;  //每次构建DbBean实例时候初始。

	public DbBean(){
		this(DefaultPoolName);
	}
	
	/**
	 * 
	 * @param poolName 使用到的数据库连接池名称
	 */
	public DbBean (String poolName){
		this.currentPoolName = poolName;
	}
	
	/**
	 * @return Returns the poolName.
	 */
	public String getPoolName() {
		return currentPoolName;
	}

	/**
	 * @param poolName The poolName to set.
	 */
	public void setPoolName(String poolName) {
		this.currentPoolName = poolName;
	}

	/**
	 * @return 数据库连接对象conn
	 */
	public Connection getConnection() throws SQLException{
		return getConnection( DefaultPoolName );
	}


	/**
	 * 本对象内部公用的数据库更新方法，执行一系列的sql语句，如果失败，返回false并且抛出SQLException。
	 * @param vv sql statements collectioins
	 * @return return true if executeUpdate success,return false if executeUpdate failed.
	 * @throws SQLException 
	 */
	public int[] executeBatch(Vector<String> vv) throws SQLException{
		// 参数判断
		if( vv==null){
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < vv.size(); i++) {
			list.add(vv.get(i).toString());
		}
		return executeBatch(list);
	}

	/**
	 * <p>比doUpdate(Vector)好，后者会导致异常：Microsoft][ODBC Microsoft Access Driver] Could not update; currently locked</p>
	 * @author Zeven on 2011-3-9
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) throws SQLException{
		// 参数判断
		if( sql==null){
			return true;
		}
		boolean isConnCreated = false;
		// 根据参数解析sql语句，并执行、提交，或者抛出异常
		Statement var_stmt = null; 
		try
		{
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				this.conn.setAutoCommit(true);
				log.debug("set AutoCommit to true,done.");  //只有调试代码的时候输出，不作为一般的提示信息输出。
				isConnCreated = true;
			}else{
				log.debug("this.conn exists,so need not to get new one.");
			}
			var_stmt = this.conn.createStatement();
			log.debug("executeUpdate,sql="+sql); //只有调试代码的时候输出，不作为一般的提示信息输出。
			var_stmt.executeUpdate(sql);
			//this.conn.commit();   //不需要提交：如果获取了新conn，则自动提交；如果使用已存在的，则不管提交与回滚；
			
		} catch(SQLException se) {
			if(isConnCreated && this.conn!=null){
				wzw.util.DbUtils.rollbackQuietly( this.conn);
			}
			se.printStackTrace();
			throw se;
			
		} finally {
			wzw.util.DbUtils.closeQuietly( var_stmt );
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				wzw.util.DbUtils.closeQuietly( this.conn );
			}
 
		}
		
		return true;
	}

 
// ------------------- copy from DaoJdbcPlainImpl.java
	/**
	 * <pre>主动释放数据库的连结 wzw on 2006-9-24
	 * 连接释放之后，该类对象就不能执行数据库操作了，
	 * 		即便是使用 getConnection()方法之后也不能执行数据库操作了，
	 * 		所以一个业务操作DAO从开始到结束，如果要主动使用Connection，
	 * 		则只使用一个Connection，而不是使用多个，而且使用了在一个业务过程中千万不要释放了再获取。
	 * 		或者完全不管理Connection。</pre>
	 * @deprecated 直接使用 this.conn.close() 方法关闭连接
	 * 
	 */
	public void freeConnection() throws SQLException{
		if(this.conn!=null) {
			this.conn.close();
			// Zeven ,主动清除引用。 一般来说掉用了close(),也就不会再执行数据库连接。
			// 只能使用 conn.close(),不能设置为null,否则将连接池中的对象置为null(空) 了。
			// 不过如果断开了引用，还可以赋值为null空吗？？？
			//this.conn = null ;
		}
	}
 
	/**
	 * 暂时不使用，而是采用直接得到连结 wzw on 2006-9-24 
	 */ 

	
	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * @deprecated replaced by resultSet2RowSet(ResultSet rs)
	 * @param rs
	 * @return
	 * @throws SQLException 
	public RowSet populateResultSet(ResultSet rs) throws SQLException{
		return ((PooledConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
	}
	 */

	/**
	 *  获取Oracle数据库指定序列的下一个序列值。
	 *    本方法限定了是Oracle的序列方式，不建议使用，用 getIdentityValue(tblName)替换；
	 *  
	 * @deprecated 用 getIdentityValue(tblName)替换
	 * @param seqName 序列对象名称，如"seq_employee"
	 * @return 序列的下一个值
	 * @throws SQLException 数据库操作失败异常。
	public int getSequenceValue( String seqName) 
	  throws SQLException {
		if(this.conn==null || this.conn.isClosed()) {
			return DBUtils.getSequenceValue( seqName);
		}else {
			/// return ((PooledConnection)conn).getDatabaseManager().getSequenceValue( seqName, conn);
			return DBUtils.getSequenceValue( seqName, conn);	// oracle 获取序列可以共用连接
		}
	}
	 */

	/* ************************** @deprecated 的内容完毕 ************************************* */

	/// private DataSource dataSource = ApplicationContext.getInstance().getDataSourceProvider().getDataSource(); 
 
	/**
	 * 展示调用路径
	 */
   public static String showTrace(int maxdepth)
    {
      String stack="调用路径：\n";
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 1; i < Math.min(maxdepth + 1, trace.length); i++)
      {
        stack += "\t[" + trace[i].hashCode() + "]" + trace[i] + "\n";
      }
      return stack;
    }
    
//	/**
//	 * <pre>获取数据库的连结 wzw on 2006-9-24
//	 * 		直接使用了 conn.close() 断开了连接。</pre>
//	 * 
//	 * @return
//	 * @throws SQLException 
//	 */
//	public Connection getConnection() throws SQLException{
//		if(this.conn==null || this.conn.isClosed() ) {
//			this.conn = ApplicationContext.getInstance().getDataSourceProvider().getConnection( );
//			log.debug("获取新连接时hashcode="+this.conn.hashCode()+"，本类hashcode="+this.hashCode()+"。调用路径："+showTrace(8) ); //
//		}else{
//			log.debug("使用已有连接hashcode="+this.conn.hashCode()+"，本类hashcode="+this.hashCode()+"。调用路径："+showTrace(8) ); //
//		}
//		return this.conn;
//		
//	}
	
	/**
	 * <pre>获取数据库的连结 wzw on 2006-9-24
	 * 		直接使用了 conn.close() 断开了连接。</pre>
	 * 
	 * @param poolName 连接池对象的名称，没有参数则使用配置的默认连接池
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String poolName) throws SQLException{
		// 下面的连接池获取连接，在产生错误时应该要抛出异常的，但是连接池模块中没有抛。
		// 保持原有代码不变。
		return ApplicationContext.getInstance().getDataSourceManager().getConnection(poolName);
		
//		if( DbBean.dataSourceManager!=null ){
//			return DbBean.dataSourceManager.getConnection(poolName);
//		}else{
//			throw new SQLException("no dataSourceManager!");
//		}
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
	
//	private RowSet doQuery(String sql, Connection conn )
//	 throws SQLException {
//		return DBUtils.doQuery(sql, conn );
//	}

	
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
//	public int executeUpdate( String sql ) throws SQLException {    
//		 
//		//return doUpdate(sql, null, null);
//		
//		if(this.conn==null || this.conn.isClosed()) {
//			return DbUtils.executeUpdate( sql );
//		}else {
//			return DbUtils.executeUpdate( sql , conn);
//			
//		}
//	}
	
	/**
	 * <pre> doUpdate 的PreparedStatement方式。</pre>
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object[] args) throws SQLException {    
		return executeUpdate(sql, args, null);
	}
	
	
	/**
	 * <pre> doUpdate 的PreparedStatement方式。</pre>
	 * @param sql	带有?的sql语句
	 * @param args	填充?的参数对象数组
	 * @param argTypes 参数对象数组对应的参数类型, java.sql.Type
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object[] args, int[] argTypes) throws SQLException {    

		boolean isConnCreated = false;
		PreparedStatement ps = null;
		int intReturn = 0 ;
		try{
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				this.conn.setAutoCommit(true);
				log.debug("this.conn.setAutoCommit to true."); //调试代码时候才输出
				isConnCreated = true;
			}else{
				log.debug("this.conn exists,so need not to get new one.");
			}
			 
			ps = this.conn.prepareStatement(sql);
			
			if(args!=null){		// has params or not??
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
			}
			
			intReturn = ps.executeUpdate();

//			if(isConnCreated && this.conn!=null){		// 提交
//				this.conn.commit();  //不需要提交：如果获取了新conn，则自动提交；如果使用已存在的，则不管提交与回滚；
//			}
			
		}catch(SQLException sqle){			
			if(isConnCreated && this.conn!=null){		// 回滚
				DbUtils.rollbackQuietly(this.conn);
			}
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + sqle.getMessage() ) ;
            sqle.printStackTrace();
			throw sqle;
		
		}finally{
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				DbUtils.closeQuietly(this.conn, ps, null);	// 关闭
			}else{
				DbUtils.closeQuietly( ps );
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
	public int[] executeBatch( List<String> list )
	  										throws SQLException {

		int[] retArr = null;
		boolean isConnCreated = false;
		Statement var_stmt = null;
		// 根据参数解析sql语句，并执行、提交，或者抛出异常
		try
		{
			if(this.conn==null || this.conn.isClosed()) {
				conn=this.getConnection();
				conn.setAutoCommit(true);
				log.debug("executeBatch -> this.conn.setAutoCommit to true.");
				isConnCreated = true;
			}else{
				log.debug("executeBatch -> this.conn exists,so need not to get new one.");
			}
			var_stmt = this.conn.createStatement();
			for (int i = 0; i < list.size(); i++) {
				var_stmt.addBatch(list.get(i));
			}
			retArr = var_stmt.executeBatch();
//			this.conn.commit();  //不需要提交：如果获取了新conn，则自动提交；如果使用已存在的，则不管提交与回滚；
			
		} catch(SQLException se) {
			if(this.conn==null || this.conn.isClosed()) {
				wzw.util.DbUtils.closeQuietly(this.conn);
			}
			se.printStackTrace();
			throw se;
			
		} finally {
			wzw.util.DbUtils.closeQuietly( var_stmt );
			if(isConnCreated && this.conn!=null){
				this.conn.setAutoCommit(false);
				wzw.util.DbUtils.closeQuietly( this.conn );
			}
		}
		
		return retArr;
	}

	/**
	 * 
	 * <pre>根据表名称和查询条件，获取符合条件的数据的行数。
	 * 	Zeven on 2009-2-11为了解决MySql问题，增加 tableName.toUpperCase()
	 * </pre>
	 * @deprecated 可以使用 queryForInt 替换本方法。
	 * @param tableName 要查询的表名称
	 * @param condition 过滤条件，如“WHERE numCol>100”
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	public int getSize(String tableName, String condition) throws SQLException {
		if(this.conn==null || this.conn.isClosed()) {
			return DbUtils.getSize(tableName.toUpperCase(), condition);
		}else {
			return DbUtils.getSize(tableName.toUpperCase(), condition, this.conn);
		}
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
	public int queryForInt(String sql ) throws SQLException {
		
		return queryForInt(sql, null, null);
//		// Zeven: 如何确定调用自己的方法还是 DbUtils里面的方法呢？？
//		// 	 涉及到this.conn的事务统一问题时，最好把实现放在Dao里面，否则可以把实现放在 DbUtils.class里面。
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForInt(sql);
//		}else {
//			return DBUtils.queryForInt(sql, this.conn);
//		}
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
	public int queryForInt(String sql, Object[] args, int[] argTypes ) throws SQLException {
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
	public String queryForString(String sql ) throws SQLException {

		return queryForString(sql, null, null);
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
	public String queryForString(String sql, Object[] args, int[] argTypes ) throws SQLException {
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
	public long queryForLong(String sql ) throws SQLException {
	
		return queryForLong(sql, null,null);
		
//		// Zeven: 如何确定调用自己的方法还是 DbUtils里面的方法呢？？
//		// 	 涉及到this.conn的事务统一问题时，最好把实现放在Dao里面，否则可以把实现放在 DbUtils.class里面。
//		if(this.conn==null || this.conn.isClosed()) {
//			return DBUtils.queryForLong(sql);
//		}else {
//			return DBUtils.queryForLong(sql, this.conn);
//		}

	}

	public long queryForLong(String sql, Object[] args, int[] argTypes ) throws SQLException {
	
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
	public List<Map<String,Object>> queryForList(String sql_datas) throws SQLException {

		return queryForList(sql_datas, null, null );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toMapList(rs);
		
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
	public List<Map<String,Object>> queryForList(String sql_datas, Object[] args, int[] argTypes) throws SQLException {

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
	public List<Object> queryForList(String sql_datas, Class<?> beanClass) throws SQLException {

		return queryForList(sql_datas, null, null, beanClass );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
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
	@SuppressWarnings("unchecked")
	public List<Object> queryForList(String sql_datas, Object[] args, int[] argTypes, Class beanClass) throws SQLException {

		return (List<Object>)queryBean(sql_datas, args, argTypes, beanClass, true );	
	}


	/**
	 * <pre> 执行指定的sql语句，将结果集包装为List<Type>对象返回。
	 *    需要使用PrepareStatement方式请使用重载的方法。 </pre>
	 * @param sql_datas 需要执行查询的sql语句
	 * @param type 返回List对象中的java.sql.Types 中的基本对象类型
	 * @return
	 * @throws SQLException 
	 */
	public List<Object> queryForList(String sql_datas, int type) throws SQLException {

		return queryForList(sql_datas, null, null, type );
		
//		RowSet rs = this.doQuery( sql_datas );
//		return ResultSetConverter.toBeanList(rs, modelObject);
		
	}


	/**
	 * 	采用PrepareStatement方式执行指定的sql语句，将结果集包装为List<Type>对象返回。
	 * @param sql_datas 需要执行查询的sql语句
	 * @param args PrepareStatement方式设置的参数值数组
	 * @param argTypes PrepareStatement方式设置的参数值数组对应的类型数组
	 * @param type 返回List对象中的java.sql.Types 中的基本对象类型
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<Object> queryForList(String sql_datas, Object[] args, int[] argTypes, int type) throws SQLException {

		return (List<Object>)query(sql_datas, args, argTypes, new TypeResultSetExtractor(type, true) );	
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
	public RowSet queryForRowSet( String sql) throws SQLException {
		
		return queryForRowSet(sql, null, null );
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
	public RowSet queryForRowSet( String sql, Object[] args, int[] argTypes) throws SQLException {

		// return this.doQuery( sql );	// not PreparedStatement
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
	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );		
		return queryForRowSet(sql);
		
	}

	/**
	 * PrepareStatement方式的queryForPagedRowSet。
	 * @param navigator
	 * @param sql_count
	 * @param sql_datas
	 * @return
	 * @throws SQLException
	 */
	public RowSet queryForPagedRowSet(Navigator navigator, String sql_count, String sql_datas,
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
	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql);
	
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
	public List<Map<String,Object>> queryForPagedList(Navigator navigator, String sql_count, String sql_datas,
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
	public List<Object> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, Class<?> clazz) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, clazz);
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
	public List<Object> queryForPagedList(Navigator navigator, String sql_count, String sql_datas, 
				Object[] args, int[] argTypes, Class<?> clazz) throws SQLException {

		String sql = this.createSqlForPage(navigator, sql_count, sql_datas );
		return this.queryForList(sql, args, argTypes, clazz);
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
	public long getIdentityValue( String tblName ) throws SQLException{

		log.debug("---------------------wzw--1");
		//return ((PooledConnection)conn).getDatabaseManager().getIdentityValue(tblName.toUpperCase(), null);

		return -1L;
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
//	 * Zeven set 'public ' to 'public'.
//     * @deprecated replaced by getNextUniqueIDValue(String tblName)
//	 * 
//	 * 获取Oracle数据库指定序列的下一个序列值。
//	 * 
//     * @param tblName 需要使用id的表的名称，如"tsys_flowtype"
//	 * @return 序列的下一个值
//	 * @throws SQLException 数据库操作失败异常。
//	 */
//	public int getNextUniqueIDValue( String tblName ) throws SQLException{
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
	public int getLastInsertIdentity(DatabaseManager databaseManager) throws SQLException {
		return databaseManager.getLastInsertIdentity( conn );
	}
	

	/**
	 * populate ResulteSet Object to RowSet Object.  DateBase is Oracle 10i.
	 *    对于直接来至数据库的封装，统一采用 RowSet，CachedRowSet(wzw)只做为特殊地方的处理。
	 * </pre>   
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException{
		return ((PlainConnection)conn).getDatabaseManager().resultSet2RowSet(rs);
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
	public RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException{
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
	public void addBatch(Statement stmt, List<String> slq_list) throws SQLException{
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
	public String escape2Sql(String str) {
		
		return ((PlainConnection)conn).getDatabaseManager().escape2Sql(str);
	}


	/**
	 * <pre>
	 * 	
	 * 根据当前的数据库语法，返回连接两个字符串的SQL 片断。
	 *   改进要求： public 类型 修改为 public 类型。
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
	public String concat(String str1, String str2) {
		return ((PlainConnection)conn).getDatabaseManager().concat( str1, str2 );
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
	public String switchNull(String exp1, String exp2) {
		return ((PlainConnection)conn).getDatabaseManager().switchNull( exp1, exp2 );
	}

	
//	/**
//	 * 获取唯一标识符的表达式。
//	 * 
//   * @deprecated replaced by nextUniqueID(String tblName)
//	 * @param tblName 需要使用标识符的表，如"tsys_flow","employee","demo_table"
//	 * @return
//	 */
//	public String nextUniqueID(String tblName) {
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
	public String identity(String tblName) {
		return ((PlainConnection)conn).getDatabaseManager().identity( tblName.toUpperCase() );
	}
	

	/**
	 * <pre>
	 * 获取系统时间的函数表达式。
	 * </pre>
	 * 
	 * @return
	 */	
	public String sysDatetime() {
		return ((PlainConnection)conn).getDatabaseManager().sysDatetime() ;
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
	public String date2Char(String colName) {
		return ((PlainConnection)conn).getDatabaseManager().date2Char( colName );
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
	public String char2Date(String colValue) {
		return ((PlainConnection)conn).getDatabaseManager().char2Date( colValue );
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
	public String datetime2Char(String colName) {
		return ((PlainConnection)conn).getDatabaseManager().datetime2Char( colName );
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
	public String char2Datetime(String colValue) {
		return ((PlainConnection)conn).getDatabaseManager().char2Datetime( colValue );
	}

	
	/*
	 public void populate(Object bean, ResultSet rs) throws SQLException {
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
	
	
//	public DAO(DataSource ds) {
//	this.ds = ds;
//	}
	
//	public void setDataSource(DataSource ds) {
//	this.ds = ds;
//	}
	
	
//	public void close(ResultSet rs) {
//	if (rs != null) {
//	try {
//	rs.close();
//	} catch (SQLException e) {
//	}
//	rs = null;
//	}
//	}
	
//	public void close(PreparedStatement pstmt) {
//	if (pstmt != null) {
//	try {
//	pstmt.close();
//	} catch (SQLException e) {
//	}
//	pstmt = null;
//	}
//	}
	
//	public void close(Connection conn) {
//	if (conn != null) {
//	try {
//	conn.close();
//	} catch (SQLException e) {
//	log.debug("debug", e);
//	/// e.pri ntStackTrace();
//	}
//	conn = null;
//	}
//	}
	
//	public void rollback(Connection conn) {
//	if (conn != null) {
//	try {
//	conn.rollback();
//	} catch (SQLException e) {
//	log.debug("debug", e);
//	///e.pri ntStackTrace();
//	}
//	conn = null;
//	}
//	}
	
//	/**
//	 * @deprecated
//	 */
//	public String java2sqlName(String name) {
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
//	 * @deprecated
//	 * @param name
//	 * @return
//	 */
//	public String sql2javaName(String name) {
//		String column = "";
//		for (int i = 0; i < name.length(); i++) {
//			if (name.charAt(i) == '_') {
//				column += ++i<name.length()?String.valueOf(name.charAt(i)).toUpperCase():"";
//			} else {
//				column += name.charAt(i);
//			}
//		}
//		return column;
//	}
	
	
//	/**
//	 * Zeven set 'public ' to 'public'.
//	 * 
//	 * 查询翻页导航数据, just for xxxJdbcDaoImpl。
//	 * @param sql_datas
//	 * @param sql_count
//	 * @param pageParams 请求的页面信息参数 {rowCount, pageSize, pageNumber}
//	 * @return
//	 * @throws Exception
//	 */
//	public NavigableDataSet queryForNavigableRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new RowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableSqlRowSet( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableList( String sql_datas,
//			String sql_count, int[] pageParams, Class elementType) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams, new SqlRowSetResultSetExtractor() );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, RowMapper rowMapper) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams, ResultSetExtractor rse) throws Exception
//	{
//		return queryForNavigableDataSet( sql_datas, sql_count,pageParams );
//	}
//	public NavigableDataSet queryForNavigableDataSet( String sql_datas,
//			String sql_count, int[] pageParams ) throws Exception
//	{
//		// 默认导航数据对象是 RowSet，高性能。
//		return doQueryDataSet( sql_datas, sql_count,pageParams );
//	}
	
	/**
	 * <pre>
	 * 查询翻页导航数据。
	 * 
	 * </pre>
	 * @deprecated wzw:建议使用 queryForPaged... 方法，再在页面与导航器等组合。
	 * @param pageParams 请求的页面信息参数 {rowCount, pageSize, pageNumber}
	 * @param sql_count 查询总行数的sql语句
	 * @param sql_datas 查询数据的sql语句
	 * 
	 */
	public NavigableDataSet executeQueryDataSet( Navigator navigator,
			String sql_count, String sql_datas) throws Exception
	{
		
		sql_datas = this.createSqlForPage(navigator, sql_count, sql_datas );
		RowSet crs = this.queryForRowSet( sql_datas );
		NavigableDataSet dataSet = new QueryDataSet(navigator,null,crs);	//
		
		return dataSet;	// not PreparedStatement

//		-------------------------- drop by Zeven on 2008-09-19		
//		int rowCount = pageParams[0];
//		int pageSize = pageParams[1];
//		int pageNumber = pageParams[2];
//		
//		NavigableDataSet dataSet = new QueryDataSet();	//
//		dataSet.setPageSize( pageSize );
//		dataSet.setCurrentPageIndex( pageNumber );
//		
//		//String action = "query";
//		
//		//处理翻页操作
//		String sql = null;
//		if ( rowCount<0 )
//		{
//			// 先查出行数
//			sql = sql_count ;
//			log.debug("get rowCount and"
//					+"\n\tpageSize="  +pageSize
//					+"\n\tpageNumber="+pageNumber
//					+"\n\tsql="+sql);
//
//			//.out.println("-------------------- this -3------1 ");
//			RowSet rs=this.doQuery(sql);
//			if(rs.next()){
//				rowCount = rs.getInt(1);
//			}	
//		}
//		
//		// 进一步设置 dataSet 的信息
//		dataSet.setRowCount(rowCount);
//		dataSet.setPageCount( (rowCount - 1) / pageSize + 1 );
//		
//		//搜索查询
//		//排序处理
//		//缺省的查询
//
//		// 再查询数据集
//		sql = this.createQuerySql(sql_datas, dataSet) ;
//		log.debug("get datas rowCount="+rowCount+" and \n\tsql="+sql );
//
//		dataSet.addData( doInnerQuery( sql) );
//		return dataSet ;
//		/// this.executeQuery(mapping, request, sql);
		
	}
 

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
	public String createSqlForPage(Navigator navigator, String sql_count, String sql_datas ) throws SQLException {

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

			rowCount = this.queryForInt( sql_count );	
			//pageParams[0] = rowCount;	// 更改总行数
			navigator.setRowCount(rowCount);
		}
		
		String sql = ((PlainConnection)conn).getDatabaseManager().getSubResultSetSql( sql_datas, 
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
	public String createSqlForCount( String sql_datas){
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
	public boolean  updateBlobColumn(String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		

		if(this.conn==null || this.conn.isClosed()) {
			return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
					null);
		}else {
			return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
					picField,
					sqlWhere,
					strPath,
 					this.conn );
		}
		
	}
	

	/**
	 * <pre>
	 * 		为了兼容程序，再改为 public 类型，最好是 public 类型。
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
	public boolean updateClobColumn(String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception{

		if(this.conn==null || this.conn.isClosed()) {
			return ((PlainConnection)conn).getDatabaseManager().updateClobColumn(  tablename, 
					  picField, 
 					  sqlWhere,
 					  content,
 					  null );
		}else {
			return ((PlainConnection)conn).getDatabaseManager().updateClobColumn(  tablename, 
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
	public String getClobColumn(String sql) throws Exception{
		return ((PlainConnection)conn).getDatabaseManager().getClobColumn( sql );	
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
	
	
//	/**
//	 * 做内部查询。
//	 * @param sql
//	 * @return
//	 * @throws Exception
//	 */
//	private Page doInnerQuery(  String sql ) throws Exception
//	{
//		//log.debug("doQuery()此次查询的sql语句为：" + sql );
//		RowSet datas = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		//ResultSetMetaData rsmd = null;
//		int count = 0;
//		//int i = 0, colcount = 0;
//		//String[] colName;
//		boolean isConnCreated = false;
//
//		try
//		{
//			//.out.println("doQuery()此次查询的sql语句为："  );
//			if( this.conn==null || this.conn.isClosed() ) {
//				// cann't using self's getConnection() method.
//				this.conn = getConnection();
//				isConnCreated = true;
//			}
//			
//			//.out.println("1---" +sql);
//			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//			//SQLUtils.fillStatement( pstmt, this.getParams() ) ;
//			//.out.println("2---" +sql);
//			rs = pstmt.executeQuery();
//			//.out.println("3---" +sql);
////			if(rs.next()){
////			rs.last();
////			//.out.println("size"+rs.getRow());
////			rs.beforeFirst();
////			}else{
////			.out.println("no cord");
////			}
//			//log.fatal("have executeQuery get hsdgs--------jyj") ;
//			
//			//.out.println(sql);
//			
//			//datas.populate( rs ) ;
//			
//			datas = this.resultSet2RowSet( rs );
//			
//			//.out.println("4---" +sql);
//			/////  count = datas.size() ;
//			//.out.println("5---" +sql);
//			//.out.println("count="+count);
//			
//			
//			//rsmd = set.getMetaData();
//			//colcount = rsmd.getColumnCount();
//			//colName = new String[colcount];
//			//for (i = 1; i <= colcount; i++)
//			//{
//			//    colName[i - 1] = rsmd.getColumnName(i);
//			//}
//			//
//			//set = pstmt.executeQuery();  twice??
//			//log.fatal("have executeQuery2 " + colName[1]);
//			//while (set.next())
//			//{
//			//    count++;
//			//   log.fatal("have executeQuery3 "+set.getObject(colName[1]));
//			//    if (count < start + 1 || count > end + 1)
//			//        continue;
//			//    DataBean bean = new DataBean();
//			//    for (i = 0; i < colcount; i++)
//			//    {
//			//        bean.put(colName[i], set.getObject(colName[i]));
//			//    }
//			//    datas.add(bean);
//			//}
//		}
//		catch (SQLException se)
//		{
//			log.fatal("doQuery()中出SQLException错误，错误为：" + se.getMessage() + "\n\tsql statement is:"+sql );
//			log.debug("debug", se);
//			/// se.pri ntStackTrace();
//			throw se;
//			///mapping.findForward("");
//		}
//		catch (Exception e)
//		{
//			log.fatal("doQuery()中出Exception错误，错误为：" + e.getMessage() + "\n\tsql statement is:"+sql );
//			log.debug( e.getMessage(), e );
//			/// e.pri ntStackTrace();
//			/// mapping.findForward("");
//			throw e;
//		}
//		finally
//		{
//			try
//			{
//				if (rs != null)
//				{
//					rs.close();
//				}
//				if (pstmt != null)
//				{
//					pstmt.close();
//				}
//				if ( isConnCreated && conn!=null )
//				{
//					conn.close();
//				}
//			}
//			catch (SQLException se)
//			{
//				log.fatal("doQuery的finally中关闭出错，错误为：" + se.getMessage());
//			}
//		}
//		//.out.println("str_pageSize "+ pageSize);
//		return new Page(datas, 0, 0, 0, count);
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
	public RowSet doQuery(String sql, Object[] args) throws SQLException {
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
	public RowSet doQuery(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (RowSet) query(sql, args, argTypes, new RowSetResultSetExtractor() );
	}
	 */
		

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
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				isConnCreated = true;
				log.debug("create a new this.conn for query.");
			}else{
				log.debug("this.conn exists, need not to create a new one.");
			}

			log.debug("--------------wzw--2------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--2------------"+ ((this.conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = this.conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = this.conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();

			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
//			if (rse==null) {
//				//
//			}
	        objReturn =  rse.extractData( rs);

		}finally{

			DbUtils.closeQuietly( ps );
			
			if(isConnCreated){				// 查询，只需要关闭，不需要提交or回滚
				log.debug("-----------1---wzw--需要关闭conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------2---wzw--需要关闭conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly(this.conn, stmt, rs);
        		log.debug("-----------3---wzw--需要关闭conn end------------"+ (this.conn.isClosed()?"conn isClosed":"conn not closed") );
				log.debug("-----------4---wzw--需要关闭conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------5---wzw--需要关闭conn end------------"+ ((this.conn==null)?"is null":"not null") );
			}else{
				log.debug("--------------wzw--不需要关闭conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--不需要关闭conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly( null, stmt , rs);
				log.debug("--------------wzw--不需要关闭conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--不需要关闭conn end------------"+ ((this.conn==null)?"is null":"not null") );
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
	private Object queryBean(String sql, Object[] args, int[] argTypes, Class<?> beanClass,boolean isList) throws SQLException {  //ResultSetExtractor rse
		
		boolean isConnCreated = false;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		Object objReturn = null;
		try{
			log.debug("--------------wzw--1------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--1------------"+ ((this.conn==null)?"is null":"not null") );
			if(this.conn==null || this.conn.isClosed()) {
				this.conn = this.getConnection();
				isConnCreated = true;
			}

			log.debug("--------------wzw--2------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
			log.debug("--------------wzw--2------------"+ ((this.conn==null)?"is null":"not null") );
			if (args==null) {		// Statement
				stmt = this.conn.createStatement();
		        rs = stmt.executeQuery(sql);
				
			} else {				// PrepareStatement
				ps = this.conn.prepareStatement(sql);
				if(argTypes==null){
					SqlUtils.setStatementArg(ps, args);
				}else{
					SqlUtils.setStatementArg(ps, args, argTypes);
				}
		        rs = ps.executeQuery();

			}

	        //crs.populate( rs ) ;
	        //RowSet crs = resultSet2RowSet( rs ) ;
	        objReturn =  ApplicationContext.getInstance().getResultSetBeanExtractor().extractData(rs, beanClass, isList );

		}finally{

			DbUtils.closeQuietly( ps );
			
			if(isConnCreated){				// 查询，只需要关闭，不需要提交or回滚
				log.debug("-----------1---wzw--需要关闭conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------2---wzw--需要关闭conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly(this.conn, stmt, rs);
        		log.debug("-----------3---wzw--需要关闭conn end------------"+ (this.conn.isClosed()?"conn isClosed":"conn not closed") );
				log.debug("-----------4---wzw--需要关闭conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("-----------5---wzw--需要关闭conn end------------"+ ((this.conn==null)?"is null":"not null") );
			}else{
				log.debug("--------------wzw--不需要关闭conn begin------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--不需要关闭conn begin------------"+ ((this.conn==null)?"is null":"not null") );
				DbUtils.closeQuietly( null, stmt , rs);
				log.debug("--------------wzw--不需要关闭conn end------------"+ ((this.conn==null||this.conn.isClosed())?"not connected":"connected") );
				log.debug("--------------wzw--不需要关闭conn end------------"+ ((this.conn==null)?"is null":"not null") );
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
	private Object query(String sql, Object[] args, ResultSetExtractor rse) throws SQLException {
		return query(sql, args, null, rse);
	}
	

	/**
	 * <pre>
	 * 执行指定的sql statement，并返回指定的类型，
	 * 	如 Integer, Long, Float, Double, Timestamp, Date, Time等。
	 *  非 PreparedStatement方式。
	 * </pre>
	 *
	 * @param sql sql statement
	 * @return 符合条件的行数
	 * @throws SQLException 数据库操作异常
	 */
	public Object queryForType(String sql, int type ) throws SQLException {
		return queryForType(sql, null, null, type);
	}

	/**
	 * <pre>
	 * 执行指定的sql statement，并返回指定的类型，
	 * 	如 Integer, Long, Float, Double, Timestamp, Date, Time等。
	 *    PreparedStatement方式。
	 * </pre>
	 * 
	 * @param sql
	 * @param args
	 * @param argTypes
	 * @param type java.sql.Tpyes 里面的值，表示返回类型
	 * @return
	 * @throws SQLException
	 */
	public Object queryForType(String sql, Object[] args, int[] argTypes, int type ) throws SQLException {

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
	public Object queryForBean(String sql, Class<?> beanClass ) throws SQLException {
		return queryForBean(sql, null, null, beanClass);
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
	public Object queryForBean(String sql, Object[] args, int[] argTypes, Class<?> beanClass ) throws SQLException {
		return queryBean(sql, args, argTypes, beanClass, false );  
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
	public Map<String,Object> queryForMap(String sql ) throws SQLException {
		return queryForMap(sql, null, null);
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
	public Map<String,Object> queryForMap(String sql, Object[] args, int[] argTypes) throws SQLException {
		return (Map<String,Object>) query(sql, args, argTypes, new MapResultSetExtractor(false) );  
	}
	public static DataSourceManager getDataSourceManager() {
		return DbBean.dataSourceManager;
	}
	public static void setDataSourceManager(DataSourceManager dataSourceManager) {
		DbBean.dataSourceManager = dataSourceManager;
	}  

}
