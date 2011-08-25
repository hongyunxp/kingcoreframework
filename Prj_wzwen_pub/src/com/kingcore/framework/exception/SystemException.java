/**
 * @(#)ApplicationException.java		    2006/03/21
 *
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Zeven.woo
 */

package com.kingcore.framework.exception;

import java.util.HashMap;

/** 
 * <p>系统异常，所有系统异常扩展该类。</p>
 * @author	WUZEWEN on 2006.03.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */

public class SystemException extends BaseException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemException(String inMessage) {
		super(inMessage);
	}
	
	public SystemException(String inMessage, Throwable inThrowable) {
		super(inMessage, inThrowable);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
