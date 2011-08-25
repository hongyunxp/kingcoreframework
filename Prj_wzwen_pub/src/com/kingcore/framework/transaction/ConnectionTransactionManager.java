package com.kingcore.framework.transaction;

/*
很重要的一点是要想实现事务，我们必须用同一个数据库连接执行这些语句，最终才能做到统一的提交和回滚。 
我们可以这样假设 
insert1和insert2为不同DAO的方法 
仔细观察，我们的insert1和insert2并没有负责打开连接和关闭连接。而是间接的调用TransactionHelper.executeNonQuery(sql); 
这样使我们执行的所有方法都是使用同一个连接进行数据库操作。 

  其实这个例子只是想告诉大家要实现声明式事务的一部分内容，这个例子只能实现简单的单事务模型，要实现更复杂的事务传播模型如嵌套等，还需要我们使用更多的技术，如AOP等等。先写到这里，希望对大家有所帮助！

Wzw： 使用
tm.begin() 方法：开始启用本地线程的conn，并且可能不只一个，需要使用stack管理所有的conn， 
Stack<Map<String,Connection>();-->事务嵌套+多DataSource。
tm.commit() 方法：提交对应list里面所有的conn；
tm.rollback() 方法：回滚对应list里面所有的conn；
*/   

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.kingcore.framework.context.ApplicationContext;
   
public final class ConnectionTransactionManager implements TransactionManager{    
       
	private static Logger log = Logger.getLogger(ConnectionTransactionManager.class);
   //使用ThreadLocal持有当前线程的数据库连接      Map:支持多数据源; Stack:支持事务嵌套
   private final static ThreadLocal<Stack<Map<String,Connection>>> connection_holder 
   				= new ThreadLocal<Stack<Map<String,Connection>>>();    
   //private final static ThreadLocal<Connection> connection_holder = new ThreadLocal<Connection>();    
       
//   //连接配置，来自connection.properties    
//   private final static Properties connectionProp = new Properties();    
//       
//   static{         
//       //加载配置文件    
//       InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("connection.properties");    
//       try {    
//               
//           connectionProp.load(is);    
//           is.close();    
//           //加载驱动程序    
//           Class.forName(connectionProp.getProperty("driverClassName"));    
//       } catch (IOException e) {    
//            throw new RuntimeException(e.getMessage(),e);    
//       }catch(ClassNotFoundException e){    
//           throw new RuntimeException("驱动未找到",e);    
//       }    
//   }    
       
   //获取当前线程中的数据库连接    
	public static Connection getCurrentConnection(TransactionType transType, String dataSourceName) {
		
		//intiTransaction(transType);  //每次可以初始一次，创建需要创建的对象
		Map<String, Connection> connsMap = getLastConnectionsMap();
		if (connsMap == null) { //如果没有开启事务(tm.begin)，则返回null.
			return null;
		}

		Connection conn = connsMap.get(dataSourceName);
		if (conn == null &&   //如果必须要有事务，则创建
				(transType==TransactionType.NEW_TRANSACTION
					||transType==TransactionType.REQUIRED_TRANSACTION)) {
			try {
				conn = ApplicationContext.getInstance().getDataSourceManager()
								.getConnection(dataSourceName);
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				log.equals(e);
			}
			connsMap.put(dataSourceName, conn);
		}
		return conn;
	}
       
//   //执行SQL语句    
//   public static int executeNonQuery(String sql) throws SQLException{    
//           
//       Connection conn = getCurrentConnection();    
//            
//       return conn.createStatement().executeUpdate(sql);    
//   
//   }    
       
	/**
	 * 弹出最后一个connectionsMap.
	 * @return
	 */
   private static Map<String, Connection> popConnectionsMap(){
	   Map<String, Connection> connsMap = null;
		Stack<Map<String, Connection>> connsStack = connection_holder.get();    
		//如果还没有事务堆栈，则先创建并记录
		if (connsStack==null) {
			return connsMap;
		}
		//如果还没有connetion Map,也需要先创建并压入堆栈
		connsMap = connsStack.pop(); //pop out
		if (connsMap==null) {
			return connsMap;
		}
		return connsMap;
   }

