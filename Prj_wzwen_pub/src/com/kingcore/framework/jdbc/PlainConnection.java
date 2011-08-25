
package com.kingcore.framework.jdbc;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;

import com.kingcore.framework.bean.DbBean;
import com.kingcore.framework.context.DatabaseManager;


/**
 * <p>包装一个Connection类,主要封装是增加对象 dataSourceManager。</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Kingnor Soft</p>
 * @author	WUZEWEN 2005-1-18
 * @version	1.0
 * @see		java.sql.Connection#close()
 * @see		DbBean#freeConnection()
 * @see		HashMap
 * @since	JDK1.4
 */


public class PlainConnection implements Connection // abstract
{
	//private static Logger log = Logger.getLogger(PooledConnection.class);
	
	Connection con = null;
	//DBBean pool = null;
	///private boolean isClosed ;

	private DatabaseManager databaseManager = null;
	public PlainConnection(Connection conn, DatabaseManager database) {
		this.con = conn;
		this.databaseManager = database;
	}

	public Statement createStatement() throws SQLException
	{
		return con.createStatement();
	}

	public PreparedStatement prepareStatement (String sql) throws SQLException
	{
		return con.prepareStatement (sql);
	}

	public CallableStatement prepareCall (String sql) throws SQLException
	{
		return con.prepareCall (sql);
	}

	public String nativeSQL (String sql) throws SQLException
	{
		return con.nativeSQL (sql);
	}

	public void setAutoCommit (boolean b) throws SQLException
	{
		con.setAutoCommit (b);
	}

	public boolean getAutoCommit () throws SQLException
	{
		return con.getAutoCommit();
	}

	public void commit () throws SQLException
	{
		con.commit();
	}

	public void rollback() throws SQLException
	{
		con.rollback();
	}

    public void close() throws SQLException
    {
    	this.con.close();
    }

    /**
     * modify by Zeven on 2009-03-05
     */
    public boolean isClosed() throws SQLException
    {
    	return this.con.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException
    {
    	return con.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException
    {
    	con.setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException
    {
    	return con.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException
    {
    	con.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException
    {
    	return con.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException
    {
    	con.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException
    {
    	return con.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException
    {
    	return con.getWarnings();
    }

    public void clearWarnings() throws SQLException
    {
    	con.clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
      throws SQLException
    {
    	return con.createStatement (resultSetType, resultSetConcurrency);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency,int p3)
      throws SQLException
    {
    	return con.createStatement (resultSetType, resultSetConcurrency,p3);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
					int resultSetConcurrency)
       throws SQLException
   	{
   		return con.prepareStatement(sql, resultSetType, resultSetConcurrency);
   	}

    public PreparedStatement prepareStatement(String sql, String[] param)
       throws SQLException
   	{
   		return con.prepareStatement(sql, param);
   	}

    public PreparedStatement prepareStatement(String sql, int[] param)
       throws SQLException
   	{
   		return con.prepareStatement(sql, param);
   	}

    public PreparedStatement prepareStatement(String sql, int param)
       throws SQLException
   	{
   		return con.prepareStatement(sql, param);
   	}

    public PreparedStatement prepareStatement(String sql, int param,int param1,int param2)
       throws SQLException
   	{
   		return con.prepareStatement(sql, param,param1,param2);
   	}

    public CallableStatement prepareCall(String sql, int resultSetType,
				 int resultSetConcurrency,int d) throws SQLException
	{
		return con.prepareCall (sql, resultSetType, resultSetConcurrency,d);
	}

    public CallableStatement prepareCall(String sql, int resultSetType,
				 int resultSetConcurrency) throws SQLException
	{
		return con.prepareCall (sql, resultSetType, resultSetConcurrency);
	}

    public java.util.Map getTypeMap() throws SQLException
    {
    	return con.getTypeMap();
    }

    public void setTypeMap(java.util.Map map) throws SQLException
    {
    	con.setTypeMap (map);
    }

    public void cleanUpConnection() throws SQLException
    {
    	con.close();
    }
    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {
    	con.releaseSavepoint(savepoint);
    }
    public void rollback(Savepoint savepoint) throws SQLException
    {
    	con.rollback(savepoint);
    }
    public Savepoint setSavepoint() throws SQLException
    {
       return con.setSavepoint();
    }
   public Savepoint setSavepoint(String name) throws SQLException
   {
       return con.setSavepoint(name);
   }

	/**
	 * WZW 2005.03.20 添加如下方法
	 */
	public void setHoldability(int holdability)
                    throws SQLException
	{
		con.setHoldability(holdability) ;
	}
	/**
	 * WZW 2005.03.20 添加如下方法
	 */
	public int getHoldability()
                   throws SQLException{
		return con.getHoldability() ;
	}

	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}


}
