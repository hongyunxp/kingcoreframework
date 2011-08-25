/*
 * @(#)IConnection.java        1.00 2004/05/11
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


package com.kingcore.framework.bean ;

/**
 * @version		1.00 2004.05.11
 * @author		zewen.woo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */

import javax.servlet.http.HttpServletRequest;

public interface IConnection {
	
    /**
     * Adds a parameter name-value pair represented by the
     * embedded ParamTag. The value is URL encoded.
     */
    
    /**
     * getConnection
     */
    void getConnection (HttpServletRequest request);
    
    /**
     * freeConnection
     */
    void freeConnection ();
}