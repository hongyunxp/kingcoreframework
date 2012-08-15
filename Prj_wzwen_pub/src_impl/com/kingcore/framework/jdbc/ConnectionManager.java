/**
 * Copyright (C) 2005 ChangSha Sino-Town Science & Technology CO,.LTD. All rights reserved.
 * Sino-Town PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.jdbc;

/**
 * <p>数据连接池管理类。
 * 		Zeven，这个内部类要实现DataSource接口，但是本类可以不实现 DataSource 接口，
 * 		如果实现，好处是只需要操作本类，完全封装 DBConnectionPool 类。</p>
 * @author	WUZEWEN on 2006-3-7
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @since	JDK5
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import wzw.io.FileUtils;
import wzw.util.XmlUtils;

import com.kingcore.framework.context.DataSourceManager;


public class ConnectionManager implements DataSource, DataSourceManager{ //连接池类
	 
	private static final String Database_ConfigFile_ = "system-datasource-conf.xml";
	
	private static ConnectionManager instance;
	private static int clients;
	///private static String filename = "/db.properties";
	public static String errlog = "/err.log";
	private Map<String,ConnectionPool> pools = new Hashtable<String,ConnectionPool>();
	private Logger log = Logger.getLogger( this.getClass() ) ;	
	
	/**
	 * Zeven: use singleton design model,just like static object or method.
	 * @return
	 */
	public static synchronized ConnectionManager getInstance() {
		//log.debug("public static synchronized ConnectionManager getInstance()----------");
		if (instance == null) {
			instance = new ConnectionManager();
		}
		clients++;
		return instance;
	}
	
	/**
	 * *** Zeven ,可能会不被 spring 构建而调用本方法。
	 *
	 */
	private ConnectionManager() {
//		drivers = new Vector();
//		pools = new Hashtable();
//		init();
		
//		instance = this;
	}
	
//	/**
//	 * Zeven:
//	 * init method for singleton object create.
//	 * put the db.properties file on the root path of this class,
//	 * 		which is under "/Web-Inf/class" directory or in wpub.jar package.
//	 */
//	private void init() {
//		Properties dbProps = new Properties();
//		try {
//			//InputStream is = getClass().getResourceAsStream(filename);
//			InputStream is = ConnectionManager.class.getResourceAsStream("/db.properties");
//			dbProps.load(is);
//		}
//		catch (Exception e) {
//			log.error( "不能读取属性文件. 请确保db.properties在指定的路径中" );
//			
//			return;
//		}
//		// String logFile = dbProps.getProperty("logfile", "ConnectionManager.log"); // by wzw
//		// errlog = dbProps.getProperty("errlog", "err.log"); //by wzw
////		try {
////			log = new PrintWriter(new FileWriter(logFile, true), true);
////		}
////		catch (IOException e) {
////			System.err.println("无法打开日志文件: ".concat(String.valueOf(String.valueOf(
////					logFile))));
////			log = new PrintWriter(System.err);
////		}
//
//		loadDrivers(dbProps);
//		createPools(dbProps);
//	}
	
	/**
	 * register driver classes.
	 * The tokenizer uses the default delimiter set, which is " \t\n\r\f": the space character, 
	 *		the tab character, the newline character, the carriage-return character, 
	 *		and the form-feed character. Delimiter characters themselves will not be treated as tokens.
	 * @param props
	 */
	private void loadDrivers(Properties props) {
		String driverClasses = props.getProperty("drivers");
		for (StringTokenizer st = new StringTokenizer(driverClasses);
		st.hasMoreElements(); ) {
			String driverClassName = st.nextToken().trim();
			try {
				Driver driver = (Driver) Class.forName(driverClassName).newInstance();
				DriverManager.registerDriver(driver);
				///drivers.addElement(driver);
				//log("成功注册JDBC驱动程序".concat(String.valueOf(String.valueOf(driverClassName))));
				//log.debug("成功注册JDBC驱动程序".concat(String.valueOf(String.valueOf(
				//    driverClassName))));
			}
			catch (Exception e) {
				// log(String.valueOf(String.valueOf((new StringBuffer("无法注册JDBC驱动程序: ")).append(driverClassName).append(", 错误: ").append(e))));
				log.error( (new StringBuffer("无法注册JDBC驱动程序: "))
								.append(driverClassName)
								.append(", 错误: ")
								.append(e) );
			}
		}		
	}	
	
