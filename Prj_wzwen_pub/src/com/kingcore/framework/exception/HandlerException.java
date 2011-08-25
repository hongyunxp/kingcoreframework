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

/**
 * <p>Handler≤„“Ï≥£ª˘¿‡°£</p>
 * @author Zeven on 2008-5-30
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class HandlerException extends BaseException {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * Constructor for HandlerException.
	 * @param msg the detail message
	 */
	public HandlerException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for HandlerException.
	 * @param msg the detail message
	 * @param cause the root cause (usually from using a underlying
	 * data access API such as JDBC)
	 */
	public HandlerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
