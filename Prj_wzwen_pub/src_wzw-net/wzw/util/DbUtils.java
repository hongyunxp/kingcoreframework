/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;



import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.kingcore.framework.context.ApplicationContext;
import com.kingcore.framework.context.DatabaseManager;
import com.kingcore.framework.jdbc.PlainConnection;

/**
 * <p>
 *    > DbUtils.java 中所有数据库操作的方法都不支持复杂事务，都是单事务的。
 *   数据库工具类，提供获取数据库连接等功能，方法基本上都是静态的(static)，
 *		不需要创建类实例就可以调用方法。通用的数据库工具，不区分数据库类型。
 *  如果所有的数据库操作基本方法都源自于这里，那么就可以完全跨容器、跨数据库。
 *    映射与协作：
 *    	org.apache.commons.dbutils.DbUtils
 *    				.closeQuietly ;
 *    	org.apache.commons.dbutils.BeanProcessor
 *    				.toBean ;
 *    				.toBeanList ;
 *  
 *  	org.apache.commons.dbutils.handlers Classes  
 *  		ArrayHandler 
 *  		ArrayListHandler 
 *  		BeanHandler 
 *  		BeanListHandler 
 *  		ColumnListHandler 
 *  		KeyedHandler 
 *  		MapHandler 
 *  		MapListHandler 
 *  		ScalarHandler 
 *  	
 *  
 *   DBUtils 的方法来自两方面：
 *   	1，所有数据库通用的jdbc操作由DBUtils自己实现； 
 *   	2，所有数据库特有的操作，由 ApplicationContext.getInstance().getDatabaseManager() 对象提供；
 *  
 *	  数据库工具与以下内容相关：
 *		1。数据源提供管理者；
 *		2。数据库管理者类型；
 *	   这些相关的因数都采用注入的方式，减少依赖性。</p>
 *
 * @author	WUZEWEN on 2004-09-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 * 
 **/

public class DbUtils {

	
//    /**
//     * 缺省的DataSource 的JNDI的名称。
//     */
//    //public static String JNDI_DATASOURCE="jndi/jdbc";

    /**
     * log4j日志对象。
     */
    protected static Logger log=Logger.getLogger(wzw.util.DbUtils.class);

    /**
	 *	构造函数。
	 */
	public DbUtils() {
	}

	/**
     * <p>获取数据库连接，使用当前缺省的名字 。
     * <UL>
     *    <LI>从jndi中获取的DataSource再获得Connection对象。
     *    <LI>单一工厂模式，静态类来获取DataSource再获得Connection对象。
     *    <LI>从scope=application中获取的DataSource再获得Connection对象。
     * </UL></p>
     * @author WUZEWEN on 2005-07-24
     * @return conn 一个实现了Connection接口的对象
     * @exception SQLException
     */
	public static Connection getConnection()
				throws SQLException {
		//log.debug("----------getConnection DBUtils 101."+ApplicationContext.getInstance().getDataSourceProvider().getClass().toString());
    	log.debug( "--0--2");
		return ApplicationContext.getInstance().getDataSourceManager().getConnection();
	 
	}
	
	/**
     * <p>根据指定的名字，获取数据库连接，支持连接多个数据源。
     * 		该名称可以是容器提供的 DataSource 的jndi 名称，也可以是 ConnectionPool 的名称。
     * <UL>
     *    <LI>从jndi中获取的DataSource再获得Connection对象。
     *    <LI>单一工厂模式，静态类来获取DataSource再获得Connection对象。
     *    <LI>从scope=application中获取的DataSource再获得Connection对象。
     * </UL></p>
     * 
	 * @param sourceName 源的DataSource 的jndi 名称，或者是 ConnectionPool 的名称
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String dataSourceName)
				throws SQLException {

		return ApplicationContext.getInstance().getDataSourceManager().getConnection(dataSourceName);
	}


	
    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
	
    public static RowSet executeQuery(String sql)
     throws SQLException {

    	Connection conn = null ;
    	
        try {

        	log.debug(sql+"--0--1");
            conn = getConnection();		// 哪里获取，就在哪里提交/回滚、关闭。
            return executeQuery(sql, conn);

        } finally {
        	try{
        		if(conn!=null)
        			conn.close();
        		
            }catch(SQLException e)
            {
	            log.fatal("在执行DBUtils.doQuery出错，错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }          
        }
	}
    
    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for connection cleanup.
     *
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException
     */
	public static RowSet executeQuery(String sql, Connection conn)
      throws SQLException {

    	log.debug(sql+"--1"); 
    	//参数检查
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}

