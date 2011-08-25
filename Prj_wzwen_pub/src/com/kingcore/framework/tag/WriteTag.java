/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

import wzw.lang.Escaper;
import wzw.text.Formatter;



/**
 * <p>将request(首选) or session or pageContext or application 域中的RowSet对象的第一行的某个列的值输出，
 * 		也可以嵌套到IterateTag 标签中，输出当前行的某个列的值。
 * 建议以后使用 columnName,columnIndex 替换 property,columnIndex. 
 * 		** 现在只支持对RowSet的取值，不支持其他对象。</p>
 * @author	WUZEWEN on 2006.09.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */
public class WriteTag extends TagSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * log4j日志对象。
     */
    protected final static Logger log = Logger.getLogger(com.kingcore.framework.tag.WriteTag.class);

	/**
	 * RowSet or DataSet 对象的名字
	 */
	protected String name = null;
	/**
	 * RowSet or DataSet 对象要取数的某个列的名字。
	 */
	protected String property = null;
	/**
	 * RowSet or DataSet 对象要取数的某个列的下标。
	 */
	protected String columnIndex = null ;
	private int columnIndexNumber ;
	
	private String columnType = null;	//列类型，如：默认string,其他 int, float, ...
	
	/**
	 * RowSet or DataSet 对象所放置的域，默认为request。
	 */
    protected String scope = null;

    /**
     * Escape Character转义[换码]字符;
     * 包含以下枚举值：
     *	htmlValue：转义为html 主要针对内容中的 ',",&
     *  scriptValue：转义为JScript，主要针对内容中的 ',",\,
     *  htmlValueForScript：转义为html，但是又作为JScript变量，主要针对 ',",\
     *  
     */
    protected String escape=null;
    
    /**
     * The format string to be used as format to convert
     * value to String.
     */
    protected String formatStr = null;

    public String getFormat() {
        return (this.formatStr);
    }

    public void setFormat(String formatStr) {
        this.formatStr = formatStr;
    }
    
	public static void main(String[] args) {
 
		Object obj = null;
		System.out.println(  obj instanceof String );
	}

	/**
	 * RowSet or DataSet 对象的名字
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * RowSet or DataSet 对象要取数的某个列的名字。
	 */
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * RowSet or DataSet 对象要取数的某个列的下标值。
	 * 建议以后使用 columnName 替换 property.
	 */
	public String getColumnIndex() {

		return columnIndex;
	}
	
	public void setColumnIndex(String columnIndex ) {		
		this.columnIndex = columnIndex ;

		try {
			this.columnIndexNumber = Integer.parseInt( this.columnIndex );
		} catch(Exception e) {
			this.columnIndexNumber = -1;
		}
	}

	public int getColumnIndexNumber() {
		return this.columnIndexNumber ;
	}
	
	
	/**
	 * Zeven 以后建议使用 columnName 替代 property。
	 * RowSet or DataSet 对象要取数的某个列的名称。
	 */
	public String getColumnName() {
		return this.property ;
	}
	public void setColumnName(String columnName ) {		
		this.property = columnName;
	}
	
	/**
	 * RowSet or DataSet 对象所放置的域，默认为request。
	 */
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getEscape() {
		return escape;
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}

	/**
	 * 
	 */
    public int doStartTag() throws JspException {

    	// log.debug( "  WriteTag ----------doStartTag " );
    	// 查找是否有外层循环标签存在
    	Tag tag=TagSupport.findAncestorWithClass(this, com.kingcore.framework.tag.IterateTag.class);
    	Object obj=null;
    	Object val="";

    	//1。根据Name属性获取对象
    	if(tag == null){
    		// 如果没有上层标签，从设置的参数中取。
    		if(this.getScope()==null){
    			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    			obj=request.getAttribute( this.getName() );
				if(obj==null){
    				obj=pageContext.getAttribute( this.getName() );
				}

    		}else if(this.getScope().equals("session")){
    			obj=pageContext.getSession().getAttribute( this.getName() );

    		}else if(this.getScope().equals("page")){
    			obj=pageContext.getAttribute( this.getName() );

    		}else {
    			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    			obj=request.getAttribute( this.getName() );
    		}

    	}else{
			if(this.getProperty()==null && this.getColumnIndex()==null ){	//没有属性值和属性索引值，输出序列i的情况，不取 RowSet 或者 DataSet.
				if( pageContext.getAttribute(this.getName())!=null){
					val=pageContext.getAttribute(this.getName());
				}
			} else{
    			// 如果有上层标签，上层标签是放置在pageContex域中的，从pageContext中取。
    			obj=pageContext.getAttribute(this.getName());
    			//obj=pageContext.getRequest().getAttribute(this.getName());
			}
    	}

    	//2。根据对象、对象的类型、Property属性，获取值。
        if( obj instanceof RowSet ){
    		RowSet crs = (RowSet)obj;
	        try {
	        	if(tag == null)
	            {
	        		//System.out.println(" outer tag is null");
	        		// 如果外部有标签，采用外部标签改变指针，否则自己改变指针
//		        	crs.beforeFirst();
//					if(crs.next()){
//						if( crs.getObject(this.getProperty())!=null ){
//							val=crs.getObject( this.getProperty());
//			        		//System.out.println(" outer tag is not null " + val );
//						}
//			        }
					try{
		        		//如果外层有标签，则由外层的Iterate标签改变指针，自己不用处理指针
						crs.absolute(1); // 可能会抛出异常
					}catch(Exception e){
						//第一行不存在，没有可取得值。
						
					}
	            }
//	        	else{
//	        		//如果外层有标签，则由外层的Iterate标签改变指针，自己不用处理指针
//					if( crs.getObject(this.getProperty())!=null ){
//						val=crs.getObject( this.getProperty());
//						//System.out.println(" outer tag is not null " + val );
//					}
//	            }
	        	
				if( this.getProperty()!=null ){
//					if("int".equalsIgnoreCase(this.getColumnType())){
//						val=crs.getInt( this.getProperty());
//					}		// 暂时取消columnType，使用format来处理int类型输入。
					val=crs.getObject( this.getProperty());
					
				}else if( this.getColumnIndex()!=null ){	// 根据 columnIndex 判断，但是取 columnIndexNumber 的值。
					val=crs.getObject( this.getColumnIndexNumber() );
				}

			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}

        }

        // 输出信息到页面
        JspWriter out = pageContext.getOut();
        try {
        	if( val == null) {
            	out.write( "" );
                return EVAL_BODY_INCLUDE;        		
        	}
        	
        	if( this.getFormat()!=null ){		// 针对数字类型
        		val = Formatter.formatObject( val, this.getFormat()) ;
        	
        	}else if( this.getEscape()!=null ){							// 针对字符串类型，应该不会与Format一起使用，以后可以更改
        		if( this.getEscape().equals("htmlValue")) {
        			val = Escaper.escape2Html( val.toString() );	//WebPageUtils.toHTMLValue(
        			
        		}else if( this.getEscape().equals("scriptValue")) {
        			val = Escaper.escape2JavaScript( val.toString() );	//WebPageUtils.toScriptValue(
        			
        		}else if( this.getEscape().equals("htmlValueForScript")) {
        			val = Escaper.escape2HtmlForJavaScript( val.toString() );	// WebPageUtils.toHTMLValueForScript( 
        			
        		}
        	}
        	
        	out.write( val.toString() );
        	
        } catch (IOException e) {
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
			throw new JspException(e.getMessage());
        }

        //System.out.print("\nval:" + val ) ;
        return EVAL_BODY_INCLUDE;
    }


    
	/**
	 * Releases all instance variables.
	 */
	public void release() {
		this.name = null;
		this.property = null;
		this.columnIndex = null;
		this.columnType = null;
		this.scope = null;
		this.formatStr=null;
		this.escape = null;
		
	    super.release();
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

}
