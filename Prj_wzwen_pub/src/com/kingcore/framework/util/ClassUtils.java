/*
 * @(#)ClassUtils.java		    2004/04/21
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

package com.kingcore.framework.util ;

import java.io.Serializable;
import java.sql.SQLException;


/**
 * @version		1.00 2004.04.21
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */

public class ClassUtils implements Serializable {

    
    public ClassUtils()
    {
    }
    
    //get a instance ;
    public ClassUtils getInstance()
    {
    	return new ClassUtils() ;
    }  

    /**
     * <p>Loads and returns the <code>Class</code> of the given name.
     * By default, a load from the thread context class loader is attempted.
     * If there is no such class loader, the class loader used to load this
     * class will be utilized.</p>
     *
     * @exception SQLException if an exception was thrown trying to load
     *  the specified class
     */      
    public Class loadClass(String className) throws SQLException {

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            
        	Class clazz = Object.class;
        	clazz = cl.loadClass(className) ;
            return clazz ;
        } catch (Exception e) {
            throw new SQLException("Cannot load column class '" +
                                   className + "': " + e);
        }

    }


}