    	log.debug(sql+"--2");
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	RowSet crs= null;

        try {
        	log.debug(sql);

            /// conn = getConnection();
        	pstmt = conn.prepareStatement( sql);
            rs = pstmt.executeQuery();
            //crs.populate( rs ) ;
            crs = resultSet2RowSet(((PlainConnection)conn).getDatabaseManager(),  rs ) ;

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.debug("Result in Qurey Exception'SQL is:\n" + sql, e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	
        	DbUtils.closeQuietly(null, pstmt, rs);
        	
        }

        return crs;
	}
	
	
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters，并且本身提供事务处理,这个方法单独写，没有调用 doUpdate(List list).
     *
     * @param sql The SQL to execute
     * @return The number of rows updated
     * @throws SQLException 数据库操作异常 
     */  
    public static int executeUpdate( String sql ) throws SQLException {

        //return doUpdate(sql, null);
    	
    	Connection conn = null; 

        try {
        	conn = getConnection(); //获取、提交、回滚、关闭 都在一个地方统一控制；
        	int val = executeUpdate(sql, conn);
        	conn.commit();
            
        	return val;
            //log.debug("doUpdate commit success!");

        } catch(SQLException sqle){
        	DbUtils.rollbackQuietly(conn);
//        	conn.rollback();
        	throw sqle ;
        	
    	} finally {
            try{
                if(conn!=null)
                    conn.close() ;

            }catch(SQLException e)
            {
                log.fatal("在执行DBUtils.doUpdate() 出错（Exception），错误信息为：\n", e);
				/// log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
        }
        //return i_returns;    
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters，并且本身提供事务处理,这个方法单独写，没有调用 doUpdate(List list).
     *
     * @param conn The connection to use to run the query
     * @param sql The SQL to execute
     * @return The number of rows updated
     * @throws SQLException 数据库操作异常
     */  
    public static int executeUpdate( String sql ,Connection conn) throws SQLException {    	 

    	//参数检查
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}
    	
        PreparedStatement pstmt = null;
        int i_returns;
        try {
        	/////conn = getConnection();  //获取、提交、回滚、关闭 都在一个地方统一控制；
            pstmt = conn.prepareStatement( sql);
            i_returns = pstmt.executeUpdate();
            //log.debug("pstmt.executeUpdate success!");
            return i_returns;

        } catch (SQLException e) {
            log.fatal( "Result in update Exception'SQL is:\n"+sql + ". Message:" + e.getMessage(), e ) ;
			/// log.debug("debug", e);
            /// e.pri ntStackTrace();
            /// System.out.println("Result in update Exception'SQL is:\n"+sql );
            throw e;
            //this.rethrow(e, sql, list);

        } finally {
            try{
                if(pstmt!=null)
                    pstmt.close() ;
            }catch(SQLException e)
            {
                log.fatal("在执行DBUtils.doUpdate() 出错（Exception），错误信息为：\n", e);
				///log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
        }
        //return i_returns;        
    }
	

    /**
     * 执行批处理，并且本身管理事务问题.
     *
     * @param allsql 要执行的sql语句组成的数组。
     * @throws 执行批处理失败。
     * @return 每个sql语句影响的行数组成的数组。
     */
    public static int[] executeBatch( List<String> list )
      throws SQLException {

    	//return doBatch(list, null);
    	
    	Connection conn = null;
        try {
        	conn = getConnection();  //获取、提交、回滚、关闭 都在一个地方统一控制；
        	
            int ret[] = executeBatch(list, conn);	// 调用内部方法
            
            conn.commit();
            return ret;
        } catch(SQLException e){
        	conn.rollback();
        	throw e;
        	
        } finally {
            try{
                if(conn!=null)
                    conn.close() ;

            }catch(SQLException e)
            {
                log.fatal("在执行DBUtils.doBatch() 出错，错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }
        // return null;
    }

    /**
     * 执行批处理，并且本身管理事务问题.
     * @param conn 数据库连接对象。
     * @param allsql 要执行的sql语句组成的数组。
     * @throws 执行批处理失败。
     * @return 每个sql语句影响的行数组成的数组。
     */
    public static int[] executeBatch( List<String> list, Connection conn )
        throws SQLException {

    	//参数检查
    	if( conn==null || conn.isClosed()){	
    		throw new SQLException("Connection Object is null or is closed!");
    	}
    	
        Statement stmt = null;
        int returns[];
        boolean isConnCreated = false;
        try {
        	if( conn==null || conn.isClosed()){		// 不可能的情况，保留这种写法。
            	conn = getConnection();  //获取、提交、回滚、关闭 都在一个地方统一控制；
            	isConnCreated = true;
        	}
        	
        	stmt = conn.createStatement();
            addBatch( stmt, list);
            returns = stmt.executeBatch();
            
            if( isConnCreated ){
            	conn.commit() ;	 	//对于外部传入的连接，不提交，不回滚，不关闭
            }
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            if( isConnCreated ){
            	conn.rollback(); 	 //对于外部传入的连接，不提交，不回滚，不关闭
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
            try{
                if(stmt!=null)
                    stmt.close() ;
            }catch(SQLException e)
            {		
                log.fatal("在执行DBUtils.doBatch() 出错，错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
            
            try{
                if(isConnCreated && conn!=null)
                    conn.close() ; 	 //对于外部传入的连接，不提交，不回滚，不关闭

            }catch(SQLException e)
            {		
                log.fatal("在执行DBUtils.doBatch() 出错，错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }
        // return null;
    }

    
    /**
     * 根据表名称和查询条件，获取符合条件的数据的行数。
     *
     * @param tableName 要查询的表名称
     * @param condition 过滤条件，如“WHERE numCol>100”
     * @return 符合条件的行数
     * @throws SQLException 数据库操作异常
     */
	public static int getSize(String tableName, String condition) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return getSize(tableName, condition, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("在执行DBUtils.getSize() 出错（Exception），错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	
    /**
     * 根据表名称和查询条件，获取符合条件的数据的行数。
     *
     * @param conn 数据库连接对象。
     * @param tableName 要查询的表名称
     * @param condition 过滤条件，如“WHERE numCol>100”
     * @return 符合条件的行数
     * @throws SQLException 数据库操作异常
     */
	public static int getSize(String tableName, String condition, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		/// PreparedStatement pstmt = null;
		/// ResultSet rs = null;
		String sql=null;
		sql = "SELECT count(*) FROM "+tableName+" "+condition;
		return queryForInt(sql, conn);
	
	}
	

	/**
	 * 类似 Spring 的 queryForInt 方法，执行一个指定的语句并返回一个int数据。
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int queryForInt(String sql) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return queryForInt(sql, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("在执行DBUtils.queryForInt() 出错（Exception），错误信息为：\n", e);
				log.debug( e.getMessage(), e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	public static int queryForInt(String sql, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			/// conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}else {
				throw new SQLException("cann't queryForInt(), sql statement is : " + sql);
			}
			
		} catch (SQLException sqle) {
			log.info( "Result in getSize Exception'SQL is:\n" + sql );
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
        	throw sqle;
        	
		} finally {

        	DbUtils.closeQuietly(null, pstmt, rs);
		}
	}
	

	/**
	 * 类似 Spring 的 queryForLong 方法，执行一个指定的语句并返回一个long数据。
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static long queryForLong(String sql) throws SQLException {
		
		Connection conn = null;
		
		try {
			conn = getConnection();
			return queryForLong(sql, conn);

		} finally {
            try{
				if(conn!=null) {
					conn.close() ;
				}
            }catch(SQLException e)
            {		
                log.fatal("在执行DBUtils.queryForInt() 出错（Exception），错误信息为：\n", e);
				log.debug( e.getMessage(), e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
	}

	public static long queryForLong(String sql, Connection conn) throws SQLException {
		
		/// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			/// conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}else {
				throw new SQLException("cann't queryForLong(), sql statement is : " + sql);
			}
			
		} catch (SQLException sqle) {
			log.info( "Result in getSize Exception'SQL is:\n" + sql );
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
        	throw sqle;
        	
		} finally {

        	DbUtils.closeQuietly(null, pstmt, rs);
		}
	}
	
	
    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 
     * @param seqName 序列对象名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
     */
	public static int getSequenceValue( String seqName) throws SQLException {

    	Connection conn = null;
        try {
        	conn = getConnection();
        	return getSequenceValue(seqName, conn);

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.info("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	try{
        		if(conn!=null)
        			conn.close();
            }catch(SQLException e)
            {
	            log.fatal("在执行DBUtils.getSequenceValue()出错，错误信息为：\n", e);
	            log.debug(e.getMessage(), e);
            	/// e.pri ntStackTrace();
            }
        } 
	}
	

    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 
     * @param conn 数据库连接对象。
     * @param seqName 序列对象名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
     */
	public static int getSequenceValue( String seqName, Connection conn ) throws SQLException {
	   	
		return new Long(((PlainConnection)conn).getDatabaseManager().getIdentityValue( seqName, conn)).intValue() ; //暂时处理
    	//return new Long(ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( seqName, conn)).intValue(); //暂时处理
	}


    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 
     * @param tblName 需要使用id的表的名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
     */
	public static long getIdentityValue( String tblName) throws SQLException {

		return getIdentityValue(tblName, null);
//    	Connection conn = null;
//        try {
//        	conn = getConnection();
//        	conn.setAutoCommit( true );		// 自动提交函数处理
//        	return getIdentityValue(tblName, conn);
//
//        } catch (SQLException e) {
//            //this.rethrow(e, sql, params);
//        	e.pri ntStackTrace();
//        	/// System.out.println("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
//        	throw e;
//
//        } finally {
//        	try{
//        		if(conn!=null) {
//        			conn.setAutoCommit( false );
//        			conn.close();
//        		}
//            }catch(SQLException e)
//            {
//            	e.pri ntStackTrace();
//	            log.fatal("在执行DBUtils.getSequenceValue()出错，错误信息为：\n", e);
//            }
//        } 
	}
	

    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 
     * @param conn 数据库连接对象。
     * @param tblName 需要使用id的表的名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
     */
	public static long getIdentityValue( String tblName, Connection conn ) throws SQLException {
	   	
		return ((PlainConnection)conn).getDatabaseManager().getIdentityValue( tblName, conn);
    	//return ApplicationContext.getInstance().getDatabaseManager().getIdentityValue( tblName, conn);
	}
	
	/**
	 * 将一个List集合对象中的所有SQL语句装载到 Statement中。
	 * @param stmt 实现了Statement接口的对象
	 * @param list 实现了List接口的对象
	 * @throws SQLException
	 */
	public static void addBatch(Statement stmt, List<String> list) throws SQLException{
		if(list==null){
			return ;
		}
		for(int i=0;i<list.size();i++){
            stmt.addBatch(list.get(i));  //.toString()
        }
	}

	/**
	 * 根据当前的数据库的特殊规则对需要插入的内容采用正则表达式做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * <pre>
	 * DBUtils.escape2Sql("ab'cd")			="ab''cd"
	 * DBUtils.escape2Sql("ab'c'd")			="ab''c''d"
	 * DBUtils.escape2Sql("ab''cd")			="ab''''cd"
	 * </pre>
	 *   建议：本方法不再使用DBUtils调用，而是使用DAO, DBBean对象调用或者直接通过DatabaseManager对象调用。
	 * @param src 需要保存到数据库的一个字段。
	 * @return
	 */
	public static String escape2Sql(DatabaseManager databaseManager, String src ) {
		return databaseManager.escape2Sql(src);
		//return ApplicationContext.getInstance().getDatabaseManager().escape2Sql(src);
	}
	
	
	/**
	 * populate ResulteSet Object to RowSet Object, all implements below:  
	 *   Oracle 10i:  oracle.jdbc.rowset.OracleCachedRowSet
	 *	 MS SQL 2000: sun.jdbc.rowset.CachedRowSet
	 *	 Access 2000: sun.jdbc.rowset.CachedRowSet
	 *
	 *	  Zeven on 2007-06-06, this static method manager which RowSet's implement used by System.
	 *		It's safe for mutil thread case.
	 *		
	 *		根据当前系统设置的数据库类型构造 RowSet 对象；
	 *		支持多数据库的普通数据的 ResultSet to RowSet 处理。
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static RowSet resultSet2RowSet(DatabaseManager databaseManager, ResultSet rs ) throws SQLException {
		//ApplicationContext.getInstance().getDatabaseManager()
		//return resultSet2RowSet(rs, databaseManager );
		return databaseManager.resultSet2RowSet( rs );
	}
	
	/**
	 *  根据传入的数据库类型构造相应的 RowSet 对象，以解决程序中可能连接多个类型数据的问题，
	 *  	而对于Servlet Container 的 Web 服务就不可能有多个的情况。
	 * @deprecated 请使用 resultSet2RowSet(DatabaseManager databaseManager, ResultSet rs )
	 * @param rs 需要装入 RowSet 的 ResultSet 对象引用
	 * @param dbms_type 枚举值，参考 com.kingcore.framework.Constants.DBMS_Type_...
	 * @return
	 * @throws SQLException
	 * @see com.kingcore.framework.Constants.DBMS_Type_...
	 */
//	public static RowSet resultSet2RowSet(ResultSet rs, DatabaseManager dbm) throws SQLException {
//		return dbm.resultSet2RowSet( rs );
//	}
	public static RowSet resultSet2RowSet(ResultSet rs ) throws SQLException {
		DatabaseManager dbm = ApplicationContext.getInstance().getDatabaseManager();
		return dbm.resultSet2RowSet( rs );
	}

	/**
	 * 将二进制大对象保存到数据库的表中。
	 *   BLOB(Binary   Large   Object)   
	 *     可用来存储无结构的二进制数据。（类似于row和long   row）
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	public static boolean  updateBlobColumn(Connection conn,String tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{
		
		//ApplicationContext.getInstance().getDatabaseManager()
		return ((PlainConnection)conn).getDatabaseManager().updateBlobColumn(tablename,
																			picField,
																			sqlWhere,
																			strPath);
	}
	

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
	public static boolean updateClobColumn(Connection conn, String tablename, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception{
		//ApplicationContext.getInstance().getDatabaseManager()
		return ((PlainConnection)conn).getDatabaseManager().updateClobColumn( tablename, 
														   					picField, 
														   					sqlWhere,
														   					content);
	}

	/**
	 * 获取字符型大对象的内容。
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static String getClobColumn(DatabaseManager databaseManager, String sql) throws Exception{
		//ApplicationContext.getInstance().getDatabaseManager()
		return databaseManager.getClobColumn( sql );	
	}
	
	// begin ********************************************************************
	// like org.apache.commons.dbutils.DbUtils.class
	//   与 closeQuietly 的不同之处在于会向后台输出信息，同时也有log4j信息输出。
	// **************************************************************************
    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs)
    {
    	
    	closeQuietly(rs);
    	closeQuietly(stmt);
    	closeQuietly(conn);
    }

    public static void closeQuietly(ResultSet rs)
    {
        try
        {
        	if(rs != null)
	        {
	            rs.close();
	        }
        }
        catch(SQLException sqle) {
        	log.info("结果集rs关闭时产生异常，信息："+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }

    public static void closeQuietly(Statement stmt)
    {
        try
        {
        	if(stmt != null)
	        {
        		stmt.close();
	        }
        }
        catch(SQLException sqle) {
        	log.info("stmt关闭时产生异常，信息："+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }
    
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
     * Connection 回滚，不抛出异常。
     * @param conn
     */
    public static void rollbackQuietly(Connection conn)
    {
        try
        {
        	if(conn != null && !conn.isClosed())  // 避免重复关闭异常
	        {
        		conn.rollback();
	        }
        }
        catch(SQLException sqle) {
        	log.info("数据库连接conn回滚时产生异常，信息："+ sqle.getMessage(), sqle );
        }
    }

    /**
     * Connection 提交，不抛出异常。
     * @param conn
     */
    public static void commitQuietly(Connection conn)
    {
        try
        {
        	if(conn != null && !conn.isClosed())  // 避免重复关闭异常
	        {
        		conn.commit();
	        }
        }
        catch(SQLException sqle) {
        	log.info("数据库连接conn提交时产生异常，信息："+ sqle.getMessage(), sqle );
        }
    }
    
    
    /**
     * Connection 关闭，不抛出异常。
     * @param conn
     */
    public static void closeQuietly(Connection conn)
    {
        try
        {
        	log.debug(conn != null?"not null":"is null"); //
		
        	if(conn != null && !conn.isClosed())  // 避免重复关闭异常
	        {
        		log.debug("连接关闭时hashcode="+conn.hashCode()+""+"。调用路径："+showTrace(8) ); //
        		//log.debug(conn.isClosed()?"---a---conn isClosed":"conn not closed");
        		//log.debug(conn.getClass());
        		conn.close();
        		//log.debug(conn.isClosed()?"---b---conn isClosed":"conn not closed");
        		
	        }
        }
        catch(SQLException sqle) {
        	log.info("数据库连接conn关闭时产生异常，信息："+ sqle.getMessage(), sqle );
			/// log.debug("debug", sqle );
        	/// sqle.pri ntStackTrace();
        }
    }

	// end **********************************************************************
	// like org.apache.commons.dbutils.DbUtils.class
	// **************************************************************************
	

    /**
     * 获取数据库连接。
     * <UL>
     *    <LI>从scope=application中获取的DataSource再获得Connection对象。
     * </UL>
     * @author WUZEWEN on 2005-07-24
     * @deprecated 建议使用容器的 jndi 或者是 自定义的 ConnnectionPool 的获取数据库连接，不建议从application等域中获取。
     * @param  request:请求对象，从scope=application获取conn专用
     * @return Connection conn:一个实现了Connection接口的对象
     * @exception no exception 
     */
	public static Connection getConnection(javax.servlet.http.HttpServletRequest request )
							throws SQLException {
		//o1.get DataSource from scope=application
		DataSource datasource = null;
        Connection conn = null;

        String DATA_SOURCE_KEY_IN_STRUTS1 = "org.apache.struts.action.DATA_SOURCE";
		datasource = (DataSource)HttpUtils.getObjectInApplication( request, DATA_SOURCE_KEY_IN_STRUTS1 ) ;
		if(datasource!=null)	//在使用前一定要做null判断
			conn = datasource.getConnection();
		//if cann't find ds in application,get it in jndi...
		if(conn==null){
			conn = getConnection() ;   //这里不能用this.getConn
		}

		return conn;
	}

	// 使用名称载入是为了接口与实现分离。 同时这个是可以配置注入或者设置的。
	private static final String Database_Name_PlainDatabase  = "com.kingcore.framework.context.PlainDatabase";
	private static final String Database_Name_OracleDatabase = "com.kingcore.framework.context.OracleDatabase";
	private static final String Database_Name_MySqlDatabase  = "com.kingcore.framework.context.MySqlDatabase";
	
	public static String getDatabaseManagerNameByDriver(String url) {
		if(url==null)
			return null;
		url = url.toLowerCase();
		String className = null;
		if(url.indexOf("sun.jdbc.odbc")>-1){
			className = Database_Name_PlainDatabase;
		}else if(url.indexOf("oracle")>-1){
			className = Database_Name_OracleDatabase;
		}if(url.indexOf("mysql")>-1){
			className = Database_Name_MySqlDatabase;
		}
		return className;
	}

	public static void main(String[] args) throws URISyntaxException {
		java.net.URI u = new java.net.URI("");
		u.getQuery();
		System.out.println(  DbUtils.escape2Sql(null, "a'b'c") );
		
	}
}
