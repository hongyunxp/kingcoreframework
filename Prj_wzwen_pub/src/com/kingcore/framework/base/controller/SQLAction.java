/*
 * @(#)SQLAction.java		    1.00 2004/04/13
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


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import wzw.util.DbUtils;

import com.kingcore.framework.Constants;
import com.kingcore.framework.bean.Page;
import com.kingcore.framework.bean.QueryDataSet;


/**
 * @version		1.00 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class SQLAction extends BaseAction
{
    private static Logger log = Logger.getLogger( SQLAction.class);
    /**
     * 定义一些常量
     */
    public static final String CHANGE_PAGE = "changepage";
    public static final String SEARCH_ACTION = "searchaction";
    public static final String DEAFLT_ACTION = "defalutaction";
    public static final String SORT_ACTION = "sortaction";

    /**
     * 查询用的 Sql语句。
     */
    private String sqlString = null ;

    /**
     * sqlString 的设置方法，要在子类中实现。
     */
    public void setSqlString( String sql )
    {

    }
    /**
     * QueryDataSet 实例的名称
     */
    private String dataSetName;

    public void setDataSetName(String name)
    {
        dataSetName = name;
    }

    public String getDataSetName()
    {
        return dataSetName;
    }

    /**
     * 格式化sql语句
     * @param sql:String
     * @return String
     */
    public static String formatSql(String sql)
    {
        String formatedSql = "";
        String tmpSql = sql.toUpperCase();
        int vSelect = 0;
        int vFrom = 0;
        int vWhere = 0;
        int vOrder = 0;
        int vGroup = 0;
        int vHaving = 0;
        int pos = 0;
        vSelect = tmpSql.indexOf("SELECT");
        vFrom = tmpSql.indexOf("FROM");
        if (vFrom > 0)
        {
            vSelect += 6;
            formatedSql = "select " + sql.substring(vSelect, vFrom) + " from ";
            vFrom += 4;
        }
        pos = vFrom;
        vWhere = tmpSql.indexOf("WHERE");
        if (vWhere > 0)
        {
            formatedSql = formatedSql + sql.substring(vFrom, vWhere) + "where";
            vWhere += 5;
        }
        else
        {
            log.debug("此sql语句：(" + sql + ")，中不包含where子语句");
            return formatedSql;
        }
        pos = vWhere;
        vGroup = tmpSql.indexOf("GROUP");
        if (vGroup > 0)
        {
            formatedSql = formatedSql + sql.substring(vWhere, vGroup);
            vGroup = tmpSql.indexOf("BY", vGroup);
            if (vGroup < 0)
            {
                log.error("此sql语句：(" + sql + ")，的group 后没跟by");
                return "";
            }
            vGroup += 2;
            pos = vGroup;
            vHaving = tmpSql.indexOf("HAVING");
            if (vHaving > 0)
            {
                formatedSql = formatedSql + sql.substring(vGroup, vHaving);
                vHaving += 6;
                pos = vHaving;
            }
        }
        vOrder = tmpSql.indexOf("ORDER");
        if (vOrder > 0)
        {
            formatedSql = formatedSql + sql.substring(pos, vOrder);
            vOrder = tmpSql.indexOf("BY", vOrder);
            if (vOrder < 0)
            {
                log.error("此sql语句：(" + sql + ")，的group 后没跟by");
                return "";
            }
            pos = vOrder + 2;
        }
        formatedSql = formatedSql + sql.substring(pos);
        log.debug("格式化后的sql语句为：" + formatedSql);
        return formatedSql;
    }

    /**
     * Action 处理的入口
     * @param sql:String
     * @return String
     */
    public ActionForward executeAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        doInitialize(request);
        doExecute(mapping, form, request, response);
        return doForword(mapping, form, request, response);
    }

    /**
     * Action 处理的预留的处理空间
     * @param sql:String
     * @return String
     */
    public void doInitialize(HttpServletRequest request) throws Exception
    {

    }

    public void doExecute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse respone)
        throws Exception
    {
    	//根据参数确定是哪种操作
        String action = getAction(request);
        //处理翻页操作
        if (action.equals(CHANGE_PAGE))
        {
            String numberString = getParameter(request, "pageNumber", "");
            if (!numberString.equals(""))
            {
                int pageNumber = Integer.parseInt(numberString.trim());
                if (gotoPage(mapping, request, pageNumber))
                {
                    return;
                }
            }
        }
        //处理查询
        if (action.equals(SEARCH_ACTION))
        {
            if (doSearch(mapping, request))
            {
                return;
            }
        }
        //排序处理
        if (action.equals(SORT_ACTION))
        {
            if (doSort(mapping, request))
            {
                return;
            }
        }
        //缺省的查询
        String sql = this.createQuerySql(request);
        this.executeQuery(mapping, request, sql);
    }

    public ActionForward doForword(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        return null;
    }
    /**
     * 创造sql语句的select子句，其中“select”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“select”了。
     * @return String 返回一个不带“select”的select子句
     */
    public String createSelectString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * 创造sql语句的from子句，其中“from”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“from”了。
     * @return String 返回一个不带“from”的from子句
     */
    public String createFromString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * 创造sql语句的where子句，其中“where”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“where”了。
     * @return String 返回一个不带“where”的where子句
     */
    public String createWhereString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * 创造sql语句的group by子句，其中“group by”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“group by”了。
     * @return String 返回一个不带“group by”的group by子句
     */
    public String createGroupByString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * 创造sql语句的having子句，其中“having”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“having”了。
     * @return String 返回一个不带“having”的having子句
     */
    public String createHavingString(HttpServletRequest request)
    {
        return "";
    }

    /**
     * 创造sql语句的order by子句，其中“order by”已经被格式化会在组合成sql语句的时候默认添加，
     * 在这里就不能再加“order by”了。
     * @return String 返回一个不带“order by”的order by子句
     */
    public String createOrderByString(HttpServletRequest request)
    {
        return "";
    }
    /**
     * 跳转到指定页。
     * @param request:HttpServletRequest
     * @param pageNumber:int 指明跳转到哪一页
     * @return boolean 返回是否跳转成功
     */
    public boolean gotoPage(
        ActionMapping mapping,
        HttpServletRequest request,
        int pageNumber)
        throws Exception
    {
        String sql = null;
        QueryDataSet dataSet = getDataSetInstance(request);
        if (dataSet.getPageCount() == 0)
        {
            return false;
        }
        if (pageNumber > dataSet.getPageCount())
        {
            pageNumber = dataSet.getPageCount();
        }
        if (pageNumber < 1)
        {
            pageNumber = 1;
        }
        if (dataSet.isNeedPaged(pageNumber))
        {
            dataSet.turnToPage(pageNumber);
            sql = dataSet.getLastSql();
            if (sql == null)
            {
                sql = createQuerySql(request);
            }
            dataSet.reIndex();
            executeQuery(mapping, request, sql);
        }
        return true;
    }
    /**
     * 得到当前对应的ListView的实例
     * @return ListView
     */
    public QueryDataSet getDataSetInstance(HttpServletRequest request)
    {
        return getDataSetInstance(request, getDataSetName());
    }

    /**
     * 根据数据列表名称，获得数据DataSet
     * @param dataSetName:String
     * @return DataSet
     */
    public QueryDataSet getDataSetInstance(
        HttpServletRequest request,
        String dataSetName)
    {
        QueryDataSet ds = null;
        HttpSession session = request.getSession(true);
        Object o = session.getAttribute(dataSetName);
        if (o != null)
            ds = (QueryDataSet) o;
        return ds;
    }

    /**
    * 根据参数名称后缀，获得参数的值的数组
    * @param String suffix 参数名称后缀
    * @return String[] 最多支持返回100个
    */
    public String[] getParameterWithSuffix(
        HttpServletRequest request,
        String suffix)
    {
        java.util.Enumeration names = request.getParameterNames();
        String[] subname = new String[100];
        int bound = 0;
        while (names.hasMoreElements())
        {
            String name = names.nextElement().toString();
            if (name.endsWith(suffix))
            {
                subname[bound] =
                    name.substring(0, name.length() - suffix.length());
                bound++;
            }
        }
        if (bound < 1)
        {
            return null;
        }
        String[] subname2 = new String[bound];
        for (int i = 0; i < bound; i++)
            subname2[i] = subname[i];
        return subname2;
    }
    /**
     * 获得要查询的字段名
     * @param request
     * @param searchName
     * @return String[]
     */
    protected String[] getSearchFieldNames(
        HttpServletRequest request,
        String searchName)
    {
        String tmp = ":" + searchName;
        String[] searchs = getParameterWithSuffix(request, tmp);
        String search = null;
        if (searchs == null)
        {
            tmp = tmp + ".y";
            searchs = getParameterWithSuffix(request, tmp);
        }
        if (searchs == null)
            return null;
        search = searchs[0];
        java.util.StringTokenizer s =
            new java.util.StringTokenizer(search, ",");
        int fieldCount = s.countTokens();
        String[] re = new String[fieldCount];
        int i = 0;
        while (s.hasMoreElements())
        {
            re[i] = s.nextToken();
            i++;
        }
        return re;
    }
    /**
     * 根据SQL语句（Select语句）和查找名称（search,hsearch,lsearch）获得查找语句。
     * 创建日期：(2001-10-17 20:20:57)
     * @param java.lang.String theString SQL语句
     * @param java.lang.String searchName
     * @return java.lang.String
     */
    public String getSearchString(
        HttpServletRequest request,
        String searchName)
    {
        QueryDataSet dataSet = this.getDataSetInstance(request);
        String mySelect =
            "select "
                + dataSet.getSelectString()
                + " from "
                + dataSet.getFromString();
        String myGroupBy = "";
        if (!dataSet.getGroupByString().equals(""))
        {
            myGroupBy = " group by " + dataSet.getGroupByString();
        }
        String sql = mySelect + " where 1=0 " + myGroupBy;
        HashMap colsInfo = getColumnsInfo(request, sql);

        String[] str = getSearchFieldNames(request, searchName);
        if (str == null)
        {
            log.debug("没有找到要查询的字段！");
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int cType = 0; //0:number,1:String
        boolean firstOne = true;
        for (int i = 0; i < str.length; i++)
        {
            String fvalue = getParameter(request, str[i], null);
            if (fvalue == null || fvalue.equals(""))
                continue;
            int ti = str[i].indexOf(".");
            Integer coltypeObject = null;
            if (ti > 0)
            {
                coltypeObject =
                    (Integer) colsInfo.get(str[i].substring(ti + 1));
            }
            else
                coltypeObject = (Integer) colsInfo.get(str[i]);
            if ((coltypeObject.intValue() == java.sql.Types.CHAR)
                || (coltypeObject.intValue() == java.sql.Types.LONGVARCHAR)
                || (coltypeObject.intValue() == java.sql.Types.VARCHAR))
                cType = 1;
            else
                cType = 0;
            if (firstOne)
                sb.append(" where (");
            else
                sb.append(" and (");
            sb.append(str[i]);
            if (cType == 0)
                sb.append(" = ");
            if (cType == 1)
                sb.append(" like '%");
            sb.append(fvalue);
            if (cType == 1)
                sb.append("%'");
            sb.append(")");
            firstOne = false;
        }

        if (sb.length() > 5)
        {
            return mySelect + sb.toString();
        }
        return "";
    }
    /**
    * 根据SQL语句，获得查询列的数据类型.
    * @param request: HttpServletRequest
    * @param mySql java.lang.String
    * @return java.util.Hashtable
    * @exception java.lang.Throwable
    */
    public HashMap getColumnsInfo(HttpServletRequest request, String mySql)
    {
        HashMap htable = new HashMap();
        Connection conn = null;
        Statement statement = null;
        ResultSet set = null;
        try
        {
            conn = wzw.util.DbUtils.getConnection(request);
            statement = conn.createStatement();
            set = statement.executeQuery(mySql);
            ResultSetMetaData rsmd = set.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
            {
                String cName = rsmd.getColumnName(i);
                int cType = rsmd.getColumnType(i);
                htable.put(cName, new Integer(cType));
            }
        }
        catch (Throwable e)
        {
			log.debug("debug", e);
            /// e.pri ntStackTrace();
        }
        finally
        {
            try
            {
                if (set != null)
                    set.close();
                if (statement != null)
                    statement.close();
                if (conn != null)
                    conn.close();
            }
            catch (java.sql.SQLException se)
            {
				log.debug("debug", se);
                /// se.pri ntStackTrace();
            }
        }
        return htable;
    }

    /**
    * 把子类得到的View放入到Session中，供页面（Jsp）调用
    * @param viewName:String
    * @param view:BaseView
    * @return void
    */
    public void setDataSetInSession(
        HttpServletRequest request,
        String dsName,
        QueryDataSet newDs)
    {
        HttpSession session = request.getSession(true);
        Object o = session.getAttribute(dsName);
        if (o != null)
            session.removeAttribute(dsName);
        session.setAttribute(dsName, newDs);
    }
    /**
     * 处理查询
     */
    public boolean doSearch(ActionMapping mapping, HttpServletRequest request)
    {
        String searchString = getSearchString(request, "search");
        if (searchString.length() < 5)
        {
            return false;
        }
        else
        {
            String whereString = createWhereString(request);
            String groupByString = createGroupByString(request);
            String havingString = createHavingString(request);
            String orderByString = createOrderByString(request);
            if (!whereString.equals(""))
            {
                searchString = searchString + " and " + whereString;
            }
            if (!groupByString.equals(""))
            {
                searchString = searchString + " group by " + groupByString;
                if (!havingString.equals(""))
                {
                    searchString = searchString + " having " + havingString;
                }
            }
            if (!orderByString.equals(""))
            {
                searchString = searchString + " order by " + orderByString;
            }
            getDataSetInstance(request).setLastSql(searchString);
            getDataSetInstance(request).resetCurrentPageIndex();
            getDataSetInstance(request).reIndex();
            try
            {
                executeQuery(mapping, request, searchString);
            }
            catch (Exception e)
            {
            }
        }
        return true;
    }
    /**
     * 得到Action类型
     */
    public String getAction(HttpServletRequest request)
    {
        String action = "";
        action = getParameter(request, "Action", DEAFLT_ACTION);
        return action;
    }
    /**
     *
     */
    public String createSqlString(HttpServletRequest request)
    {
        String sql = "";
        String selectString = "";
        String fromString = "";
        String whereString = "";
        String groupByString = "";
        String havingString = "";
        String orderByString = "";
        selectString = "select " + createSelectString(request);
        fromString = " from " + createFromString(request);
        whereString = createWhereString(request);
        if (!whereString.equals(""))
        {
            whereString = " where " + whereString;
        }
        groupByString = createGroupByString(request);
        if (!groupByString.equals(""))
        {
            groupByString = " group by " + groupByString;
        }
        havingString = createHavingString(request);
        if (!havingString.equals(""))
        {
            havingString = " having " + havingString;
        }
        orderByString = createOrderByString(request);
        if (!orderByString.equals(""))
        {
            orderByString = " order by " + orderByString;
        }
        sql =
            selectString
                + fromString
                + whereString
                + groupByString
                + havingString
                + orderByString;
        return sql;
    }

    /**
     *
     */
    public String createQuerySql(HttpServletRequest request)
    {
        QueryDataSet dataSet = getDataSetInstance(request);
        String sql = "";
        sql = createSqlString(request);
        dataSet.setLastSql(sql);
        dataSet.setFromString(createFromString(request));
        dataSet.setSelectString(createSelectString(request));
        dataSet.setWhereString(createWhereString(request));
        dataSet.setGroupByString(createGroupByString(request));
        dataSet.setHavingString(createHavingString(request));
        dataSet.setOrderByString(createOrderByString(request));
        return sql;
    }

    public String getSortString(HttpServletRequest request)
    {
        String sortString = "";
        String sortName = "";
        sortName = getParameter(request, "sortName", "");
        if (sortName.equals(""))
            return "";
        setObjectInSession(
            request,
            this.getClass().getName() + "sortName",
            sortName);
        String direction = "";
        Object o =
            getObjectInSession(request, this.getClass().getName() + sortName);
        if (o != null)
            direction = String.valueOf(o).toUpperCase();
        if (direction.equals("DESC"))
            direction = "ASC";
        else
            direction = "DESC";
        setObjectInSession(
            request,
            this.getClass().getName() + sortName,
            direction);
        if (sortName != null && !sortName.equals(""))
        {
            sortString = " order by " + " " + sortName + " " + direction;
        }
        return sortString;
    }

    public void executeQuery(
        ActionMapping mapping,
        HttpServletRequest request,
        String sql)
        throws Exception
    {}

    public Page doQuery(
        ActionMapping mapping,
        HttpServletRequest request,
        String sql,
        int pageSize,
        int start,
        int end)
        throws Exception
    {
    	log.debug("doQuery()此次查询的sql语句为：" + sql );
        RowSet datas = null ;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //ResultSetMetaData rsmd = null;
        int count = 0;
        int i = 0, colcount = 0;
        //String[] colName;
        try
        {
            conn = wzw.util.DbUtils.getConnection(request);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            log.fatal("have executeQuery get ") ;

        	//datas.populate( rs ) ; 
        	datas = DbUtils.resultSet2RowSet( rs );



            //rsmd = set.getMetaData();
            //colcount = rsmd.getColumnCount();
            //colName = new String[colcount];
            //for (i = 1; i <= colcount; i++)
            //{
            //    colName[i - 1] = rsmd.getColumnName(i);
            //}
            //
            //set = pstmt.executeQuery();  twice??
            //log.fatal("have executeQuery2 " + colName[1]);
            //while (set.next())
            //{
            //    count++;
            //   log.fatal("have executeQuery3 "+set.getObject(colName[1]));
            //    if (count < start + 1 || count > end + 1)
            //        continue;
            //    DataBean bean = new DataBean();
            //    for (i = 0; i < colcount; i++)
            //    {
            //        bean.put(colName[i], set.getObject(colName[i]));
            //    }
            //    datas.add(bean);
            //}
        }
        catch (SQLException se)
        {
            log.fatal("doQuery()中出SQLException错误，错误为：" + se.getMessage());
            mapping.findForward("");
        }
        catch (Exception e)
        {
            log.fatal("doQuery()中出Exception错误，错误为：" + e.getMessage());
            mapping.findForward("");
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery的finally中关闭set时出错，错误为：" + se.getMessage());
            }
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery的finally中关闭pstmt时出错。");
            }
            try
            {
                if (conn != null)
                {
                    conn.close();
                }
            }
            catch (SQLException se)
            {
                log.fatal("doQuery的finally中关闭conn时出错。");
            }
        }
        return new Page(datas, pageSize, start, end, count);
    }

    /**
    * 增加数据到数据列表。
    * 创建日期：(2001-10-17 20:20:57)
    * @param DataBean
    * @return void
    */
    public void addData(HttpServletRequest request, Page page) throws Exception
    {
        try
        {
            ///// getDataSetInstance(request).addData(page);
        }
        catch (Exception e)
        {
            log.debug(e);
            throw new Exception(e);
        }
    }
    /**
     * 得到用户登录信息
     * @param request
     */
    public Object getUserLoginBean(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        return session.getAttribute(Constants.USERLOGINBEAN);
    }
    /**
     * 处理排序操作
     * @param mapping: ActionMapping
     * @param request: HttpServletRequest
     */
    public boolean doSort(ActionMapping mapping, HttpServletRequest request)
    {
        String sql = null;
        String sortString = getSortString(request);
        sql = getDataSetInstance(request).getLastSql();
        if (sql != null && !sql.equals(""))
        {
            int order_pos = sql.indexOf(" order ");
            if (order_pos > 0)
                sql =
                    sql.substring(0, order_pos)
                        + " "
                        + sortString;
            else
                sql = sql + sortString;
        }else{
        	return false;
        }
        getDataSetInstance(request).setLastSql(sql);
        getDataSetInstance(request).reIndex();
        try
        {
            executeQuery(mapping, request, sql);
        }
        catch (Exception e)
        {
        }
        return true;
    }
}
