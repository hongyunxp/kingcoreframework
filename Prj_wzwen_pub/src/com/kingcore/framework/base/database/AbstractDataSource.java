/*
 * @(#)AbstractDataSource.java		    1.00 2004/04/16
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

package com.kingcore.framework.base.database;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * @version		1.00 2004.04.15
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
public abstract class AbstractDataSource
{
    public abstract DataSource getDefDataSource(ServletContext context);

    public abstract Connection getDefConnection(ServletContext context);

    public abstract DataSource getDataSource(
        ServletContext context,
        String prefix);

    public abstract Connection getConnection(
        ServletContext context,
        String prefix);

    public abstract DataSource getDataSource(
        ServletContext context,
        String key,
        String prefix);

    public abstract Connection getConnection(
        ServletContext context,
        String key,
        String prefix);
}
