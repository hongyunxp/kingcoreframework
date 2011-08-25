/*
 * @(#)DataSourcImpl.java		    1.00 2004/04/15
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

package com.kingcore.framework.base.view ;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.struts.Globals;


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


public class DataSourcImpl
{

    /**
     * @see com.kingcore.framework.base.database.AbstractDataSource#getDefDataSource(ServletContext)
     */
    public static DataSource getDefDataSource(ServletContext context)
    {
        DataSource dataSource = null;
        dataSource = (DataSource) context.getAttribute(Globals.DATA_SOURCE_KEY);
        return dataSource;
    }

    /**
     * @see com.kingcore.framework.base.database.AbstractDataSource#getDefConnection(ServletContext)
     */
    public static Connection getDefConnection(ServletContext context)
    {
        DataSource dataSource = null;
        Connection conn = null;
        dataSource = getDefDataSource(context);
        try
        {
            conn = dataSource.getConnection();
        }
        catch (SQLException e)
        {
        }
        return conn;
    }

    /**
     * @see com.kingcore.framework.base.database.AbstractDataSource#getDataSource(ServletContext, String)
     */
    public static DataSource getDataSource(
        ServletContext context,
        String prefix)
    {
        DataSource dataSource = null;
        dataSource =
            (DataSource) context.getAttribute(Globals.DATA_SOURCE_KEY + prefix);
        return dataSource;
    }

    /**
     * @see com.kingcore.framework.base.database.AbstractDataSource#getConnection(ServletContext, String)
     */
    public static Connection getConnection(
        ServletContext context,
        String prefix)
    {
        DataSource dataSource = null;
        Connection conn = null;
        dataSource = getDataSource(context, prefix);
        try
        {
            conn = dataSource.getConnection();
        }
        catch (SQLException e)
        {
        }
        return conn;
    }

    public static DataSource getDataSource(
        ServletContext context,
        String key,
        String prefix)
    {
        DataSource dataSource = null;
        dataSource = (DataSource) context.getAttribute(key + prefix);
        return dataSource;
    }

    public static Connection getConnection(
        ServletContext context,
        String key,
        String prefix)
    {
        DataSource dataSource = null;
        Connection conn = null;
        dataSource = getDataSource(context, key, prefix);
        try
        {
            conn = dataSource.getConnection();
        }
        catch (SQLException e)
        {
        }
        return conn;
    }

}
