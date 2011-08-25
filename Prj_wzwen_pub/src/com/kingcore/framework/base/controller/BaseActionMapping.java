/*
 * @(#)BaseActionMapping.java		    1.00 2004/04/16
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


package com.kingcore.framework.base.controller;

import org.apache.struts.action.ActionMapping;


/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 

//Extend the ActionMapping class
public class BaseActionMapping extends ActionMapping {
	
	//Add the new properties
	protected boolean logResult = false;
	protected String privilege = null ;

	public BaseActionMapping() {		
	//Call the ActionMapping's default Constructor
		super();
	}
	
	
	//Add matching setter/getter methods
	public void setLogResult(boolean logResult) {
	this.logResult = logResult;
	}		
	public boolean getLogResult() {
	return logResult;
	}
	
	//Add matching setter/getter methods
	public void setPrivilege(String privilege) {
	this.privilege = privilege;
	}		
	public String getPrivilege() {
	return privilege;
	}
}


