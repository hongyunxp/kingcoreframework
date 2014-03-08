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

package com.kingcore.framework.bean.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.kingcore.framework.bean.Navigator;

/**
 * <p>web 页面使用的导航翻页对象，支持getter,在页面可使用标签获取。</p>
 * @author Zeven on 2008-10-2
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class WebPageNavigator implements Navigator {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 日志变量
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.impl.WebPageNavigator.class);
	
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
	 * 分页标记
	 */
	protected boolean isPaged;
	
	/**
	 * 
	 */
	private static int nvlCount=6;
	
	/**
	 *  wzw 初始导航栏的所有信息
	 * @param pageParams
	 */
	public WebPageNavigator(int[] pageParams, String path) {

		int rowCount = pageParams[0];
		int pageSize = pageParams[1];
		int pageNumber = pageParams[2];
		
		if(pageSize<1 || pageNumber<1 || rowCount<0){
			log.fatal("参数初始失败，请先调用 getPageParameter、getNavigableDataSet 两个方法之后调用本方法");
		}
		
		this.setPageSize( pageSize );
		this.setPageNumber(pageNumber);
		//this.setCurrentPageIndex( pageNumber );
		this.setRowCount(rowCount);
		this.setPageCount( (rowCount - 1) / pageSize + 1 );
		this.setPath(path);
		 
		doinit() ;
	}

	private WebPageNavigator(){
		
	}


	/**
	 * 初始部分信息。
	 *
	 */
	private void doinit()
	{
		this.beginIndex = 0;
		this.endIndex = getPageSize() - 1;
		if( this.rowCount<0){
			this.rowCount = 0;
		}
	}

	public String getPagesPn() {
		if(getRowCount()<1){
			return "<div class='tab_bot_box'>当前没有记录！</div>";	//wzw on 2006-11-15, 如果一条数据都没有，不输出导航信息。 
		}
		
		String commandName = this.path;
		//log.debug("pagesPnfl )))))))) "+ getPageCount() );
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		pageSize = getPageSize();	//;rows;
		//pageCount = (getRowCount() - 1) / rows + 1;
//		String title =
//			"(行"
//			+ ((currentPageIndex - 1) * pageSize + 1)
//			+ "/"
//			+ getRowCount()
//			+ ",页"
//			+ getPageNumber()
//			+ "/"
//			+ getPageCount()
//			+ ")";

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(rowCount)
			.append("&pageSize=")
			.append(pageSize);
			
		StringBuffer sb = new StringBuffer();

		sb.append( "<div class='tab_bot_box'>");
		
//		sb.append( "<div class='tab_bot_box'>")
//			.append("<span class='list_all'>")			
//			.append("第")
//			.append(getPageNumber())
//			.append("/")
//			.append(getPageCount())
//			.append("页，")
//			.append((currentPageIndex - 1) * pageSize + 1)
//			.append("-")
//			.append( (((currentPageIndex - 1) * pageSize + getPageSize())<this.rowCount)?((currentPageIndex - 1) * pageSize + getPageSize()):this.rowCount)
//			.append("，共")
//			.append(getRowCount())
//			.append("条")	
//			.append("</span>");
			
//		if (!isFirstPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=1")
//			.append("\">1</a>");	//首 页
//		}
//		else
//		{
//			sb.append("<span >首 页</span>");
//		}
		//
		if (hasPreviousPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex - 1))
			.append("\" class=\"page_turn\">&lt; 上一页</a>");
		}
		else
		{
			sb
			.append("<span class=\"no_page_turn\">&lt; 上一页</span>"); 
		}

//		// ...234567891011...
//		int fpi = currentPageIndex- nvlCount/2;
//		if(fpi>=pageCount-nvlCount){
//			fpi=pageCount-nvlCount;
//		}
//		if(fpi<1){
//			fpi=1;
//		}
//		if(fpi>1){
//			
//			if(1==currentPageIndex){
//				sb
//				.append( 1 );
//			}else{
//				sb
//				.append( pubHref )
//				.append("&pageNumber=1")
//				.append("\">1</a>");
//				
//			}
//			
//			if(fpi>2){
//				sb
//				.append("...");
//			}
//			
//		}
		
