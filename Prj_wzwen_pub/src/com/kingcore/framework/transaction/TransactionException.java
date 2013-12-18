/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
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

import org.springframework.dao.DataAccessException;


/**
 * <p>Handler层综合事务异常。</p>
 * @author Zeven on 2008-5-30
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TransactionException extends DataAccessException {

	public TransactionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor for TransactionException.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using a underlying
	 * data access API such as JDBC)
	 */
	public TransactionException(String msg, Throwable cause) {
		super(msg, cause);
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
