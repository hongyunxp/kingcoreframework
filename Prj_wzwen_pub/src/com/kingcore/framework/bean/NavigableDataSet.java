/**
 * Copyright (C) 2006 ChangSha WangWin Science & Technology CO,.LTD. All rights reserved.
 * WangWin PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;

/**
 * <p>可以导航的数据集合接口，
 * 		 -- extends RowSet，如果要实现 RowSet 方法，需要在实现的类中添加 160 个左右的方法。</p>
 * @author Zeven on 2006-7-20
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface NavigableDataSet extends DataSet, Navigator{

	
	/**
	 * 设定图片的存放位置, the method cann't be static.
	 */
	public void setImgPath(String path);
	
	/**
	 * 取得图片存放位置
	 */
	public String getImgPath();

	
	/**
	 *设置primaryKey
	 */
	///public void setPrimaryKey(String key);
	
	/**
	 *获取primaryKey
	 */
	///public String getPrimaryKey();
	
	/**
	 * 返回开始行
	 * @return int
	 */
	public int getBeginIndex();
	

	/**
	 * 其中的一种使用List保存表格数据的方案。
	 */
	public List getDataList();
	public void setDataList(List dataList);
	
	/**
	 *得到所有的行集
	 * @return List
	 */
	public RowSet getDatas();

	/**
	 *得到抽象数据集对象，包括 dataList, crs。
	 * @return List
	 */
	public Object getDataObject();
	
	/**
	 *得到检索的最后一条记录号
	 * @return int
	 */
	public int getEndIndex();
	
	/**
	 * 返回是否是分页
	 * @return boolean
	 */
	public boolean getIsPaged();
	
	/**
	 * 返回最近一次执行的sql语句
	 * @return String
	 */
	public String getLastSql();
	
	public void setLastSql(String sql);
	
	public String getSelectString();
	
	public void setSelectString(String select);
	
	public String getFromString();
	
	public void setFromString(String from);
	
	public String getWhereString();
	
	public void setWhereString(String where);
	
	public String getGroupByString();
	
	public void setGroupByString(String groupBy);
	
	public String getHavingString();
	
	public void setHavingString(String having);
	
	public String getOrderByString();
	
	public void setOrderByString(String orderBy);

	
	/**
	 * 分页情况下每页实际显示的行数，该方法主要是在JSP中调用
	 * @return int
	 */
	public int getShowRows();
	
	/**
	 * 是否需要分页显示
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex);
	
	/**
	 * 转到下页
	 * @return void
	 */
	public void nextPage();
	
	/**
	 * 重置页面
	 */
	public void reIndex();
	
	public void resetCurrentPageIndex();
	
	/**
	 * 转到某页
	 * @param int pageIndex
	 * @return void
	 */
	public void turnToPage(int newPageIndex);
	
	
	
	/**
	 * 添加数据
	 * @parma page:Page
	 * @return void
	 */
	
 
	
//	private void doinit()
//	{
//		beginIndex = 0;
//		endIndex = pageSize - 1;
//		rowCount = 0;
//	}
	
	/**
	 * 添加数据
	 * @parma page:Page
	 * @return void
//	 */
//	public void addData(Page page);
	
	
	/**
	 * 添加数据
	 */
	
	public void setDataset( RowSet crs);
	
	public void setDataset( ResultSet rs) throws SQLException;
	
	
	/**
	 *	其它方法, 移动到DataSet中。
	 */

//	/**
//	 * 分页
//	 * @param java.lang.String commandName Command的URL
//	 * @return java.lang.String
//	 */
//	public String pages(String commandName);
//	
//	/**
//	 * 分页
//	 * @param int rows 每页显示的最大行数
//	 * @param java.lang.String commandName Command的URL
//	 * @return java.lang.String
//	 */
//	public String pages(int rows, String commandName);
//	
	
//	/**
//	 * 分页
//	 * @param int rows 每页显示的最大行数
//	 * @param java.lang.String commandName Command的URL
//	 * @return java.lang.String
//	 */
//	public String pagesPN(int rows, String commandName);
//	
//	
//	/**
//	 * 分页
//	 * @param int rows 每页显示的最大行数
//	 * @param java.lang.String commandName Command的URL
//	 * @return java.lang.String
//	 */
//	public String pagesPNFL(int rows, String commandName);
//	
//	
	//add wuzewen
	
	
}