//	/**
//	 * create pools when startup.
//	 * @param props
//	 */
//	private void createPools(Properties props) {
//		Enumeration propNames = props.propertyNames();
//		do {
//			if (!propNames.hasMoreElements()) {
//				break;
//			}
//			String name = (String) propNames.nextElement();
//			if (name.endsWith(".url")) {
//				String poolName = name.substring(0, name.lastIndexOf("."));
//				String url = props.getProperty(String.valueOf(String.valueOf(poolName)).
//						concat(".url"));
//				if (url == null) {
//					log.info( (new StringBuffer("没有为连接池"))
//									.append(poolName)
//									.append("指定URL") );
//				}
//				else {
//					String driver = props.getProperty(String.valueOf(String.valueOf(
//							poolName)).concat(".driver"));
//					String user = props.getProperty(String.valueOf(String.valueOf(
//							poolName)).concat(".user"));
//					String password = props.getProperty(String.valueOf(String.valueOf(
//							poolName)).concat(".password"));
//					String maxconn = props.getProperty(String.valueOf(String.valueOf(
//							poolName)).concat(".maxconn"), "0");
//					int max;
//					try {
//						max = Integer.valueOf(maxconn.trim()).intValue();
//					}
//					catch (NumberFormatException e) {
//
//						log.info( (new StringBuffer("错误的最大连接数限制: "))
//										.append(maxconn)
//										.append(" .连接池: ")
//										.append(poolName) );
//						max = 0;
//						
//					}
//					DBConnectionPool pool = new DBConnectionPool(poolName, driver, url,
//							user, password, max, max);
//					pools.put(poolName, pool);
//					//log("成功创建连接池".concat(String.valueOf(String.valueOf(poolName))));
//					//log.debug("成功创建连接池".concat(String.valueOf(String.valueOf(
//					//    poolName))));
//				}
//			}
//		}
//		while (true);
//	}

	/**
	 *  get connection by default name.
	 *  default DataSource Pool Name is "jndi/jdbc", it is same to default JNDI name.
	 */
	public Connection getConnection( ) throws SQLException{
    	///log.debug( "--0--3--1");
		return getConnection( getDefaultDataSourceName() );
	}
	
	/**
	 * get Connection by name.
	 */
	public Connection getConnection(String name) throws SQLException{
		//DBConnectionPool pool = (DBConnectionPool) pools.get(name);
    	///log.debug( "--0--3--2");
		DataSource pool = this.getDataSource( name );
		if (pool != null) {
	    	///log.debug( "--0--3--pool not null");
			return pool.getConnection();
		}
		else {
	    	///log.debug( "--0--3--pool is null");
			return null;
		}
	}
	
	/**
	 * get connection by name, and set time of out.
	 * @param name connection pool name
	 * @param time the number for out of time, unit is microsecond
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection(String name, long time) throws SQLException{
		ConnectionPool pool = (ConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		else {
			return null;
		}
	}

	/**
	 * release connection object to pool.
	 * @//deprecated free connection by call conn.close().
	 * @param name
	 * @param conn
	 */
	protected void freeConnection(String name, Connection conn) {
		ConnectionPool pool = (ConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(conn);
			clients--;
		}
	}

	protected boolean isConnectionClosed(String name, Connection conn) {
		ConnectionPool pool = (ConnectionPool) pools.get(name);
		return pool.isConnectionClosed(conn);
	}
	

	/**
	 * <p>系统停止使用后，释放所有连接池的连接对象。</p>
     * WZW on 2005-10-29 增加对多个连接池管理的消除功能
     */
	public void release()
	{
		try
		{ 
			if (pools != null)
			{
				java.util.Iterator it = pools.entrySet().iterator() ;
				java.util.Map.Entry mapen=null;
				ConnectionPool pool= null;
				while(it.hasNext() ){
					mapen = (java.util.Map.Entry)it.next() ;
					pool=(ConnectionPool)mapen.getValue();
					if(pool!=null)
						pool.release();
				}
			}
		}
		catch (Exception e)
		{}
	}
	
