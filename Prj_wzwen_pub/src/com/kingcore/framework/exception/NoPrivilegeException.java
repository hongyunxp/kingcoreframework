/*
 * @(#)NoPrivilegeException.java		    1.00 2004/04/17
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.exception;



/**
 * @version		1.00 2004.04.17
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * import org.apache.struts.util.ModuleException;
 * ModuleException
 */

public class NoPrivilegeException extends BaseException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoPrivilegeException(String inMessage,int showType) {
		super(inMessage, showType);
	}
	public NoPrivilegeException(String inMessage) {
		super(inMessage);
	}
	
	public NoPrivilegeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
