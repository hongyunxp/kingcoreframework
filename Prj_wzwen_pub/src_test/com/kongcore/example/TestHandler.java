

package com.kingcore.example ;
 
import java.sql.Connection;
import java.util.Map;

import com.kingcore.framework.base.handler.AbstractHandler;

/**
 * <p>业务逻辑处理，事物完整性控制类。</p>
 * @author Zeven on 2007-8-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TestHandler extends AbstractHandler {

	/* (non-Javadoc)
	 * @see com.kingcore.framework.base.handler.Handler#getInstance()
	 */

    /**
     * 单件的设计模式替换全静态方法，便于spring 构造
     */
	private static TestHandler instance;
	

	public static TestHandler getInstance(){  
		if (instance == null) {
			instance = new TestHandler();
		}
		return instance;
	}
	/**
	 * 构造对象并且赋予默认值。 会被spring 调用，导致两次构造!!!!!!!!!!!!
	 *
	 */
	private TestHandler() {
		//...
		
		instance = this ;
	}
	
	/**
	 * <p>这是一个处理店铺注册的例子，
	 * 		展示如何使用业务层处理业务逻辑、控制事务。</P>
	 * @param map
	 * @throws Exceptioin
	 */
	public void shopRegister(Map map) throws Exception {
		
		// JDBC Connection 对象用于控制事务完整性。
		Connection conn = null;
		
		try {
			// 获取连接
			conn = this.getConnection();
			
			// 构建 DAO并且注入连接以便控制事务
			//UserDealDAO udd = new UserDealDAO();
			//ShopDealDAO sdd = new ShopDealDAO();
			//udd.setConnection(conn);
			//sdd.setConnection(conn);
			
			// 调用DAO各种数据处理方法
			// sdd.register(...);
			// udd.updateInfor(...);
			// ...
			
			// 处理成功提交事务
			conn.commit();
		} catch (Exception e) {
			// 处理不成功回滚事务
			conn.rollback();
			
		} finally {
			
			// 获取连接的地方一定要关闭连接
			if(conn!=null){
				conn.close();
			}
			
		}
	}
	
}
