package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * <p>SQL Server 数据库的个性操作方法。</p>
 * @author Zeven on 2007-6-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class SqlServerDatabase extends PlainDatabase {


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */

	public String switchNull(String exp1, String exp2) {
		return "isNull(" +exp1+ "," +exp2+ ")";
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#concat()
	 */
	public String concat(String str1, String str2) {
		return str1 + "+" + str2 ;
	}

	
	/**
	 * 日志记录对象。
	 */
	protected final static Logger log = Logger.getLogger( SqlServerDatabase.class);

	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#getSubResultSetSql(java.lang.String, int, int)
	 */
	public String getSubResultSetSql(String sql, int offset, int row_count) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean updateClobColumn(String tablename, String picField,
			String sqlWhere, String content) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#getClobColumn(java.lang.String)
	 */
	public String getClobColumn(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * just for MSS,get id that insert last.
	 * 		select   @@identity
	 * 		select   SCOPE_IDENTITY(   )  
	 *   Zeven : 以后考虑变为long类型。
     * @param conn 数据库连接对象。
	 * @return last insert id
	 */
	public int getLastInsertIdentity(Connection conn) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql=null;
		try {
			sql = "select @@identity";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			int id = rs.getInt(1);
			return id;
		} catch (SQLException sqle) {
			log.error("Result in @@identity Exception'SQL is:\n" + sql);
			log.debug("debug", sqle);
			/// sqle.pri ntStackTrace();
        	throw sqle;
		} finally {
            try{
				if(rs!=null)
					rs.close() ;
				if(pstmt!=null)
					pstmt.close() ;

            }catch(SQLException e)
            {
                log.fatal("在执行getLastInsertIdentity 出错（Exception），错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
                //this.addErrors(new ActionError("error.database.deal"));
            }
		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
