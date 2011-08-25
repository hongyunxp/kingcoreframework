package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

import com.sun.rowset.CachedRowSetImpl;

/**
 * <p>缺省的 DatabaseManager 实现，可以被继承。</p>
 * @author Zeven on 2007-6-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class PlainDatabase implements DatabaseManager {

	/**
	 * 本类的日志对象。
	 */
	protected final static Logger log = Logger.getLogger( PlainDatabase.class);


	
	/**
	 *  <p>提供数据库通用的获取序列值方法.
	 *  	要求数据库提供了 getSequenceValue( tblName )的函数。
	 *  	对于采用表记录序列的方式，要求单独提交，所以独立获取conn,即使传入了也不使用conn，做到万无一失。
	 *  	根据表名获取这个表当前序列号，这就要求建立一个表，同时使用getSequenceValue 函数获取值，表结构如下：
	 *  <pre>
	 *  -- create table, use MySQL as example.
	 *  use mysql ;
	 *  drop table Tsys_Sequence ;
	 *  CREATE TABLE Tsys_Sequence (
  	 *  	Table_Name  varchar(60) NOT NULL,
  	 *  	Next_Value  bigint(20) not null,
  	 *  	PRIMARY KEY  (`Table_Name`)
	 *  );
	 *
	 * -- init
	 * delete from Tsys_Sequence where table_name='employee'; 
	 * insert into Tsys_Sequence (table_name,next_value) values('employee',1);
	 * 
	 * -- test
	 * select getSequenceValue('employee') ; 
	 * insert into employy(id, name) values( getSequenceValue('employee'), 'Mike') ;
	 * 
	 *  </pre>
	 *  	You can override this method in subclass.</p>
	 *  
	 * @param tblName 需要使用序列的表，唯一，不能为null
	 * @param conn 数据库连接对象，可以为null，实际上这里没有使用这个参数，而是单独创建Connection对象
	 * @return 当前可用序列值
	 */
	
	public long getIdentityValue(String tblName, Connection p_conn ) throws SQLException {
		
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
				return ti;
				
			}else {
				throw new SQLException("cann't get Sequence Value. sql statement is : " + sql);
			} 

        } catch (SQLException e) {
    		if( conn!=null ){
                conn.rollback();
    		}
        	//this.rethrow(e, sql, params);
        	log.error("Result in Qurey Exception'SQL is:\n" + sql);
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	DbUtils.closeQuietly(conn, pstmt, rs);
        }
        
	}

	
	/**
	 * @deprecated  建议使用 getIdentityValue 替换本方法。
	public int getSequenceValue(String tblName, Connection conn) throws SQLException {
		return new Long( getIdentityValue( tblName, conn )).intValue(); // 暂时处理
		
	}
	 */
	
	/**
	 * <p>根据当前查询的SQL 语句，生成一个获取其中部分数据行的SQL语句，一般在分页查询中使用。</p>
	 * 
	 * @param sql 源SQL语句
	 * @param offset 获取行段的起始位置，从0开始
	 * @param row_count 需要获取的行数
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 *
	 * <p>采用Sun的实现做为缺省的处理。 </p>
	 * @see com.kingcore.framework.context.DatabaseManager#resultSet2RowSet(java.sql.ResultSet)
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {

		// 采用Sun的实现针对 Access2000、MySQL5 等 数据库
		CachedRowSetImpl crs= new CachedRowSetImpl();
		crs.populate( rs );	
		
		return crs ;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateBlobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateBlobColumn(String tablename, String picField,
			String sqlWhere, String strPath, Connection conn) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateBlobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateBlobColumn(String tablename, String picField,
			String sqlWhere, String strPath) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#updateClobColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */

	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception{

		///Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		
		try{
			//开始对数据库操作；
			conn = DbUtils.getConnection();
			//conn.setAutoCommit(false);  //conn为Connection对象

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=? "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.setString( 1, content );
			pstmt.executeUpdate();

			conn.commit();
			return true;
			
		}catch (Exception e){
			conn.rollback();
			log.error("SQL=" + str_sql + "\ncontent="+content );
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			DbUtils.closeQuietly(conn, pstmt, rs);
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
	 * @see com.kingcore.framework.context.DatabaseManager#getClobColumn(java.lang.String)
	 */
	public String getClobColumn(String sql) throws Exception{

		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        ///StringBuffer sb = new StringBuffer();
		
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


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PlainDatabase pdb = new PlainDatabase();
		System.out.println("" + pdb.char2Date("2007-01-01"));
		System.out.println("" + pdb.date2Char("beginTime"));
		System.out.println("" + pdb.char2Datetime("2007-01-01 12:15:10"));
		System.out.println("" + pdb.datetime2Char("beginTime"));


	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#date2Char(java.lang.String)
	 */
	public String date2Char(String colName) {
		return colName;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#char2Date(java.lang.String)
	 */
	public String char2Date(String colValue) {
		return "'" + colValue + "'" ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#datetime2Char(java.lang.String)
	 */
	public String datetime2Char(String colName) {
		return colName;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#char2Datetime(java.lang.String)
	 */
	public String char2Datetime(String colValue) {
		return "'" + colValue + "'" ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#sysDatetime()
	 */
	public String sysDatetime() {
		return "now()";   // sysdate();
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#identity(java.lang.String)
	 */
	public String identity(String tblName) {
		return "getSequenceValue('"+tblName+"')";
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#concat()
	 */
	public String concat(String str1, String str2) {
		return "concat("+ str1 + "," + str2 +")";
	}
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */

	public String switchNull(String exp1, String exp2) {
		return "isNull(" +exp1+ "," +exp2+ ")";
	}

	/**
	 * 如果需要使用，本方法需要在子类中单独实现。
	 */
	public int getLastInsertIdentity( Connection conn  ) throws SQLException {
		return 0;
	}


	/**
	 * 根据当前的数据库的特殊规则对需要插入的内容采用正则表达式做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * 		-- 针对 MySQL数据库，"'","\"两个都是转义字符，都要变为"\'","\\"
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''c\\d")			="ab''''c\\\\d"
	 * </pre>
	 * @param src 需要保存到数据库的一个字段。
	 * @return
	 */
	public String escape2Sql(String src) {
		if(src==null) {
			return null;
		}
		
		// 可以参考 return wzw.lang.Escaper.escape2Sql( src );		
		return StringUtils.replace(src, "'", "''");
	}

}