//		int iCnt=0;
//		while(iCnt< nvlCount && fpi<=pageCount){
//			if(fpi==currentPageIndex){
//				sb
//				.append("<span class='list_on_page'>"+ fpi++ +"</span>");
//				
//			}else{
//					
//				sb
//				.append( pubHref )
//				.append("&pageNumber=")
//				.append( fpi )
//				.append("\">"+ fpi++ +"</a>");
//			}
//			
//			iCnt++;
//		}
//		if(pageCount>=fpi){
//			if(pageCount>fpi){
//				sb
//				.append("...");
//			}
//			
//			if(pageCount==currentPageIndex){
//				sb
//				.append( pageCount );
//			}else{
//				sb
//				.append( pubHref )
//				.append("&pageNumber=")
//				.append( String.valueOf( getPageCount() ) )
//				.append("\">"+pageCount+"</a>");
//			}
//		}
		
		//
		if (hasNextPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex + 1))
			.append("\" class=\"page_turn\">下一页&gt;</a>");
		}
		else
		{
			sb
			.append("<span class=\"no_page_turn\">下一页&gt;</span>");
		}

		//
//		if (!isLastPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=")
//			.append( String.valueOf( getPageCount() ) )
//			.append("\">"+pageCount+"</a>");
//		}
//		else
//		{
//			sb
//			.append("<span >末 页</span>");
//		}
 
		
//		if (pageCount > 1 && false)
//		{
//			String id = "toPage";	//commandName + "pageNumber";
//				//name=\"pageNumber\" id=\"toPage\" 
//			sb
//			.append("<td align=right valign=\"center\" nowrap>")
//			.append("&nbsp;跳到第</td><td><input id='")
//			.append(id)
//			.append("' size=\"4\"></td>")
//			.append("<td>页</td>")
//			.append("<td><input type=button onclick=\"javascript:")
//			.append("((document.getElementById('")
//			.append(id)
//			.append("').value==''||document.getElementById('")
//			.append(id)
//			.append("').value==")
//			.append(currentPageIndex)
//			.append(")?(''):(location.href='")
//			.append(commandName)
//			.append((commandName.indexOf("?")>0)?"&":"?")
//			.append("Action=")
//			.append(action)
//			.append("&pageSize=")
//			.append(pageSize)
//			.append("&pageNumber=' + ((document.getElementById('")
//			.append(id)
//			.append("').value=='')?")
//			.append(currentPageIndex)
//			.append(":")
//			.append("document.getElementById('")
//			.append(id)
//			.append("').value)))\" value=查看 class=input></td>") ;
//		}
		
		sb.append("</div>");
		return sb.toString(); 
	}

	
