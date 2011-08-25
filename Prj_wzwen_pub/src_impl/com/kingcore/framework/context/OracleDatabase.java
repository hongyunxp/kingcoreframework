package com.kingcore.framework.context;

 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.RowSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wzw.util.DbUtils;

/**
 * <p>Oracle 数据库jdbc特殊操作的实现类。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class OracleDatabase implements DatabaseManager {

	/**
	 * 日志对象 final 。
	 */
	protected static Logger log = Logger.getLogger( OracleDatabase.class);


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OracleDatabase odb = new OracleDatabase();
		System.out.println("" + odb.char2Date("2007-01-01"));
		System.out.println("" + odb.date2Char("beginTime"));
		System.out.println("" + odb.char2Datetime("2007-01-01 12:15:10"));
		System.out.println("" + odb.datetime2Char("beginTime"));

	}


    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 
     * @deprecated 保留该方法是为了向下兼容，建议统一使用 getIdentityValue 方法。
     * @param conn 数据库连接对象。
     * @param seqName 序列对象名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
	public int getSequenceValue( String seqName, Connection conn ) throws SQLException {

		/// return ApplicationContext.getDatabaseManager().updateBlobColumn(tablename,
		
    	/// Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	int nextVal=0;

        try {
        	/// conn = getConnection();
            pstmt = conn.prepareStatement( "SELECT "+seqName+".Nextval FROM DUAL");
            rs = pstmt.executeQuery();
            if(rs.next()){
            	nextVal = rs.getInt(1);
            }

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.error("Result in get Sequence Exception'SQL is:\nSELECT "+seqName+".Nextval FROM DUAL");
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	try{
        		if(rs!=null)
        			rs.close();
        		if(pstmt!=null)
        			pstmt.close();
//        		if(conn!=null)
//        			conn.close();
            }catch(SQLException e)
            {
	            log.fatal("在执行 getSequenceValue()出错，错误信息为：\n", e);
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
            }
        }        
    	return nextVal;
	}
     */
	
	
    /**
     * 获取Oracle数据库指定序列的下一个序列值，for Oracle Only。
     * 	 对于Oracle如果是使用序列发生器来得到id,可以不提交，因此可以共用conn。
     * 
     * @param conn 数据库连接对象。
     * @param seqName 序列对象名称
     * @return 序列的下一个值
     * @throws SQLException 数据库操作失败异常。
     */
	public long getIdentityValue( String tblName, Connection conn ) throws SQLException {
	   	
		
		/// return ApplicationContext.getDatabaseManager().updateBlobColumn(tablename,

		log.debug("---------------------wzw--2");
    	/// Connection conn = null;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	long nextVal=0;
    	boolean isConnCreated = false;		// 获取的地方一定同事 提交/回滚，关闭。
    	log.debug("开始执行 getIdentityValue");
        try {
        	if( conn==null || conn.isClosed() ){
        		conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
        		isConnCreated = true ;
            	log.debug("获取了新的连接");
        	}else{
        		log.debug("使用现有的连接");
        	}
        	
        	/// conn = getConnection();
            pstmt = conn.prepareStatement( "SELECT SEQ_"+tblName+".Nextval FROM DUAL");
    		log.debug("开始执行查询");
            rs = pstmt.executeQuery();
    		log.debug("查询执行完成");
            if(rs.next()){
            	nextVal = rs.getLong(1);
            }

        } catch (SQLException e) {
            //this.rethrow(e, sql, params);
        	log.error("Result in get Sequence Exception'SQL is:\nSELECT SEQ"+tblName+".Nextval FROM DUAL", e);
			/// log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	throw e;

        } finally {
        	log.debug("开始关闭对象");
    		if( isConnCreated ){		//创建了就关闭
    			wzw.util.DbUtils.closeQuietly(conn, pstmt, rs);
    		}else{
    			wzw.util.DbUtils.closeQuietly(null, pstmt, rs);
    			
    		}
            
        }        
    	return nextVal;
	}

	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#resultSet2RowSet(java.sql.ResultSet)
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {

		// 采用Oracle的实现针对 Oracle10 数据库
		oracle.jdbc.rowset.OracleCachedRowSet crs= new oracle.jdbc.rowset.OracleCachedRowSet();
		
		// 下面是返回
		crs.populate( rs );
		
		//log.debug(" ----------------- ok ");
		//ResultSetWrap crs2 = new ResultSetWrap(rs);
		//crs2.
		
		return crs ;
	}


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
		sql =
			"Select t.* " +
			"  From (select Rownum As rowseq,t.* From ( "+sql+") t ) t " +
			" WHERE rowseq BETWEEN "+ offset +" AND "+ ( offset + row_count -1 );
		
		log.debug( sql );
		return sql ;
	}
	


	/**
	 * 将指定的文件内容更新到Oracle数据库的BLOB字段。
	 * @param tableName 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return 成功返回true，失败抛出异常
	 * @exception 如果产生操作异常，向外部抛出
	 */	
	public boolean updateBlobColumn(String tableName, 
								   String picField, 
								   String sqlWhere, 
								   String strPath,
								   Connection conn ) throws Exception {
		
		oracle.sql.BLOB blb=null;

		boolean isConnCreated = false;
		
		try{
			//开始对数据库操作；
			if(conn==null || conn.isClosed()){
				conn = DbUtils.getConnection();
				isConnCreated = true ;				// 如果是自己获取的连接，就自己处理 获取、提交/回滚、关闭 所有事务之类的操作，否则不处理提交、回滚、关闭。
			}
			
			conn.setAutoCommit(false) ;
			String str_sql="SELECT " + picField + " FROM " +   tableName +" "+ sqlWhere + " FOR UPDATE";
			PreparedStatement pstmt = conn.prepareStatement(str_sql);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) blb=(oracle.sql.BLOB)rset.getBlob(picField) ;
			//log.debug(blb.toString());
			String fileName = strPath;
			java.io.File f = new java.io.File(fileName);
				
			try{
				java.io.FileInputStream fin ;
				fin = new java.io.FileInputStream(f);     
				
				//log.info("file size = " + fin.available());
				pstmt = conn.prepareStatement("update " + tableName  + " set " + picField + "=? "  +  sqlWhere);
				
				OutputStream out = blb.getBinaryOutputStream();
				/// int count = -1, total = 0;
				byte[] data = new byte[(int)fin.available()];
				fin.read(data);
				out.write(data);
				fin.close();
				out.close();
				pstmt.setBlob(1,blb);
				pstmt.executeUpdate();
				pstmt.close();
				
				if(isConnCreated){		// 自己创建的才提交
					conn.commit();
				}
				return   true ;
      
			}catch(java.io.FileNotFoundException e){
				if(isConnCreated){		// 自己创建的才回滚
					conn.rollback();
				}
				log.debug("debug", e);
				/// e.pri ntStackTrace();
				throw new Exception ("上传的文件没找到，可能是文件没有上传到服务器上，导致没有数据写入数据库" + e.getMessage());
				
			}finally{
				if(isConnCreated && conn!=null){	// 自己创建的才关闭
					conn.close();      	
				}
				
			}
		}catch(Exception ex){
        	log.error( "Exception:"+ex.getMessage() );
			log.debug("debug", ex);
			/// ex.pri ntStackTrace();
			throw ex;
		}
	}

	/**
	 * 将指定的文件内容更新到Oracle数据库的BLOB字段。
	 * @param tableName 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param strPath 要放入数据库的文件的全路径，如 "D:/upload/a.txe","D:\\upload\\a.txt"
	 * @return 成功返回true，失败抛出异常
	 * @exception 如果产生操作异常，向外部抛出
	 */	
	public boolean updateBlobColumn(String tableName, 
								   String picField, 
								   String sqlWhere, 
								   String strPath) throws Exception {
		return updateBlobColumn( tableName, 
				     picField, 
				     sqlWhere, 
				     strPath,
				     null );
	}


	/**
	 * 将字符串内容更新到Oracle数据库的CLOB字段。
	 *   CLOB(Character   Large   Object)   
	 *     用于存储对应于数据库定义的字符集的字符数据。（类似于long类型）
	 *        
	 * @param tableName 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param content 文本内容
	 * @param conn 数据库连接对象的引用
	 * @return 成功返回true，失败抛出异常
	 * @exception 如果产生操作异常，向外部抛出
	 */
	public boolean updateClobColumn(String tableName, 
			   					String picField, 
			   					String sqlWhere,
			   					String content,
			   					Connection conn) throws Exception {

		/// Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		String str_sql=null;
		//oracle.sql.CLOB clob=null;
		boolean isConnCreated = false;
		
		try{
			//开始对数据库操作；
			if(conn==null || conn.isClosed()){
				conn = DbUtils.getConnection();
				isConnCreated = true ;
			}
			
			conn.setAutoCommit(false);  //conn为Connection对象

			//set clob column to empty first.
			str_sql="UPDATE " + tableName + " SET " +   picField +"=empty_clob() "+ sqlWhere;
			pstmt = conn.prepareStatement( str_sql );
			pstmt.executeUpdate();
			
			//get clob column for update.
			str_sql="SELECT " + picField + " FROM " +   tableName +" "+ sqlWhere + " FOR UPDATE";
			
			pstmt = conn.prepareStatement( str_sql );
			rs = pstmt.executeQuery();
			BufferedWriter out = null;
			BufferedReader in = null;
			if(rs.next()){ 
				//oracle.sql.CLOB clob = (oracle.sql.CLOB)rs.getClob( picField );

				Object dsp = ApplicationContext.getInstance().getDataSourceManager();
				Object clob = null;
				// 显示当前jdbc环境
				log.debug("I'm \n" + dsp.getClass() +"\n" );
//							+ conn.getClass() +"\n" 
//							+ rs.getClass() +"\n" 
//							+ rs.getClob(picField).getClass() );
//				if( dsp instanceof WebLogicContainer)  {
//					
//					clob = rs.getClob( picField );
//					// wzw:需要添加：/bea/weblogic/server/lib/weblogic.jar 包
//					log.info("need : /bea/weblogic/server/lib/weblogic.jar");
//					//out = new BufferedWriter( ((weblogic.jdbc.vendor.oracle.OracleThinClob)clob).getCharacterOutputStream());
//				
//				}else if( dsp instanceof TomcatContainer )  {
//					// wzw:需要添加：Tomcat/common/lib/naming-factory-dbcp.jar 包
//					org.apache.tomcat.dbcp.dbcp.DelegatingResultSet drs = (org.apache.tomcat.dbcp.dbcp.DelegatingResultSet)rs;
//					clob = ((oracle.jdbc.OracleResultSet)drs.getDelegate()).getCLOB( picField );
//					out = new BufferedWriter( ( (oracle.sql.CLOB)clob).getCharacterOutputStream() );
//				
//				}else 
//				{
					
					oracle.jdbc.driver.OracleResultSet ors =
						 (oracle.jdbc.driver.OracleResultSet)rs;
					clob = ors.getClob(picField);
					// wzw :如果这里报NullPointException，则有可能数据库的字段不是Clob类型，而是Varchar2类型，需检查表结构。
					out = new BufferedWriter( ( (oracle.sql.CLOB)clob).getCharacterOutputStream() );
					
//				}
				
				in = new BufferedReader(new StringReader(content));
				//log.debug( out.w );
				//log.debug( "content55="+content );
				
				//int c;
				
				//while ((c=in.read())!=-1) {
				//	log.debug( "c= "+c );
				//	out.write(c);
				//}
				
				//buffer to hold data to being written to the clob.
				char[] cBuffer = new char[512];	//((OracleThinClob)cl).getBufferSize()

				//Read data from file, write it to clob
				int iRead = 0;
				while( (iRead= in.read(cBuffer)) != -1 ) {
					out.write( cBuffer, 0, iRead); 
					
				}
				
				in.close();
				out.close();
			}
			
			// commit all.
			if( isConnCreated ){		// 自己创建的才提交
				conn.commit();
			}
			
			return true;
			
		}catch (Exception e){
			if( isConnCreated ){		// 自己创建的才回滚
				conn.rollback();
			}
			log.error("SQL=" + str_sql + "\ncontent="+content );
			log.error("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			if(isConnCreated){		//// 自己创建的才关闭
				DbUtils.closeQuietly(conn, pstmt, rs);
			}else{
				DbUtils.closeQuietly(null, pstmt, rs);
			}
		}
	}

	/**
	 * 将字符串内容更新到Oracle数据库的CLOB字段。
	 *   CLOB(Character   Large   Object)   
	 *     用于存储对应于数据库定义的字符集的字符数据。（类似于long类型）
	 *        
	 * @param tableName 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param content 文本内容
	 * @return 成功返回true，失败抛出异常
	 * @exception 如果产生操作异常，向外部抛出
	 */
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

	/**
	 * 将字符串内容更新到Oracle数据库的CLOB字段。
	 * 独立的事务。
	 * @param tableName 表名称
	 * @param picField 列名称
	 * @param sqlWhere sql的where 语句，如 "where id='123456'"
	 * @param content 文本内容
	 * @return 成功返回true，失败抛出异常
	 * @exception 如果产生操作异常，向外部抛出
	 */
	public String getClobColumn(String sql) throws Exception { 
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		//oracle.sql.CLOB clob=null;
        StringBuffer sb = new StringBuffer();
		
		try{
			//开始对数据库操作；
			conn = DbUtils.getConnection();
			conn.setAutoCommit(false);  //conn为Connection对象

			pstmt = conn.prepareStatement( sql );
			rs = pstmt.executeQuery(sql);
			if (rs.next())
			{
				//weblogic.jdbc.vendor.oracle.OracleThinClob clob = (weblogic.jdbc.vendor.oracle.OracleThinClob)rs.getClob( 1 );
				Clob clb = rs.getClob( 1 );
			
				if (clb != null){
					//Reader is = clob.getString();//getCharacterInputStream();
		 
					BufferedReader br = new BufferedReader( clb.getCharacterStream() );
					String s = br.readLine();
					while (s != null) {
						sb.append( s ).append("\n");
						s = br.readLine();
					}
					return sb.toString();	//return clob.getChars(f,t);
				}
			}
			return "";

		}catch (Exception e){
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw e;
			
		}finally{
			wzw.util.DbUtils.closeQuietly(conn, pstmt, rs);
		
		}

	}


	/**
	 * 
	 * @return to_char( Brithday,'yyyy-mm-dd')
	 */
	public String date2Char(String colName) {
		return "to_char("+colName+", 'yyyy-mm-dd')";
	}


	/**
	 * 
	 * @return to_date('2006-01-01','yyyy-mm-dd')
	 */
	public String char2Date(String colValue) {
		return "to_date('"+colValue+"' , 'yyyy-mm-dd')";
	}


	/**
	 * 
	 * @return to_char( BeginTime, 'yyyy-mm-dd hh24:mi:ss')
	 */
	public String datetime2Char(String colName) {
		return "to_char("+colName+", 'yyyy-mm-dd hh24:mi:ss')";
	}
	
	/**
	 * 
	 * @return to_date( '2006-01-01 12:15:30', 'yyyy-mm-dd hh24:mi:ss')
	 */
	public String char2Datetime(String colValue) {
		return "to_date('"+colValue+"' , 'yyyy-mm-dd hh24:mi:ss')";
	}


	/**
	 * 返回获取数据库系统时间的 sql 片断。
	 */
	public String sysDatetime() {
		return "sysdate";
	}
	

	/**
	 * 返回获取序列的 sql 片断。
	 */
	public String identity(String tblName) {
		return "SEQ_"+tblName+".nextVal";
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#concat()
	 */
	public String concat(String str1, String str2) {
		return str1 + "||" + str2 ;
	}


	/* (non-Javadoc)
	 * @see com.kingcore.framework.context.DatabaseManager#switchNull()
	 */
	public String switchNull(String exp1, String exp2) {
		return "nvl(" +exp1+ ", " +exp2+ ")";
	}


	/**
	 * Oracle 暂时不使用这种先插入后获取的方式，而是先获取后插入，因为Oracle 不提供自动增长列。
	 */
	public int getLastInsertIdentity( Connection conn  ) throws SQLException {
		throw new SQLException("Oracle 中不要使用本特性，请使用 getIdentityValue 方法。"); 
		// return -1;
	}


	/**
	 * 根据当前的数据库的特殊规则对需要插入的内容采用正则表达式做特殊处理。
	 * 		针对字符串类型(Varchar)，数字(Number)、日期(Date)和二进制(LOB)类型不用这个处理。
	 * 		-- 当前是针对 Oracle 数据库，将 ' 符号 替换为 '' ，才能插入到数据库中。
	 * <pre>
	 * escape2Sql("ab'cd")			="ab''cd"
	 * escape2Sql("ab'c'd")			="ab''c''d"
	 * escape2Sql("ab''cd")			="ab''''cd"
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

//
//	public void setCachedResultSet(RowSet rs) throws Exception {
//		OracleResultSetWrap crs= new OracleResultSetWrap();
//		
//		this.rs = crs;
//		//this.rs.
//	}
//
//
//	public RowSet getCachedResultSet() throws Exception {
//		// TODO Auto-generated method stub
//		//return this.rs.clo
//		
//		return (RowSet)BeanUtils.cloneBean( this.rs );
//		//return null;
//	}

}

