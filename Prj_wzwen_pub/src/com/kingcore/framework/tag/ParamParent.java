/*
 * @(#)DataBean.java        1.00 2004/04/18
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


package com.kingcore.framework.tag ;

/**
 * @version		1.00 2004.04.18
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 

public interface ParamParent {
    /**
     * Adds a parameter name-value pair represented by the
     * embedded ParamTag. The value is URL encoded.
     */
    void setParam(String name, String value);
}