	/**
	 * 获取最后一个connectionsMap，但是不弹出.
	 * @return
	 */
   private static Map<String, Connection> getLastConnectionsMap(){
	   Map<String, Connection> connsMap = null;
		Stack<Map<String, Connection>> connsStack = connection_holder.get();    
		//如果还没有事务堆栈，则先创建并记录
		if (connsStack==null) {
			return connsMap;
		}
		//如果还没有connetion Map,也需要先创建并压入堆栈
		connsMap = connsStack.lastElement(); //just get, not pop out
		if (connsMap==null) {
			return connsMap;
		}
		return connsMap;
   }
   
   //提交事务    
   public void commit() throws TransactionException {    

	   Map<String, Connection> connsMap = popConnectionsMap();
	   if (connsMap==null) {
		   log.error("commit fail for the previous error:"+
					 "no transaction found! ");
		   return;
	   }
		//提交所有conn
		Iterator<Entry<String, Connection>> it = connsMap.entrySet().iterator() ;
		try {
			while(it.hasNext() ){
				Entry<String, Connection> mapen = it.next() ;
					mapen.getValue().commit();
			}
		} catch (SQLException e) {
			log.error(e);
			throw new TransactionException(e.getMessage());
		}
		log.info("commit successfully!");
   }    
       
       
   //回滚事务    
   public void rollback() throws TransactionException {

	   Map<String, Connection> connsMap = popConnectionsMap();
	   if (connsMap==null) {
		   log.error("rollback fail for the previous error:"+
					 "no transaction found! ");
		   return;
	   }
		//提交所有conn
		Iterator<Entry<String, Connection>> it = connsMap.entrySet().iterator() ;
		try {
			while(it.hasNext() ){
				Entry<String, Connection> mapen = it.next() ;
					mapen.getValue().rollback();
			}
		} catch (SQLException e) {
			log.error(e);
			throw new TransactionException(e.getMessage());
		}
		log.info("rollback successfully!");  
   }    
       
//   //创建一个不自动Commit的数据库连接    
//   private static Connection createNotAutoCommitConnection() {    
//       try {    
//               
////           Connection conn = DriverManager.getConnection(connectionProp.getProperty("url")+";databaseName="+ connectionProp.getProperty("databaseName")    
////                   ,connectionProp.getProperty("username")    
////                   ,connectionProp.getProperty("password"));  
//           Connection conn = ApplicationContext.getInstance().getDataSourceManager().getConnection();
//           conn.setAutoCommit(false);    
//           return conn;    
//       } catch (SQLException e) {    
//            throw new RuntimeException(e.getMessage(),e);    
//       }    
//   }

	public void begin() throws TransactionException { 
		begin(null);
	}
	
	public void begin(TransactionType transType) throws TransactionException { 
		intiTransaction(transType); //事务管理开始
	}
	
	private static void intiTransaction(TransactionType transType) throws TransactionException { 
		if (transType == TransactionType.MAYBE_TRANSACTION) {
			//getCurrentConnection();  nothing to do
			
		} else if (transType == TransactionType.REQUIRED_TRANSACTION) {
			Stack<Map<String, Connection>> connsStack = connection_holder.get();    
			//如果还没有线程事务堆栈，则先创建并记录
			if (connsStack==null) {
				connsStack = new Stack<Map<String, Connection>>();
				connection_holder.set(connsStack);
			}
			//如果还没有则创建多数据源事务:connetion Map,也需要先创建并压入堆栈，有就不用管
			Map<String, Connection> connsMap = connsStack.lastElement();
			if (connsMap==null || connsMap.size()<1) {  //如果一个都没有，就创建
				connsMap = new HashMap<String, Connection>();
				connsStack.push(connsMap);
			}
			// Connection放入Map在需要的时候再处理，也就是获取conn的时候。
			
		} else if (transType == TransactionType.NEW_TRANSACTION) {
			Stack<Map<String, Connection>> connsStack = connection_holder.get();    
			//如果还没有线程事务堆栈，则先创建并记录
			if (connsStack==null) {
				connsStack = new Stack<Map<String, Connection>>();
				connection_holder.set(connsStack);
			}
			//创建新的多数据源事务:connetion Map,并压入堆栈
			Map<String, Connection> connsMap = new HashMap<String, Connection>();
			connsStack.push(connsMap);
			
		}
	}
    
}