//  // -----------------------  放入到基类中
//	/**
//	 * 判断是否有下页
//	 * @return boolean
//	 */
//	public boolean hasNextPage()
//	{
//		//log.debug(currentPageIndex+ "  "+  pageCount );
//		//log.debug(currentPageIndex+ "  "+  getPageCount() );
//		if ( this.currentPageIndex < getPageCount() )
//		{
//			return true;
//		}
//		return false;
//	} 
//
//	/**
//	 * 判断是否有上页
//	 * @return boolean
//	 */
//	public boolean hasPreviousPage()
//	{
//		if ( this.currentPageIndex > 1 )
//		{
//			return true;
//		}
//		return false;
//	}
//	

	
	/**
	* <p>不采用Web服务器缓存，每次翻页都查找数据库，
	* 		针对不同的数据如Oracle,SQL Server采用不同的封装实现。
	* 	wzw on 2006-11-28 将onclick事件中的exit修改为'',使用return 也不行。</p>
	* @param commandName 翻页时用的的URL，后面带有翻页信息参数和其他需要的参数
	* @return 查询导航信息html代码
	*/
	public String getPagesPnfl( )
	{
		if(getRowCount()<1){
			return "<div class='tab_bot_box'>当前没有记录！</div>";	//wzw on 2006-11-15, 如果一条数据都没有，不输出导航信息。 
		}
		
		String commandName = this.path;
		//log.debug("pagesPnfl )))))))) "+ getPageCount() );
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		pageSize = getPageSize();	//;rows;
		//pageCount = (getRowCount() - 1) / rows + 1;
		String title =
			"(行"
			+ ((currentPageIndex - 1) * pageSize + 1)
			+ "/"
			+ getRowCount()
			+ ",页"
			+ getPageNumber()
			+ "/"
			+ getPageCount()
			+ ")";

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(rowCount)
			.append("&pageSize=")
			.append(pageSize);
			
		StringBuffer sb = new StringBuffer();
		
		sb.append( "<div class='tab_bot_box'>")
			.append("<span class='list_all'>")			
			.append("第")
			.append(getPageNumber())
			.append("/")
			.append(getPageCount())
			.append("页，")
			.append((currentPageIndex - 1) * pageSize + 1)
			.append("-")
			.append( (((currentPageIndex - 1) * pageSize + getPageSize())<this.rowCount)?((currentPageIndex - 1) * pageSize + getPageSize()):this.rowCount)
			.append("，共")
			.append(getRowCount())
			.append("条")	
			.append("</span>");
			
//		if (!isFirstPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=1")
//			.append("\">1</a>");	//首 页
//		}
//		else
//		{
//			sb.append("<span >首 页</span>");
//		}
		//
		if (hasPreviousPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex - 1))
			.append("\">&lt; 上一页</a>");
		}
		else
		{
			sb
			.append("<span >&lt; 上一页</span>"); 
		}

		// ...234567891011...
		int fpi = currentPageIndex- nvlCount/2;
		if(fpi>=pageCount-nvlCount){
			fpi=pageCount-nvlCount;
		}
		if(fpi<1){
			fpi=1;
		}
		if(fpi>1){
			
			if(1==currentPageIndex){
				sb
				.append( 1 );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=1")
				.append("\">1</a>");
				
			}
			
			if(fpi>2){
				sb
				.append("...");
			}
			
		}
		
		int iCnt=0;
		while(iCnt< nvlCount && fpi<=pageCount){
			if(fpi==currentPageIndex){
				sb
				.append("<span class='list_on_page'>"+ fpi++ +"</span>");
				
			}else{
					
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( fpi )
				.append("\">"+ fpi++ +"</a>");
			}
			
			iCnt++;
		}
		if(pageCount>=fpi){
			if(pageCount>fpi){
				sb
				.append("...");
			}
			
			if(pageCount==currentPageIndex){
				sb
				.append( pageCount );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( String.valueOf( getPageCount() ) )
				.append("\">"+pageCount+"</a>");
			}
		}
		
		//
		if (hasNextPage())
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf(currentPageIndex + 1))
			.append("\">下一页&gt;</a>");
		}
		else
		{
			sb
			.append("<span >下一页&gt;</span>");
		}

