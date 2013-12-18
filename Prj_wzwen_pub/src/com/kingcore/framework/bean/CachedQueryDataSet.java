/*
 * @(#)CachedQueryDataSet.java		    1.00 2004/04/13
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

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


public class CachedQueryDataSet implements Serializable
{
	/**
	 * 日志变量
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.CachedQueryDataSet.class);

	/**
	* 图片路径
	*/

	protected static String imgPath = "jsp/image/";

	/**
	* 主键
	*/
	protected String primaryKey = "";

	/**
	* 总行数
	*/
	protected int rowCount;

	/**
	* 每页显示的行数
	*/
	protected int pageSize = 10;

	/**
	* 总页数
	*/
	protected int pageCount;

	/**
	*当前页号
	*/
	protected int currentPageIndex = 1;

	/**
	* 检索的第一条纪录行号
	*/
	protected int beginIndex;

	/**
	* 检索的最后一条纪录行号
	*/
	protected int endIndex;

	/**
	* 一行记录
	*/
	//protected DataBean data;

	/**
	* 所有数据集合
	*/
	protected CachedRowSet crs ;

	/**
	* 最后一次查询的SQL语句
	*/
	protected String lastSql;
	/**
	* 最后一次查询的select子句（除“select”)
	*/
	protected String selectString = "";
	/**
	* 最后一次查询的from子句（除“from”)
	*/
	protected String fromString = "";
	/**
	* 最后一次查询的where子句（除“where”)
	*/
	protected String whereString = "";
	/**
	* 最后一次查询的gruop by子句（除“group by”)
	*/
	protected String groupByString = "";
	/**
	*  最后一次查询的having子句（除“having”)
	*/
	protected String havingString = "";
	/**
	* 最后一次查询的order by子句（除“order by”)
	*/
	protected String orderByString = "";
	/**
	* 分页标记
	*/
	protected boolean isPaged;
	/**
	* 暂时不需要的变量
	*protected boolean isUpdate; //数据修改、删除标记
	*protected boolean isInsert; //数据增加标记
	*protected boolean isDetail; //数据明细标记
	*/

	/**
	* 设定图片的存放位置
	*/
	public static void setImgPath(String path)
	{
	imgPath = path;
	}

	/**
	* 取得图片存放位置
	*/
	public static String getImgPath()
	{
	return imgPath;
	}

	/**
	*设置primaryKey
	*/
	public void setPrimaryKey(String key)
	{
	primaryKey = key;
	}

	/**
	*获取primaryKey
	*/
	public String getPrimaryKey()
	{
	return primaryKey;
	}

	/**
	 * 返回开始行
	 * @return int
	 */
	public int getBeginIndex()
	{
	    return beginIndex;
	}
	/**
	* 当前页
	* @return int
	*/
	public int getCurrentPageIndex()
	{
	    return currentPageIndex;
	}
	/**
	 *得到所有的行集
	 * @return List
	 */
	public CachedRowSet getDatas()
	{
	    return this.crs ;
	}
	/**
	 *得到检索的最后一条记录号
	 * @return int
	 */
	public int getEndIndex()
	{
	    return endIndex;
	}
	/**
	 * 返回是否是分页
	 * @return boolean
	 */
	public boolean getIsPaged()
	{
	    return isPaged;
	}
	/**
	 * 返回最近一次执行的sql语句
	 * @return String
	 */
	public String getLastSql()
	{
	    System.out.print("CachedQueryDataSet:getLastSql()  lastSql=" + lastSql);
	    return lastSql;
	}
	public void setLastSql(String sql)
	{
	    lastSql = sql;
	}
	public String getSelectString()
	{
	    return selectString;
	}
	public void setSelectString(String select)
	{
	    selectString = select;
	}
	public String getFromString()
	{
	    return fromString;
	}
	public void setFromString(String from)
	{
	    fromString = from;
	}
	public String getWhereString()
	{
	    return whereString;
	}
	public void setWhereString(String where)
	{
	    whereString = where;
	}
	public String getGroupByString()
	{
	    return groupByString;
	}
	public void setGroupByString(String groupBy)
	{
	    groupByString = groupBy;
	}
	public String getHavingString()
	{
	    return havingString;
	}
	public void setHavingString(String having)
	{
	    havingString = having;
	}
	public String getOrderByString()
	{
	    return orderByString;
	}
	public void setOrderByString(String orderBy)
	{
	    orderByString = orderBy;
	}
	/**
	* 总页数
	* @return int
	*/
	public int getPageCount()
	{
	    return pageCount;
	}

	/**
	* 每页显示的行数
	* @return int
	*/
	public int getPageSize()
	{
	    return pageSize;
	}

	/**
	*当前页号
	*/
	public void setCurrentPageIndex(int index)
	{
	    currentPageIndex = index;
	}

	/**
	* 获得总的行数
	* @return int
	*/
	public int getRowCount()
	{
	    return rowCount;
	}
	/**
	* 获得当前行号
	* @return int
	*/
	public int getRow()
	{
		try{
			return crs.getRow();
		}catch( SQLException e){
			System.out.print("\nDataSet -- getrow execption "+e.getMessage()) ;
		}
		return 0 ;
	}
	/**
	* 分页情况下每页实际显示的行数，该方法主要是在JSP中调用
	* @return int
	*/
	public int getShowRows()
	{
	    if ( crs != null)
	    {
	        return crs.size( );
	    }
	    return 0;
	}
	/**
	* 判断是第一页
	* @return boolean
	*/
	protected boolean isFirstPage()
	{
	    if (currentPageIndex == 1)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* 判断是否是最后一页
	* @return boolean
	*/
	protected boolean isLastPage()
	{
	    if (currentPageIndex == getPageCount() )
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* 判断是否有下页
	* @return boolean
	*/
	protected boolean hasNextPage()
	{
	    if (currentPageIndex < pageCount)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	* 判断是否有上页
	* @return boolean
	*/
	protected boolean hasPreviousPage()
	{
	    if (currentPageIndex > 1)
	    {
	        return true;
	    }
	    return false;
	}
	/**
	 * 是否需要分页显示
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex)
	{
	    if (forPageIndex != currentPageIndex)
	    {
	        isPaged = true;
	    }
	    else
	    {
	        isPaged = false;
	    }
	    return isPaged;
	}
	/**
	* 转到下页
	* @return void
	*/
	public void nextPage()
	{
	    if (currentPageIndex < pageCount)
	        currentPageIndex++;
	}

	/**
	 * 重置页面
	 */
	public void reIndex()
	{
		if (crs.size()<=0) return ;
		if (currentPageIndex>getPageCount())
			setCurrentPageIndex( getPageCount()) ;
	    beginIndex = pageSize * (currentPageIndex - 1);
	    endIndex = pageSize * currentPageIndex - 1;
	    // crs 的指针重新定位 zewen.wu
	    try
	    {
	    	if (beginIndex ==0)
	    		crs.beforeFirst() ;
	    	else
	    		crs.absolute( beginIndex ) ;

	    }catch( SQLException exp)
	    {
	    	System.out.print("Can't reIndex,beginIndex="+beginIndex ) ;
	    }
	}

	public void resetCurrentPageIndex()
	{
	    currentPageIndex = 1;
	}

	/**
	* 转到某页
	* @param int pageIndex
	* @return void
	*/
	public void turnToPage(int newPageIndex)
	{
	    currentPageIndex = newPageIndex;
	    isPaged = false;
	}



	/**
	* 添加数据
	* @parma page:Page
	* @return void
	*/


	/**
	 * Constructor for QueryRowset.
	 */
	public CachedQueryDataSet() throws SQLException
	{
		doinit() ;
		this.crs = new CachedRowSetImpl() ;
	}

	public CachedQueryDataSet( CachedRowSet crs) throws SQLException
	{
		doinit() ;
		this.crs = crs ;
	}

	public CachedQueryDataSet( ResultSet rs) throws SQLException
	{
		doinit() ;
		this.crs.populate( rs ) ;
	}
	private void doinit()
	{
	    beginIndex = 0;
	    endIndex = pageSize - 1;
	    rowCount = 0;
	}

	/**
	* 添加数据
	* @parma page:Page
	* @return void
	*/
	public void addData(Page page)
	{
	    if (page != null)
	    {
	    	rowCount = page.getCount();
	        crs = null;//   wzw on 2006-10-26  page.getDatas();
	    	pageCount = page.getPageCount();
	    	currentPageIndex = 1 ;

	        //	当前的数据data在此 不使用。
	        //if (data == null && page.getDatas().size() != 0)
	        //{
	        //    data = (DataBean) page.getDatas().get(0);
	        //}
	    }
	    else
	    {
	    	try{
	    		crs = new CachedRowSetImpl();
	    	}
	    	catch( SQLException exp)
	    	{ }

	    }
	}

	/**
	 * 添加数据
	 */

	public void setDataset( CachedRowSet crs)
	{
		this.crs = crs ;
	}

	public void setDataset( ResultSet rs) throws SQLException
	{
		this.crs.populate( rs ) ;
	}


	/**
	 *	其它方法
	 */
	public boolean absolute( int row) throws SQLException
	{
		return ( crs.absolute( row ) ) ;
	}

	public void beforeFirst() throws SQLException
	{
		crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		crs.afterLast() ;
	}

	public boolean first() throws SQLException
	{
		return (crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return (crs.last() ) ;
	}

	public boolean previous() throws SQLException
	{
		return (crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		return (crs.next() ) ;
	}


	/**
	 *	BigDecimal 的geter 方法。
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( crs.getBigDecimal( colNum ) ) ;
	}

	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( crs.getBigDecimal( colName)) ;
	}

	/**
	 *	Blob 的geter 方法。
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( crs.getBlob( colNum )) ;
	}

	public Blob getBlob( String colName ) throws SQLException
	{
		return (crs.getBlob( colName )) ;
	}

	/**
	 *	Boolean 的geter 方法。
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( crs.getBoolean( colNum) ) ;
	}

	public boolean getBoolean( String colName) throws SQLException
	{
		return ( crs.getBoolean( colName ) ) ;
	}


	/**
	 *	Byte 的geter 方法。
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( crs.getByte( colName ) ) ;
	}


	/**
	 *	Byte[] 的geter 方法。
	 */
	public byte[] getBytes( int colNum ) throws SQLException
	{
		return ( crs.getBytes( colNum ) ) ;
	}
	public byte[] getBytes( String colName )  throws SQLException
	{
		return ( crs.getBytes( colName ) ) ;
	}

	/**
	 *	CharacterStream 的geter 方法。

	public CharacterStream getCharacterStream( int colNum)
	{
		return ( crs.getCharacterStream( colNum) ) ;
	}

	public CharacterStream getCharacterStream( String colName)
	{
		return ( crs.getCharacterStream( colName) ) ;
	}
	*/


	/**
	 *	Clob 的geter 方法。
	 */
	public Clob getClob( int colNum) throws SQLException
	{
		return ( crs.getClob( colNum ) ) ;
	}
	public Clob getClob ( String colName)  throws SQLException
	{
		return ( crs.getClob( colName ) ) ;
	}

	/**
	 *	Date 的geter 方法。
	 */
	public java.util.Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}

	public java.util.Date getDate( String colName) throws SQLException
	{
		return ( crs.getDate( colName ) ) ;
	}

	/**
	 *	Double 的geter 方法。
	 */
	public double getDouble( int colNum ) throws SQLException
	{
		return ( crs.getDouble( colNum ) ) ;
	}
	public double getDouble( String colName ) throws SQLException
	{
		return ( crs.getDouble( colName ) ) ;
	}

	/**
	 *	Double 的geter 方法。
	 */
	public float getFloat( int colNum) throws SQLException
	{
		return ( crs.getFloat( colNum ) ) ;
	}
	public float getFloat( String colName) throws SQLException
	{
		return ( crs.getFloat( colName ) ) ;
	}

	/**
	 *	Integer 的geter 方法。
	 */
	public int getInt( int colNum) throws SQLException
	{
		return ( crs.getInt( colNum )) ;
	}
	public int getInt( String colName) throws SQLException
	{
		return ( crs.getInt( colName )) ;
	}

	/**
	 *	Long 的geter 方法。
	 */
	public long getLong( int colNum ) throws SQLException
	{
		return ( crs.getLong( colNum ) ) ;
	}
	public long getLong( String colName ) throws SQLException
	{
		return ( crs.getLong( colName ) ) ;
	}

	/**
	 *	Object 的geter 方法。
	 */
	public Object getObject( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object getObject( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}

	/**
	 *	抽象的 的geter 方法。
	 */
	public Object get( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object get( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}


	/**
	 *	Short 的geter 方法。
	 */
	public short getShort( int colNum ) throws SQLException
	{
		return ( crs.getShort( colNum )) ;
	}
	public short getShort( String colName )  throws SQLException
	{
		return ( crs.getShort( colName ) ) ;
	}

	/**
	 *	String 的geter 方法。
	 */
	public String getString( int colNum) throws SQLException
	{
		return ( crs.getString( colNum ) ) ;
	}
	public String getString( String colName ) throws SQLException
	{
		return ( crs.getString( colName ) ) ;
	}

	/**
	 *	Time 的geter 方法。
	 */
	public Time getTime( int colNum ) throws SQLException
	{
		return ( crs.getTime( colNum ) ) ;
	}
	public Time getTime( String colName ) throws SQLException
	{
		return ( crs.getTime( colName ) ) ;
	}

	/**
	 *	Timestamp 的geter 方法。
	 */
	public Timestamp getTimestamp( int colNum ) throws SQLException
	{
		return ( crs.getTimestamp( colNum ) ) ;
	}

	public Timestamp getTimestamp( String colName ) throws SQLException
	{
		return ( crs.getTimestamp( colName ) ) ;
	}

	//说明：其他的get方法需要再添加。


	/**
	 *	返回总的行数
	 */
	public int size()
	{
		return ( crs.size() ) ;
	}
	/**
	* 分页
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pages(String commandName)
	{
	    return pages(getPageSize(), commandName);
	}

	/**
	* 分页
	* @param int rows 每页显示的最大行数
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pages(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(行"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",页"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb
	    	.append("<td nowrap align=\"center\" width=\"7%\"><img name=\"selectall\" src=\"")
	        .append(imgPath)
	        .append("selectallno.gif\" ")
	    	.append("onclick=\"javascript:selectAll()\"></td>") ;
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirst.gif")
	            .append("' title='首页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirstno.gif")
	            .append("' title='首页")
	            .append(title)
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='下一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelast.gif")
	            .append("' title='末页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelastno.gif")
	            .append("' title='末页")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("</td>") ;

	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("共<b>")
	        .append(getPageCount())
	        .append("</b>页，第<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>页")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;共<b>")
	        .append(getRowCount())
	        .append("</b>条,从<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>条到<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>条")
	        .append("</td>");   //getShowRows()改为 getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;转到第<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >页")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	/**
	* 分页
	* @param int rows 每页显示的最大行数
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pagesPN(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(行"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",页"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='下一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("共<b>")
	        .append(getPageCount())
	        .append("</b>页，第<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>页")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;共<b>")
	        .append(getRowCount())
	        .append("</b>条,从<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>条到<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>条")
	        .append("</td>");   //getShowRows()改为 getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;转到第<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >页")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	/**
	* 分页
	* @param int rows 每页显示的最大行数
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pagesPNFL(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(行"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",页"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirst.gif")
	            .append("' title='首页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagefirstno.gif")
	            .append("' title='首页")
	            .append(title)
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageup.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pageupno.gif")
	            .append("' title='上一页")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\"><img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedown.gif")
	            .append("' title='下一页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='NextArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagedownno.gif")
	            .append("'>");
	    }
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\"><img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelast.gif")
	            .append("' title='末页")
	            .append(title)
	            .append("'></a>");
	    }
	    else
	    {
	        sb
	            .append("<img name='PreviousArrow' border='0' src='")
	            .append(imgPath)
	            .append("pagelastno.gif")
	            .append("' title='末页")
	            .append(title)
	            .append("'>");
	    }
	    sb.append("</td>") ;

	    sb
	        .append("<td valign=\"bottom\" nowrap>")
	        .append("共<b>")
	        .append(getPageCount())
	        .append("</b>页，第<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>页")
	        .append("</td>");
	    sb
	        .append("<td valign=bottom nowrap>")
	        .append("&nbsp;共<b>")
	        .append(getRowCount())
	        .append("</b>条,从<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>条到<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>条")
	        .append("</td>");   //getShowRows()改为 getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td valign=\"bottom\" nowrap>")
	            .append("&nbsp;转到第<input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\" >页")
	            .append("<img name='Image23' border='0' src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("' width='20' height='20' align='absbottom'  style='cursor:hand' onmouseout=\"this.src='")
	            .append(imgPath)
	            .append("go.gif")
	            .append("'\" onmouseover=\"this.src='")
	            .append(imgPath)
	            .append("go_h.gif")
	            .append("'\" onclick=\"javascript:location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + document.getElementById('")
	            .append(id)
	            .append("').value; \">")
	            .append("</td>");
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


	//add wuzewen


	/**
	* 分页
	* @param int rows 每页显示的最大行数
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pagesPn(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(行"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",页"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr valign=\"center\"><td></td>");
	    //
	    sb.append("<td nowrap valign=\"center\" align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\">[上一页]</a>");
	    }
	    else
	    {
	        sb
	            .append("[上一页]");
	    }
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\">[下一页]</a>");
	    }
	    else
	    {
	        sb
	            .append("[下一页]");
	    }
	    //
	    sb
	        .append("</td><td nowrap>")
	        .append("&nbsp;&nbsp;共<b>")
	        .append(getPageCount())
	        .append("</b>页，第<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>页")
	        .append("</td>");
	    sb
	        .append("<td nowrap>")
	        .append("&nbsp;共<b>")
	        .append(getRowCount())
	        .append("</b>条，从<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>条到<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>条")
	        .append("</td>");   //getShowRows()改为 getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td align=right valign=\"center\" nowrap>")
	            .append("&nbsp;跳到第</td><td><input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\"></td>")
	            .append("<td>页</td>")
	            .append("<td><input type=button onclick=\"javascript:")
	            .append("((document.getElementById('")
	            .append(id)
	            .append("').value==''||document.getElementById('")
	            .append(id)
	            .append("').value==")
	            .append(currentPageIndex)
	            .append(")?(exit):(location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + ((document.getElementById('")
	            .append(id)
	            .append("').value=='')?")
	            .append(currentPageIndex)
	            .append(":")
	            .append("document.getElementById('")
	            .append(id)
	            .append("').value)))\" value=查看 class=input></td>") ;
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}



	/**
	* 分页
	* @param int rows 每页显示的最大行数
	* @param java.lang.String commandName Command的URL
	* @return java.lang.String
	*/
	public String pagesPnfl(int rows, String commandName)
	{
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
	    pageSize = rows;
	    pageCount = (getRowCount() - 1) / rows + 1;
	    String title =
	        "(行"
	            + ((currentPageIndex - 1) * pageSize + 1)
	            + "/"
	            + getRowCount()
	            + ",页"
	            + getCurrentPageIndex()
	            + "/"
	            + getPageCount()
	            + ")";
	    StringBuffer sb = new StringBuffer();
	    sb.append("<table><tr><td valign=\"bottom\"></td>");
	    //
	    sb.append("<td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isFirstPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=1")
	            .append("\">[首 页]</a>");
	    }
	    else
	    {
	        sb
	            .append("[首 页]");
	    }
	    //
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasPreviousPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex - 1))
	            .append("\">[上一页]</a>");
	    }
	    else
	    {
	        sb
	            .append("[上一页]");
	    }
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (hasNextPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append(String.valueOf(currentPageIndex + 1))
	            .append("\">[下一页]</a>");
	    }
		else
		{
		    sb
		        .append("[下一页]");
		}
	    //
	    sb.append("</td><td nowrap align=\"center\" width=\"5%\">") ;
	    if (!isLastPage())
	    {
	        sb
	            .append("<a href=\"")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=")
	            .append( String.valueOf( getPageCount() ) )
	            .append("\">[末 页]</a>");
	    }
	    else
	    {
	        sb
	            .append("[末 页]");
	    }
	    //
	    sb
	        .append("</td><td valign=\"center\" nowrap>")
	        .append("&nbsp;&nbsp;共<b>")
	        .append(getPageCount())
	        .append("</b>页，第<b>")
	        .append(getCurrentPageIndex())
	        .append("</B>页")
	        .append("</td>");
	    sb
	        .append("<td valign=\"center\" nowrap>")
	        .append("&nbsp;共<b>")
	        .append(getRowCount())
	        .append("</b>条，从<b>")
	        .append((currentPageIndex - 1) * pageSize + 1)
	        .append("</b>条到<b>")
	        .append( (((currentPageIndex - 1) * pageSize + getPageSize())<crs.size())?((currentPageIndex - 1) * pageSize + getPageSize()):crs.size())
	        .append("</b>条")
	        .append("</td>");   //getShowRows()改为 getPageSize()

	    if (pageCount > 1)
	    {
	        String id = commandName + "pageNumber";
	        sb
	            .append("<td align=right valign=\"center\" nowrap>")
	            .append("&nbsp;跳到第</td><td><input id='")
	            .append(id)
	            .append("' name=\"pageNumber\" id=\"toPage\" size=\"4\"></td>")
	            .append("<td>页</td>")
	            .append("<td><input type=button onclick=\"javascript:")
	            .append("((document.getElementById('")
	            .append(id)
	            .append("').value==''||document.getElementById('")
	            .append(id)
	            .append("').value==")
	            .append(currentPageIndex)
	            .append(")?(exit):(location.href='")
	            .append(commandName)
	            .append((commandName.indexOf("?")>0)?"&":"?")
	            .append("Action=")
	            .append(action)
	            .append("&pageNumber=' + ((document.getElementById('")
	            .append(id)
	            .append("').value=='')?")
	            .append(currentPageIndex)
	            .append(":")
	            .append("document.getElementById('")
	            .append(id)
	            .append("').value)))\" value=查看 class=input></td>") ;
	    }
	    sb.append("</tr></table>");
	    return sb.toString();
	}


}
