/*
 * @(#)Page.java		    1.00 2004/04/13
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
 * @version		1.00 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */

import javax.sql.RowSet;

public class Page {
	
	private int start;
	private int end;
	private int count;
	private int pageSize;
	private int pageCount;
	private RowSet crs;
	
	public Page(RowSet crs, int pageSize, int start, int end, int count) 
	{
		this.start = start;
		this.end = end;
		this.pageSize = pageSize;
		//this.count = crs.size() ;   //Maybe is zero.
		this.crs = crs;
		//.out.print("\nthe rows account is : "+ count) ;
		//this.pageCount = (count - 1) / pageSize + 1;
		//.out.print("\nthe pageCount account is : "+ this.pageCount) ;
	}
	
    public int getCount()
    {
        return count;    
    }
    
	public RowSet getDatas() {		//Cached
    	return crs;
	}
	public int getPageCount() {
    	return pageCount;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setDatas(RowSet crs) {	//Cached
		this.crs = crs;
	}

}