//
//		if (!isLastPage())
//		{
//			sb
//			.append( pubHref )
//			.append("&pageNumber=")
//			.append( String.valueOf( getPageCount() ) )
//			.append("\">"+pageCount+"</a>");
//		}
//		else
//		{
//			sb
//			.append("<span >末 页</span>");
//		}
 
		
		// 要求页数大于1才会出现“跳转到...”功能
		// 执行请求的条件： 
		//      > 填写的也号 必须在1->总页数的之间，并且不能等于当前页号
		if (pageCount > 1 )
		{
			String id = "toPage";	//commandName + "pageNumber";
				//name=\"pageNumber\" id=\"toPage\" 
			sb
			.append("<span>跳转到")
			.append( pubHref )
			.append("\" id=\"toPageHref\" style='display:none;'></a>")
			.append("<input type=\"text\" class=\"tab_bot_text\" id=\"")
			.append(id)
			.append("\"><input type=\"button\" class=\"tab_bot_button\" onclick=\"javaScript:")
			.append("if(!(document.getElementById('")
			.append(id)
			.append("').value==''||document.getElementById('")
			.append(id)
			.append("').value<1||document.getElementById('")
			.append(id)
			.append("').value>")
			.append( pageCount )
			.append("||document.getElementById('")
			.append(id)
			.append("').value==")
			.append(currentPageIndex)
			.append(")){document.getElementById('toPageHref').href+='&pageNumber='+")
			.append("document.getElementById('")
			.append(id)
			.append("').value+'&t='+")
			.append("new Date().getTime();if(typeof(navigatorClick)=='function'){navigatorClick(this);};window.location.href=document.getElementById('toPageHref').href;}\" value=\"GO\"></span>") ;
			//.append("new Date().getTime())?document.getElementById('toPageHref').click():''))\" value=\"GO\"></span>") ;
		}
		
		sb.append("</div>");
		return sb.toString(); 
	}

	

	/**
	* <p>分页导航信息。Zeven on 2009-07-31，另一种风格的导航代码。
	* 
	* 	效果如下：
	*************************************************************************************
			<div class="page_box">
                <a class="page_up" href="#"><span>上一页</span></a><!--没有上一页时，去掉href="#"，样式改为“page_up_unable”-->
                <a href="#">1</a>
                <span>2</span>
                <a href="#">3</a>
                <a href="#">4</a>
                <a href="#">5</a>
                <a href="#">6</a>
                <a href="#">7</a>
                <span>…</span>
                <a href="#">30</a>
                <a href="#">31</a>
                <a class="page_down_unable"><span>下一页</span></a><!--没有下一页时，去掉href="#"，样式改为“page_down_unable”-->
                <span>共30页,到</span>
                <input class="page_text" name="" type="text" />
                <span>页</span>
                <input class="button" name="" type="button" value="确定" />
            </div>
	*************************************************************************************
	* 
	* 	</p>
	* @param rows 每页显示的最大行数
	* @param commandName Command的URL
	* @param nvlCount 导航中间同时显示的链接数，默认可以6
	* @return 查询导航信息html代码
	*/
	public String getPagesPnfl2( )
	{
		return getPagesPnfl(this.path, this.getRowCount(), this.getPageSize(),
				nvlCount, this.currentPageIndex ); 
	}
	
	/**
	 * <p>本方法具体生成导航的html代码，只使用参数，不使用任何成员对象，
	 *       可以很拷贝移植到js中。</p>
	 * @param p_path
	 * @param p_rowCount
	 * @param p_pageSize
	 * @param p_nvlCount
	 * @param p_currentPageIndex
	 * @return
	 */
	protected String getPagesPnfl(String p_path, int p_rowCount, int p_pageSize, 
			 int p_nvlCount, int p_currentPageIndex) {
		
		if(p_rowCount<1){  //getRowCount()
			return "<div class='page_box'>当前没有记录！</div>"; 
		}
		int t_pageCount = (p_rowCount - 1) / p_pageSize + 1;
		String commandName = p_path;  //this.path
		String action = "changepage"; //QueryAction.CHANGE_PAGE;
		// pageSize = getPageSize();	//;rows;

		StringBuffer pubHref = new StringBuffer();
		pubHref
			.append("<a onclick=\"javaScript:if(typeof(navigatorClick)=='function'){navigatorClick(this);}\" href=\"")
			.append(commandName)
			.append((commandName.indexOf("?")>0)?"&":"?")
			.append("Action=")
			.append(action)
			.append("&rowCount=")
			.append(p_rowCount)
			.append("&pageSize=")
			.append(p_pageSize);
			
		StringBuffer sb = new StringBuffer();
		
		sb.append( "<div class='page_box'>");

		boolean t_hasPreviousPage = p_currentPageIndex>1?true:false;
		if ( t_hasPreviousPage )
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf( p_currentPageIndex - 1))
			.append("\" class='page_up'><span>上一页</span></a>");
		}
		else
		{
			sb
			.append("<a class='page_up_unable'><span>上一页</span></a>"); 
		}

		// ...234567891011...
		int fpi = p_currentPageIndex- p_nvlCount/2;
		if(fpi>=t_pageCount- p_nvlCount){
			fpi=t_pageCount- p_nvlCount;
		}
		if(fpi<1){
			fpi=1;
		}
		if(fpi>1){
			
			if(1== p_currentPageIndex){
				sb
				.append( "<span class='current_page'>1</span>" );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=1")
				.append("\">1</a>");
			}
			
			if(fpi>2){
				sb
				.append("<span>...</span>");
			}
			
		}
		
		int iCnt=0;
		while(iCnt< p_nvlCount && fpi<= t_pageCount){
			if(fpi== p_currentPageIndex){	// 当前页
				sb
				.append("<span class='current_page'>"+ fpi++ +"</span>");
				
			}else{
					
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( fpi )
				.append("\">"+ fpi++ +"</a>");
			}
			
			iCnt++;
		}
		if( t_pageCount>=fpi){
			if( t_pageCount>fpi){
				sb
				.append("<span>...</span>");
			}
			
			if( t_pageCount==p_currentPageIndex){  // 当前页
				sb
				.append( "<span class='current_page'>"+t_pageCount+"</span>" );
			}else{
				sb
				.append( pubHref )
				.append("&pageNumber=")
				.append( String.valueOf( t_pageCount ) )
				.append("\">"+ t_pageCount+"</a>");
			}
		}
		
		//
		boolean t_hasNextPage = p_currentPageIndex<t_pageCount?true:false;
		if ( t_hasNextPage )
		{
			sb
			.append( pubHref )
			.append("&pageNumber=")
			.append(String.valueOf( p_currentPageIndex + 1))
			.append("\" class='page_down'><span>下一页</span></a>");
		}
		else
		{
			sb
			.append("<a class='page_down_unable'><span>下一页</span></a>");
		}
		
		//<span>共30页,到</span>
		if ( t_pageCount > 1 ){
			sb.append("<span>共"+ t_pageCount+"页,到</span>");
		}else{
			sb.append("<span>共"+ t_pageCount+"页</span>");
		
		}
		
		// 要求页数大于1才会出现“跳转到...”功能
		// 执行请求的条件： 
		//      > 填写的也号 必须在1->总页数的之间，并且不能等于当前页号
		if ( t_pageCount > 1 )
		{
			String id = "toPage";	//commandName + "pageNumber";
				//name=\"pageNumber\" id=\"toPage\" 
			sb
			//.append("<span>跳转到")
			.append( pubHref )
			.append("\" id=\"toPageHref\" style='display:none;'></a>")
			.append("<input type=\"text\" class=\"page_text\" id=\"")
			.append(id)
			.append("\"><span>页</span><input type=\"button\" class=\"button\" onclick=\"javaScript:")
			.append("if(!(document.getElementById('")
			.append(id)
			.append("').value==''||document.getElementById('")
			.append(id)
			.append("').value<1||document.getElementById('")
			.append(id)
			.append("').value>")
			.append( t_pageCount )
			.append("||document.getElementById('")
			.append(id)
			.append("').value==")
			.append( p_currentPageIndex)
			.append(")){document.getElementById('toPageHref').href+='&pageNumber='+")
			.append("document.getElementById('")
			.append(id)
			.append("').value+'&t='+")
			.append("new Date().getTime();if(typeof(navigatorClick)=='function'){navigatorClick(this);};window.location.href=document.getElementById('toPageHref').href;}\" value=\"确定\">") ; //</span>
			//.append("new Date().getTime())?document.getElementById('toPageHref').click():''))\" value=\"GO\"></span>") ;
		}
		
		sb.append("</div>");
		return sb.toString(); 
	}
	
