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
 * <p>应用程序异常，所有应用程序异常扩展该类。</p>
 * @author	WUZEWEN on 2006.03.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */


public class ApplicationException extends BaseException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationException(String inMessage,int showType) {
		super(inMessage, showType);
	}
	public ApplicationException(String inMessage) {
		super(inMessage);
	}
	
	public ApplicationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
