package com.kingcore.framework.context;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import wzw.util.DbUtils;

import com.kingcore.framework.bean.impl.ResultSetWrapper;


/**
 * <p> MySQL 数据库jdbc 个性操作的实现类。</p>
 * @author Zeven on 2007-6-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MySqlDatabase2 extends MySqlDatabase {
	
	protected final static Logger log = Logger.getLogger( MySqlDatabase2.class);


	/**
	 *
	 * <p>采用Sun的实现做为缺省的处理。 </p>
	 * @see com.kingcore.framework.context.DatabaseManager#resultSet2RowSet(java.sql.ResultSet)
	 */
	public RowSet resultSet2RowSet(ResultSet rs) throws SQLException {

		ResultSetWrapper crs = new ResultSetWrapper(rs);
		
		return crs ;
	}

	
	/**
	 *  <p>提供数据库通用的获取序列值方法.
	 *  	要求数据库提供了 getSequenceValue( tblName )的函数。
	 *  	对于采用表记录序列的方式，要求单独提交，所以独立获取conn,即使传入了也不使用conn，做到万无一失。
	 *		2009-02-18:线程安全，避免多个连接connection同时取序列导致并发问题。
	 *  	根据表名获取这个表当前序列号，这就要求建立一个表，同时使用getSequenceValue 函数获取值，表结构如下：
	 *  <pre>
	 *  -- create table, use MySQL as example.
	 *  use mysql ;
	 *  drop table TSYS_SEQUENCE ;
	 *  CREATE TABLE TSYS_SEQUENCE (
  	 *  	Table_Name  varchar(60) NOT NULL,
  	 *  	Next_Value  bigint(20) not null,
  	 *  	PRIMARY KEY  (`Table_Name`)
	 *  );
	 *
	 * -- init
	 * delete from TSYS_SEQUENCE where table_name='employee'; 
	 * insert into TSYS_SEQUENCE (table_name,next_value) values('employee',1);
	 * 
	 * -- test
	 * select getSequenceValue('employee') ; 
	 * insert into employy(id, name) values( getSequenceValue('employee'), 'Mike') ;
	 * 
	 *  </pre>
	 *  	You can override this method in subclass.</p>
	 *  
	 *  ********************************************* 采用自动增长列
	 *  drop table t3;
	 *  create   table   t3(Id   int   auto_increment   primary   key   not   null) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	 *  
	 *  set autocommit=0;

	 *  insert into t3 values();
	 *  select last_insert_id();
	 *  ********************************************* 采用自动增长列 end
	 *  
	 * @param tblName 需要使用序列的表，唯一，不能为null
	 * @param conn 数据库连接对象，可以为null，实际上这里没有使用这个参数，而是单独创建Connection对象
	 * @return 当前可用序列值
	 */
	
	public synchronized long getIdentityValue(String tblName, Connection pm_conn ) throws SQLException {
		
		Connection conn = null ;
    	Statement stmt = null;
    	Statement stmt2 = null;
        ResultSet rs = null;
    	String sql = null;
    	StringBuffer sb = new StringBuffer();
        try {
        	sb.append("1:conn is null.");
        	// 自己获取一个连接，而不管连接参数是否存在
        	conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();

        	sb.append("2:get conn.");
        	sb.append(conn.isClosed());
        	
        	String sql_query = "select t.next_value from TSYS_SEQUENCE t where t.table_name='"+tblName.trim()+"' for update";
        	log.debug(sql_query);
        	
        	while(true){

            	sb.append("3:get conn." );
            	sb.append(conn.isClosed());

            	stmt = conn.createStatement();
            	stmt2 = conn.createStatement();
            	
            	sb.append("4:get conn." );
            	sb.append(conn.isClosed());
	        	rs = stmt.executeQuery(sql_query);

	        	sb.append("5:get conn." );
	        	sb.append(conn.isClosed());
	        	
				if( rs.next() ) {
		        	long ti = rs.getLong(1);
		        	sql = "update TSYS_SEQUENCE t set t.next_value = t.next_value+1 where t.table_name='"+tblName.trim()+"' and t.next_value="+ti;

		        	sb.append("6:get conn." );
		        	sb.append(conn.isClosed());
		        	
		        	if(stmt2.executeUpdate(sql)==1){
			        	sb.append("6_1:get conn." );
			        	sb.append(conn.isClosed());
			        	
			        	conn.commit();			// 这里提交
			        	return ti;
		        	
		        	}else{
			        	sb.append("7:get conn." );
			        	sb.append(conn.isClosed());
			        	
			        	conn.rollback();		// 这里回滚

			        	sb.append("8:get conn." );
			        	sb.append(conn.isClosed());
			        	
			        	continue;		        		
		        	}
		        	
		        	
//		        	else{// 否则继续循环
//		        		try {
//							Thread.sleep(50);    //-- 导致连接关闭？？
//						} catch (InterruptedException e) {
//							//e.printStackTrace();
//							log.error( e.getMessage(), e);
//						}
//		        		continue;
//		        	}
		        	
				}else {
					throw new SQLException("cann't get Sequence Value. sql statement is : " + sql);
				} 
        	}
        	
        } catch (SQLException e) {

			log.error("error--"+sb.toString(), e);
			
    		if( conn!=null ){
                conn.rollback();
    		}
        	//log.error("Result in Qurey Exception'SQL is:\n" + e.getMessage());
        	throw e;

        } finally {
        	DbUtils.closeQuietly(conn, stmt, rs);
        }
        
	}

	/**
	 * 线程安全，避免多个连接connection同时取序列导致并发问题。
	 */
	public synchronized String identity(String tblName) {
		try {
			//return String.valueOf( this.getIdentityValue(tblName, null) );
			return this.getIdentityValue(tblName, null)+"";
			
		} catch (SQLException e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return null;
	}

}
