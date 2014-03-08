/*
 * @(#)QueryDataSet.java		    1.00 2004/04/13
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

 
/**
 * <p>实现了可序列化、可导航的接口。</p>
 * @version		1.00 2004.04.13
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */


public class QueryDataSet implements NavigableDataSet, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 日志变量
	 */
	public static Logger log = Logger.getLogger(com.kingcore.framework.bean.QueryDataSet.class);
	
	
//	/**
//	 * 请求的uri字符串，比如 /user/userDeal.jhtml?Action=add&id=123
//	 * 可用于生成导航信息。
//	 */
//	protected String commandName = null; 
	
	
	/**
	 * 图片路径
	 */
	
	protected static String imgPath = "jsp/image/";
	
	/**
	 * 主键
	 */
	protected String primaryKey = "";
	
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
	 * 所有数据集合，支持3种 RowSet, List, DataSet(=RowSet and List)
	 */
	protected Object dataObject ;	
	protected RowSet crs ;
	protected RowSet datas ;
	private List dataList ;	// add by Zeven on 2008-08-16。纯粹保存List对象，如List<Bean>, List<Map>，对于dataList的操作，必须直接先获datList。
	

	protected Navigator pagination = null;
	
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


	private String path;


	private Integer rowCount = -1;
	
	/**
	 * 暂时不需要的变量
	 *protected boolean isUpdate; //数据修改、删除标记
	 *protected boolean isInsert; //数据增加标记
	 *protected boolean isDetail; //数据明细标记
	 */
	
	/**
	 * 其中的一种使用List保存表格数据的方案。
	 */
	public List getDataList() {
		return this.dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
	/**
	 * 设定图片的存放位置
	 */
	public void setImgPath(String path)
	{
		this.imgPath = path;
	}
	
	/**
	 * 取得图片存放位置
	 */
	public String getImgPath()
	{
		return this.imgPath;
	}
	
	/**
	 *设置primaryKey
	 */
	public void setPrimaryKey(String key)
	{
		this.primaryKey = key;
	}
	
	/**
	 *获取primaryKey
	 */
	public String getPrimaryKey()
	{
		return this.primaryKey;
	}
	
	/**
	 * 返回开始行
	 * @return int
	 */
	public int getBeginIndex()
	{
		return this.beginIndex;
	}
	
	/**
	 * 当前页
	 * @return int
	 */
	public int getPageNumber()
	{
		return this.pagination.getPageNumber();
	}
	
	/**
	 *得到所有的行集
	 * @return List<Map>,List<Bean>,RowSet,SqlRowSet
	 */
	public RowSet getDatas()
	{
		return this.crs;
	}
	
	/**
	 *得到检索的最后一条记录号
	 * @return int
	 */
	public int getEndIndex()
	{
		return this.endIndex;
	}
	
	/**
	 * 返回是否是分页
	 * @return boolean
	 */
	public boolean getIsPaged()
	{
		return this.isPaged;
	}
	/**
	 * 返回最近一次执行的sql语句
	 * @return String
	 */
	public String getLastSql()
	{
		//.out.print("QueryDataSet:getLastSql()  lastSql=" + lastSql);
		return this.lastSql;
	}
	public void setLastSql(String sql)
	{
		this.lastSql = sql;
	}
	public String getSelectString()
	{
		return this.selectString;
	}
	public void setSelectString(String select)
	{
		this.selectString = select;
	}
	public String getFromString()
	{
		return this.fromString;
	}
	public void setFromString(String from)
	{
		this.fromString = from;
	}
	public String getWhereString()
	{
		return this.whereString;
	}
	public void setWhereString(String where)
	{
		this.whereString = where;
	}
	public String getGroupByString()
	{
		return this.groupByString;
	}
	public void setGroupByString(String groupBy)
	{
		this.groupByString = groupBy;
	}
	public String getHavingString()
	{
		return this.havingString;
	}
	public void setHavingString(String having)
	{
		this.havingString = having;
	}
	public String getOrderByString()
	{
		return this.orderByString;
	}
	public void setOrderByString(String orderBy)
	{
		this.orderByString = orderBy;
	}
	
	/**
	 * 总页数
	 * @return int
	 */
	public void setPageCount(int pageCount)
	{
		this.pagination.setPageCount(pageCount);
	}

	/**
	 * 总页数, 计算放在Action基类中。
	 * @return int
	 */
	public int getPageCount()
	{
		return this.pagination.getPageCount();
	}
	
	/**
	 * 每页显示的行数
	 * @return int
	 */
	public int getPageSize()
	{
		return this.pagination.getPageSize();
	}


	/**
	 * 
	 */
	public void setPageSize(int pageSize) {
		this.pagination.setPageSize(pageSize);
	}
	
	/**
	 * 总的行数
	 * @return int
	 */
	public int getRowCount()
	{
		return this.pagination.getRowCount();
	}
	
	public void setRowCount(int rowCount) {
		this.pagination.setRowCount(rowCount);
	}
	
	/**
	 *当前页号
	 */
	public void setPageNumber(int index)
	{
		this.pagination.setPageNumber( index );
	}
	
	/**
	 * 获得当前行号
	 * @return int
	 */
	public int getRow()
	{
		try{
			return this.crs.getRow();
		}catch( SQLException e){
			log.error("\nDataSet -- getrow execption "+e.getMessage()) ;
		}
		return 0 ;
	}

	
	/**
	 * 分页情况下每页实际显示的行数，该方法主要是在JSP中调用
	 * @deprecated
	 * @return int
	 */
	public int getShowRows()
	{
		return this.pagination.getPageNumber()==getPageCount()?( getRowCount()-(getPageCount()-1)*getPageSize() ):getPageSize() ;
	}

	/**
	 * 判断是第一页
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		return this.pagination.isFirstPage();
		
	}
	
	/**
	 * 判断是否是最后一页
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		return this.pagination.isLastPage();
	}
	
	/**
	 * 判断是否有下页
	 * @return boolean
	 */
	public boolean hasNextPage()
	{		
		return this.pagination.hasNextPage();
	}
	
	/**
	 * 判断是否有上页
	 * @return boolean
	 */
	public boolean hasPreviousPage()
	{
		return this.pagination.hasPreviousPage();
	}
	
	/**
	 * 是否需要分页显示
	 * @return boolean
	 */
	public boolean isNeedPaged(int forPageIndex)
	{
		if (forPageIndex != this.pagination.getPageNumber() )  //this.currentPageIndex)
		{
			this.isPaged = true;
		}
		else
		{
			this.isPaged = false;
		}
		return this.isPaged;
	}
	
	/**
	 * 转到下页
	 * @return void
	 */
	public void nextPage()
	{
//		if ( this.currentPageIndex < this.pageCount)
//			this.currentPageIndex++;

		if ( this.pagination.getPageNumber() < this.pagination.getPageCount() ){
			this.pagination.setPageNumber( this.pagination.getPageNumber()+1 ) ;
		}
	}
	
	/**
	 * 重置页面
	 */
	public void reIndex()
	{
		try {
			this.crs.beforeFirst();
			
		} catch (SQLException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
	}
	
	public void resetCurrentPageIndex()
	{
		//currentPageIndex = 1;
	}
	
	/**
	 * 转到某页
	 * @param int pageIndex
	 * @return void
	 */
	public void turnToPage(int newPageIndex)
	{
		//currentPageIndex = newPageIndex;
		this.isPaged = false;
	}
	
	/**
	 * 添加数据
	 * @parma page:Page
	 * @return void
	 */
	
	
	/**
	 * Constructor for QueryRowset.
	 */
	public QueryDataSet() throws SQLException
	{
		doinit() ;
		// wzw on 2007-06-05
		// 对于一个 RowSet 对象，如果不立即加载数据，单纯的构建一个没有必要
		// log.debug("Error: the code has been drop by Zeven on 2007-06-05."); 
		// this.crs = new RowSet() ;
		

	}

	public QueryDataSet( RowSet crs) throws SQLException
	{
		doinit() ;
		this.crs = crs ;
	}
	
	
	public QueryDataSet( ResultSet rs) throws SQLException
	{
		log.debug(" QueryDateSet ResultSet rs" );
		doinit() ;
		//this.crs.populate( rs ) ;
	}
	

	/**
	 * Zeven on 2008-05-27， 将导航信息从Dao移到Controller中。
	 * @param pageParams
	 * @param datas
	 * @throws SQLException
	 */
	public QueryDataSet( Navigator pagination, String path, RowSet datas) 
	{
		/// this.datas = datas;
		this.crs = datas;
		this.path = path;
		this.pagination = pagination;
		
		doinit() ;
	}

	/**
	 * Zeven on 2008-08-16， 将导航信息从Dao移到Controller中。
	 * @param pageParams
	 * @param datas
	 * @throws SQLException
	 */
	public QueryDataSet( Navigator pagination, String path, List dataList) 
	{
		/// this.datas = datas;
		this.dataList = dataList;
		this.crs = datas;
		this.path = path;

		this.pagination = pagination;
		
		doinit() ;
	}

	
	/**
	 * 初始部分信息。
	 *
	 */
	private void doinit()
	{
		this.beginIndex = 0;
		this.endIndex = getPageSize() - 1;
		if( this.pagination.getRowCount()<0){
			this.pagination.setRowCount( 0 ) ;
		}
	}
	
	
	/**
	 * 添加数据
	 */
	
	public void setDataset( RowSet crs)
	{
		this.crs = crs ;
	}
	
	public void setDataset( ResultSet rs) throws SQLException
	{
		log.debug("do setDataset( ResultSet rs) throws SQLException");
		// this.crs.populate( rs ) ;
	}
	
	
	/**
	 *	其它方法 --------------------------
	 */
	public boolean absolute( int row) throws SQLException
	{
		return ( this.crs.absolute( row ) ) ;
	}
	
	public void beforeFirst() throws SQLException
	{
		this.crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		this.crs.afterLast() ;
	}
	
	public boolean first() throws SQLException
	{
		return ( this.crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return ( this.crs.last() ) ;
	}
	
	public boolean previous() throws SQLException
	{
		return ( this.crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		//log.debug("======================= "+this.crs.getClass() );
		return ( this.crs.next() ) ;
	}
	
	
	/**
	 *	BigDecimal 的geter 方法。
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( this.crs.getBigDecimal( colNum ) ) ;
	}
	
	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( this.crs.getBigDecimal( colName)) ;
	}
	
	/**
	 *	Blob 的geter 方法。
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( this.crs.getBlob( colNum )) ;
	}
	
	public Blob getBlob( String colName ) throws SQLException
	{
		return ( this.crs.getBlob( colName )) ;
	}
	
	/**
	 *	Boolean 的geter 方法。
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( this.crs.getBoolean( colNum) ) ;
	}
	
	public boolean getBoolean( String colName) throws SQLException
	{
		return ( this.crs.getBoolean( colName ) ) ;
	}
	
	
	/**
	 *	Byte 的geter 方法。
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( this.crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( this.crs.getByte( colName ) ) ;
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
	public Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}
	
	public Date getDate( String colName) throws SQLException
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
	
//	/**
//	 *	抽象的 的geter 方法，与getObejct相同，在接口中去掉了。
//	 */
//	public Object get( int colNum) throws SQLException
//	{
//		return ( crs.getObject( colNum) ) ;
//	}
//	public Object get( String colName)  throws SQLException
//	{
//		return ( crs.getObject( colName ) ) ;
//	}
//	
	
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
	
	// --------------------------------------------  get 方法 end 
	//说明：其他的get方法需要再添加。
	
	
	/**
	 *	返回总的行数
	 */
	public int size()
	{
		if ( this.crs instanceof List ) {
			return ((List)this.crs).size();
			
		}else{
			return this.pagination.getRowCount();
			
		}   // 这里只是暂时使用
		
		// eturn rowCount ;
	}


	/**
	* <p>不采用Web服务器缓存，每次翻页都查找数据库，
	* 		针对不同的数据如Oracle,SQL Server采用不同的封装实现。
	* 	wzw on 2006-11-28 将onclick事件中的exit修改为'',使用return 也不行。</p>
	* @param commandName 翻页时用的的URL，后面带有翻页信息参数和其他需要的参数
	* @return 查询导航信息html代码
	*/
	public String getPagesPnfl( )
	{
		return this.pagination.getPagesPnfl( ); //8, commandName
	}
	public String getPagesPnfl2( )
	{
		return this.pagination.getPagesPnfl2( );//commandName
	}


	public String getPagesPn( ) {

		return this.pagination.getPagesPn( ); //commandName
	}
 
	
	/**
	 * Zeven:方便页面调用，直接使用 dataObject，而不需要具体指定是dataList还是crs对象。
	 */
	public Object getDataObject() {
		if(this.crs==null){
			return this.dataList;
		}else{
			return this.crs;
		}
	}

	public String getPath() {
		return this.path;
	}

	/**
	 * wzw 
	 */
	public void setPath(String path) {
		this.path = path ;
		this.pagination.setPath(path);  //修改导航对象的path属性
		
	}

	public int getOffset() {
		return 0;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount ;
		
	}

}
