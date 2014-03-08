/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.bean;

import java.io.Serializable;
import java.util.HashMap;

/** 
 * <p>采用接口编程，所有可导航器对象实现本接口定义的导航方法，可被NavigatorTag 标签使用。</p>
 * @author	WUZEWEN on 2006.11.01
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */
public interface Navigator extends Serializable {
	
//	/**
//	 *  基本构造函数。
//	 * @param pageParams
//	 */
//	public Pagination(int[] pageParams) {
//		int rowCount = pageParams[0];
//		int pageSize = pageParams[1];
//		int pageNumber = pageParams[2];
//		
//		this.setPageSize( pageSize );
//		this.setCurrentPageIndex( pageNumber );
//		this.setRowCount(rowCount);
//		this.setPageCount( (rowCount - 1) / pageSize + 1 );
//	}


	
	/**
	 * 总页数
	 * @deprecated wzw：pageCount由rowCount/pageSize计算出来，不需要设置。
	 * @return int
	 */
	public void setPageCount(int pageCount);
	
	/**
	 * 总页数
	 * @return int
	 */
	public int getPageCount();
	
	
	/**
	 * 每页显示的行数
	 * @return int
	 */
	public int getPageSize();
	
   
	public void setPageSize(int pageSize);
	
	/**
	 * 总的行数
	 * @return int
	 */
	public int getRowCount();
	
	/**
	 * 基本地址
	 * @return
	 */
	public String getPath();

	public void setRowCount(int rowCount);
	
	public void setPath(String path);
	
	/**
	 *当前页号
	 */
	public void setPageNumber(int index);
	
	/**
	 * 当前页
	 * @return int
	 */
	public int getPageNumber();
	
	/**
	 * 判断是否有下页
	 * @return boolean
	 */
	public boolean hasNextPage();

	/**
	 * 判断是否有上页
	 * @return boolean
	 */
	public boolean hasPreviousPage();

	/**
	 * 判断是第一页
	 * @return boolean
	 */
	public boolean isFirstPage();
	
	/**
	 * 判断是否是最后一页
	 * @return boolean
	 */
	public boolean isLastPage();
	
//
//	/**
//	 * @deprecated 不再传递每页的行数。
//	 * <p>不采用Web服务器缓存，每次翻页都查找数据库，
//	 * 		针对不同的数据如Oracle,SQL Server采用不同的封装实现。
//	 * 	wzw on 2006-11-28 将onclick事件中的exit修改为'',使用return 也不行。</p>
//	 * @param commandName 翻页时用的的URL，后面带有翻页信息参数和其他需要的参数
//	 * @param rows 每页显示的行数
//	 * @return 查询导航信息html代码
//	 */
//	
//	public String pagesPnfl(int rows,String commandName);  //兼容旧版本
//	

	/**
	 * <p>翻页导航信息的输出，所有需要翻页的对象都实现该接口，
	 * 		本方法输出 Privious,Next,First,Last, toPage, 五个导航链接。</p>
	 * @param commandName 翻页时用的的URL，后面带有翻页信息参数和其他需要的参数
	 * @return 查询导航信息html代码
	 */
	public String getPagesPnfl( );
	public String getPagesPnfl2( ); 
 

	/**
	 * 分页
	 * @param int rows 每页显示的最大行数
	 * @param java.lang.String commandName Command的URL
	 * @return java.lang.String
	 */
	public String getPagesPn( );


	public int getOffset();

	public void setRowCount(Integer rowCount);
		
}
