/*
 * @(#)LdtActionServlet.java		    1.00 2004/04/16
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

package com.kingcore.framework.tag;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

import com.kingcore.framework.Constants;
import com.kingcore.framework.util.ConvertUtils;


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


public class ConnBean extends ActionServlet
{
    /**
     * 本方法是继承父类ActionServlet所有的初始化方法后再加入我们自己的处理
     */
    public void init() throws ServletException
    {
    	log.debug("LdtActionServlet开始init()!");
        initInternal();
        initOther();
        initServlet();

        // Initialize modules as needed
        getServletContext().setAttribute(Globals.ACTION_SERVLET_KEY, this);
        ModuleConfig moduleConfig = initModuleConfig("", config);
        initModuleMessageResources(moduleConfig);
        initModuleDataSources(moduleConfig);
        initModulePlugIns(moduleConfig);
        moduleConfig.freeze();
        Enumeration names = getServletConfig().getInitParameterNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            if (!name.startsWith("config/"))
            {
                continue;
            }
            String prefix = name.substring(6);
            moduleConfig =
                initModuleConfig(
                    prefix,
                    getServletConfig().getInitParameter(name));
            initModuleMessageResources(moduleConfig);
            initModuleDataSources(moduleConfig);
            initModulePlugIns(moduleConfig);
            moduleConfig.freeze();
        }
        destroyConfigDigester();

        System.out.println("LdtActionServlet");

        ServletContext context = null;
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet set = null;

        ArrayList roles = new ArrayList();
        String sql = "";
        int i = 0, count = 0;
        try
        {
            context = getServletContext();
            DataSource dataSource =
                (DataSource) context.getAttribute(Globals.DATA_SOURCE_KEY);
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            //Get Database Type
            DatabaseMetaData dmd = conn.getMetaData();
            setObjectInServletcontext(
                Constants.DATABASE_TYPE,
                getDatabaseType(dmd));
            sql = " select GVROLE_BH from GVROLE ";
            System.out.print("to 11 ");
            set = stmt.executeQuery(sql);
            while (set.next())
            {
            System.out.print("to 22");
                count++;
                roles.add(set.getString("GVROLE_BH"));
            }
            sql = " select GNQX_BH from GVGNQX where ROLE_BH=?";
            pstmt = conn.prepareStatement(sql);
            System.out.print("to 33 ");
            String rolename = null;
            for (i = 0; i < count; i++)
            {
                rolename = (String) roles.get(i);
                pstmt.setString(1, rolename);
                set = pstmt.executeQuery();
                ArrayList list = new ArrayList();
            System.out.print("to 44 ");
                while (set.next())
                {
                    list.add(set.getString(1));
                }
                context.setAttribute(Constants.ROLEPREFIX + rolename, list);
            }
            context.setAttribute(Constants.ROLESLIST, roles);
        }
        catch (SQLException se)
        {
        	System.out.println("SQLException se:"+se.getMessage());
            throw new ServletException("SQLException错误：" + se);
        }
        catch (Exception se)
        {
			System.out.println("Exception se:"+se.getMessage());
            throw new ServletException("Exception错误：" + se);
        }
        finally
        {
            try
            {
                if (set != null)
                {
                    set.close();
                }
                if (stmt != null)
                {
                    stmt.close();
                }
                if (pstmt != null)
                {
                    pstmt.close();
                }
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException se)
            {
            }
        }
    }
    /**
     * 返回数据库类型
     * @param DatabaseMetaData
     * @return String
     */
    public String getDatabaseType(DatabaseMetaData dmd)
    {
        String type = null;
        try
        {
            type = dmd.getDriverName();
            if (type.indexOf("jConnect") > -1)
            {
                type = "SYB";
            }
            if (type.indexOf("SQLServer") > -1)
            {
            	type = "SQL";
            }
            if (type.indexOf("Oracle") > -1)
            {
            	type = "ORA";
            }
            if (type.indexOf("AveConnect") > -1)
            {
            	type = "SQL";
            }
            log.debug("数据库类型：" + type);
            ConvertUtils.setDatabase(type);
        }
        catch (SQLException e)
        {
        }
        return type;
    }
    /**
     * 新建的方法
     * 把一个Object放到ServletContext中
     * @param String
     * @param Object
     */
    protected void setObjectInServletcontext(String name, Object o)
    {
        ServletContext context = this.getServletContext();
        context.setAttribute(name, o);
    }

}