//	/**
//	 * ？？？ Zeven on 2007-08-20
//	 *
//	 */
//	public synchronized void release() {
//		if (--clients != 0) {
//			return;
//		}
//		DBConnectionPool pool;
//		for (Enumeration allPools = pools.elements();
//		allPools.hasMoreElements(); pool.release()) {
//			pool = (DBConnectionPool) allPools.nextElement();
//			
//		}
//		for (Enumeration allDrivers = drivers.elements();
//		allDrivers.hasMoreElements(); ) {
//			Driver driver = (Driver) allDrivers.nextElement();
//			try {
//				DriverManager.deregisterDriver(driver);
//				log.info( (new StringBuffer("撤销JDBC驱动程序 ")).
//						append(driver.getClass().getName()).
//						append("的注册") );
//			}
//			catch (SQLException e) {
//				log.info( (new StringBuffer("无法撤销下列JDBC驱动程序的注册: "))
//									.append( driver.getClass().getName()) );
//			}
//		}
//		
//	}

	/***************************************************
	 * by Zeven
	 * below is implements of DataSourceProvider.
	 * 
	 ***************************************************/
	protected DataSource getDataSource() throws SQLException {
		return getDataSource( getDefaultDataSourceName() );
	}

	/**
	 * get DataSource by dataSourceName.
	 */
	protected DataSource getDataSource(String dataSourceName) throws SQLException {
    	///log.debug( "--0--3");
        
		ConnectionPool pool = pools.get(dataSourceName);
    	///log.debug( "--0--4");
		if (pool == null) {  //延时加载，而不是一开始就加载所有，有利有弊
            //获取配置信息
            //读取系统数据库配置xml文件
            //取多个值
            Vector<String> vec = new Vector<String>();
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@driverClassName");
            vec.add("data-sources@data-source[key=" + dataSourceName +
            		"]@databaseManagerName");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@url");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@username");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@password");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@maxCount");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@minCount");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@maxActive");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@maxWait");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@defaultAutoCommit");
            vec.add("data-sources@data-source[key=" + dataSourceName +
                    "]@defaultReadOnly");

            //一定要保证数据库配置文件存在
            String confUrl = getConfigFile();
            try {
				vec = XmlUtils.getElementValues( new FileInputStream(confUrl), vec);
				if (vec == null)
	                throw new SQLException("对应的数据库配置文件["+confUrl+"]不存在！");

	        } catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new SQLException("对应的数据库配置文件["+confUrl+"]不存在！");
			}
            String driver = vec.elementAt(0).toString();
            String databaseManagerName = vec.elementAt(1).toString();
            String url = (String) (vec.elementAt(2));
            String user = (String) (vec.get(3));
            String password = (String) (vec.get(4));
            int totalConnections = Integer.parseInt( (String) (vec.get(
                5)));
            int minConnections = Integer.parseInt( (String) (vec.get(6)));

            log.debug(dataSourceName + ".driverClassName = " + driver +
                      "\n\t" +
                      dataSourceName + ".url = " + url + "\n\t" +
                      dataSourceName + ".databaseManagerName = " + databaseManagerName + "\n\t" +
                      dataSourceName + ".user = " + user + "\n\t" +
                      dataSourceName + ".totalConnections = " +
                      totalConnections + "\n\t" +
                      dataSourceName + ".minConnections = " + minConnections);
            //log.debug(dataSourceName+".password = "+ password  ) ;
 
            pool = new ConnectionPool(dataSourceName, driver, databaseManagerName, url, user, 
            		  password,minConnections, minConnections );
            pools.put(dataSourceName, pool);
        }
		
		return pool;
	}

	private String getConfigFile() {
		return FileUtils.convert2LinuxPath( getConfigPath() 
							+ File.separator
							+ Database_ConfigFile_);
	}

	/***************************************************
	 * by Zeven
	 * below implements of DataSource.
	 * 
	 ***************************************************/
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	

    /**
     * 缺省的DataSource 的名称。
     */
    protected String defaultDataSourceName = "jndi/jdbc";

	private String configPath = null;
 

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	public void setDefaultDataSourceName(String dataSourceName) {
		this.defaultDataSourceName = dataSourceName;
		
	}

	public void setConfigPath(String configPath) {
		this.configPath  = configPath;
	}

	public String getConfigPath() {
		return configPath;
	}

}
