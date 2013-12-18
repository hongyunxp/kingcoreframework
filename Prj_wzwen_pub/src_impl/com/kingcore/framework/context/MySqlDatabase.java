package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

/**
 * <p> MySQL 数据库jdbc 个性操作的实现类。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MySqlDatabase extends PlainDatabase {
	
	protected final static Logger log = Logger.getLogger( MySqlDatabase.class);


	/**
	 *  根据需要查询的某段数据行，包装当前的sql statement and return.
	 * @param sql 原sql statement
	 * @param offset 起始位置，第一行为[1]
	 * @param row_count 需要获取的行数
	 * @return 包装之后的 sql statement
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count) {

		if(sql==null) {
			return null;
		}
		sql += " LIMIT "+ (offset-1) +"," + row_count ; 
		
		log.debug( sql );
		return sql ;
	}


	/**
	 * 将文件保存到数据库的表中。
	 * @param Tablename 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return
	 */	
	public  boolean  updateBlobColumn(String Tablename,
								String picField,
								String sqlWhere,
								String strPath) throws Exception{

		return true ;
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
	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception{

		/// Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		boolean isConnCreated = false;
		
		try{
			//开始对数据库操作；
			if(conn==null){
				conn = DbUtils.getConnection();
				isConnCreated = true;
			}
			//conn.setAutoCommit(false);  //conn为Connection对象

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=? "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.setString( 1, content );
			pstmt.executeUpdate();
			
			if(isConnCreated){	//自己创建的连接就提交，否则不处理事务问题
				conn.commit();
			}
			return true;
			
		}catch (Exception e){
			if(isConnCreated){	//自己创建的连接就回滚，否则不处理事务问题
				conn.rollback();
			}
			
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			log.info("SQL=" + str_sql + "\ncontent="+content );
			throw e;
			
		}finally{
			if(isConnCreated){
				DbUtils.closeQuietly(conn, pstmt, rs);
			}else{
				DbUtils.closeQuietly(null, pstmt, rs);
				
			}
		}
	}


	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content) throws Exception {
		return updateClobColumn(tableName, 
					  picField, 
   					  sqlWhere,
   					  content,
   					  null);
	}
	
	/* (non-Javadoc)
	 * 独立的事务。
	 * @see com.kingcore.framework.context.DatabaseManager#getClobColumn(java.lang.String)
	 */
	public String getClobColumn(String sql) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        //StringBuffer sb = new StringBuffer();
		
		try{
			//开始对数据库操作；
			conn = DbUtils.getConnection();
			conn.setAutoCommit(false);  //conn为Connection对象

			pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery(sql);
			if (rs.next())
			{
				//weblogic.jdbc.vendor.oracle.OracleThinClob clob = (weblogic.jdbc.vendor.oracle.OracleThinClob)rs.getClob( 1 );
				//Clob clb = rs.getClob( 1 );
				return rs.getString( 1 );
			}
			return "";

		}catch (Exception e){
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			DbUtils.closeQuietly(conn, pstmt, rs);			
		}
		
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#sysDatetime()
	 */
	public String sysDatetime() {
		return "now()";   // sysdate();
	}
	

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */
	public String switchNull(String exp1, String exp2) {
		return "coalesce(" +exp1+ "," +exp2+ ")";
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#date2Char(java.lang.String)
	 */
	public String date2Char(String colName) {
		return "DATE_FORMAT("+colName+",'%Y-%m-%d')";
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#datetime2Char(java.lang.String)
	 */
	public String datetime2Char(String colName) {
		return "DATE_FORMAT("+colName+",'%Y-%m-%d %H:%i:%s')";
	}


	/**
	 * just for MySQL,get id that insert last.
	 * 
     * @param conn 数据库连接对象，不能为null。
	 * @return last insert id
	 */
	public int getLastInsertIdentity(Connection conn) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql=null;
		try {
			sql = "select last_insert_id()";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			int id = rs.getInt(1);
			return id;

		} catch (SQLException sqle) {
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
			log.info("Result in getLastInsertIdentity Exception'SQL is:\n" + sql);
        	throw sqle;
		} finally {
			DbUtils.closeQuietly(null, pstmt, rs);
		}
		
	}
	

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
//	public String convertString(String src) {
//		if(src==null) {
//			return null;
//		}
//		return StringUtils.replace( StringUtils.replace(src, "'", "''"), "\\","\\\\");
//	}

	public String escape2Sql(String src) {
		if(src==null) {
			return null;
		}
		return StringUtils.replace( StringUtils.replace(src, "'", "''"), "\\","\\\\");
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#identity(java.lang.String)
	 * Zeven on 2009-01-17: MySql做了特殊处理，为了处理并发问题，本函数实际返回的是一个获取的数据库值，而不是Sql片段。
	 *					 将tsys_sequence表设置为 MYISAM类型的方式取代了。
	 * 
	 */
	public String identity(String tblName) {
		
		Connection conn = null ;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	String sql = null;
    	
        try {
        	// 自己获取一个连接，而不管连接参数是否存在
        	conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
        	
        	sql = "Select getSequenceValue('"+tblName.trim()+"')";
        	log.debug(sql);
        	
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			conn.commit() ;				// 提交避免锁定表。

			if( rs.next() ) {
	        	int ti = rs.getInt(1);
				return String.valueOf(ti);	//Zeven on 2009-01-17
				
			}else {
				throw new SQLException("cann't get Sequence Value. sql statement is : " + sql);
			} 
			
        } catch (SQLException e) {
    		if( conn!=null ){
                try {
					conn.rollback();
				} catch (SQLException e1) {
		        	log.error("rollback exception'SQL is:\n" + sql);
				}
    		}
        	log.error("Result in Qurey Exception'SQL is:\n" + sql);
			log.debug("debug", e);
        	/// e.pri ntStackTrace();

        } finally {
            DbUtils.closeQuietly(conn, pstmt, rs);
            
        }		
        return null;
        
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println( StringUtils.replace( "abc'fd\\fds", "\\","\\\\") );
		MySqlDatabase mysql = new MySqlDatabase();
		String a = "a\\";
		System.out.println(" --- " + mysql.escape2Sql( a ));
		System.out.println(" --- " + mysql.escape2Sql( "abc'fd\\fds" ));
		System.out.println(" -- " + mysql.escape2Sql( "abc'fd\\f'''\\'\\''\\ds" ));

	}
	
}