//	/**
//	 *  基本构造函数。
//	 * @param pageParams
//	 */
//	public Pagination(int[] pageParams) {
//	}
	
	//------------------------------------------------- 
	/**
	 * 总行数
	 */
	protected int rowCount = -1;
	
	/**
	 * 每页显示的行数 默认值
	 */
	protected int pageSize = 20;
	
	/**
	 * 总页数
	 */
	protected int pageCount;

	/**
	 *当前页号
	 */
	protected int currentPageIndex = 1;

	/**
	 * 导航链接信息
	 */
	protected String path = null;
	

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		
	}


	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
		
	}


	/**
	 *  设置每页显示的行数。
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		
		if(pageSize>120) {	// 设置最大值，防止攻击行为。	
			pageSize = 120;
		}
		this.pageSize = pageSize;
		
	}

	public int getPageSize() {
		return this.pageSize;
	}

	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getPageCount() {
		return this.pageCount;
	}


	public void setPageNumber(int pageNumber) {
		this.currentPageIndex = pageNumber;
		
	}
	
	public int getPageNumber() {
		return this.currentPageIndex;
	}
	
	/**
	 * 判断是否有下页
	 * @return boolean
	 */
	public boolean hasNextPage()
	{
		//log.debug(currentPageIndex+ "  "+  pageCount );
		//log.debug(currentPageIndex+ "  "+  getPageCount() );
		if ( this.currentPageIndex < getPageCount() )
		{
			return true;
		}
		return false;
	} 

	/**
	 * 判断是否有上页
	 * @return boolean
	 */
	public boolean hasPreviousPage()
	{
		if ( this.currentPageIndex > 1 )
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断是第一页
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		if ( this.currentPageIndex == 1)
		{
			return true;
		}
		return false;
		
	}
	/**
	 * 判断是否是最后一页
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		if ( this.currentPageIndex == getPageCount() )
		{
			return true;
		}
		return false;
	}
	
	// ----------------------------------------------------------


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 注意：接口里面的成员对象，是final类型，不能被修改的。
//		Pagination wng = new WebPageNavigator();
//		System.out.println("=="+wng.getPageNumber() );
//		wng.setPageNumber( wng.getPageNumber()+2 );
//		System.out.println("=="+wng.getPageNumber());
//		
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getOffset() {
		return 0;
	}

	public void setRowCount(Integer rowCount) {
		// TODO Auto-generated method stub
		
	}

}
