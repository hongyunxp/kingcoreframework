/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.transaction;

import java.util.HashMap;

/**
 * <p>事务管理器接口。
 * 		事务管理放在业务处理层，是实现业务处理层的基础。
 * 		事务管理的内容很多，包括数据库连接事务，邮件发送事务，文件读写事务，远程调用事务等等。
 * 
 * try {
 *  TransactionManager tm = new ConnectionTransactionManager();
 * 	tm.begin();
 * 	log("delete before" ,tm.getStatus());
 * 
 * 	executeDelete("delete from test");
 * 	executeDelete("delete from test1");
 * 	log("delete after" ,tm.getStatus());
 * 	tm.commit();
 * 	log("delete commit after" ,tm.getStatus());
 * 
 * } catch (TransactionException e) {
 * 	try {
 * 		tm.rollback();
 * 	} catch (RollbackException e1) {
 * 		e1.printStackTrace();
 * 	}
 * 	e.printStackTrace();
 * } catch (Exception e) {
 * 	e.printStackTrace();
 * 	try {
 * 		tm.rollback();
 * 	} catch (RollbackException e1) {
 * 		e1.printStackTrace();
 * 	}
 * }
 * 
 * 		</p>
 * @author Zeven on 2007-8-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface TransactionManager {
	
//    public abstract TransactionStatus getTransaction(TransactionDefinition transactiondefinition)
//    throws TransactionException;
//
//	public abstract void commit(TransactionStatus transactionstatus)
//	    throws TransactionException;
//	
//	public abstract void rollback(TransactionStatus transactionstatus)
//	    throws TransactionException;

	public abstract void begin()
	    throws TransactionException;

	public void begin(TransactionType transType) 
		throws TransactionException;
	
	public abstract void commit()
		    throws TransactionException;
	
	public abstract void rollback()
	    throws TransactionException;

}
