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

package com.kingcore.framework.exception;

import java.util.HashMap;

import org.springframework.dao.DataAccessException;

/**
 * <p>远程数据库访问异常，可以导致整个事务异常。</p>
 * @author Zeven on 2008-6-4
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class RemoteAccessException extends DataAccessException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteAccessException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	public RemoteAccessException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
