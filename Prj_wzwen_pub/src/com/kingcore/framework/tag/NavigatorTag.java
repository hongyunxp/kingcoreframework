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
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.kingcore.framework.bean.Pagination;


/** 
 * <p>将request or session or pageContext 域中的NavigableDataSet对象（实现了Navigable接口的对象）的导航信息，
 *		输出到当前的页面。
 *		 标签的 type 参数值目前有两个：pnfl, pnfl_2，分别输出不同风格的导航条；
 *	</p>
 * @author	WUZEWEN on 2006.11.01
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */
public class NavigatorTag extends TagSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger( com.kingcore.framework.tag.NavigatorTag.class);
	
	/**
	 * 实现了Navigable接口的对象的名称
	 */
	protected String name = null;	
	/**
	 * 要调用的Navigable对象的导航类型
	 */
	protected String type = "pnfl";
	/**
	 * Navigable接口的对象存在的域，默认"request"
	 */
    protected String scope = null;
    /**
     * Navigable导航路径
     */
    protected String path = null;

    private String onClick = null;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	/**
	 * 实现了Navigable接口的对象的名称
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Navigable接口的对象存在的域
	 */
    public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * 要调用的Navigable对象的导航类型
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
    /**
     * Navigable导航路径
     */
	public String getPath() {
		return path;
	}
	
	/**
	 * 获取jsp页面标签的path属性，如果是java变量，放在pageContext中，传入该名称，使用${paraname}传入。
	 * 		如jsp页面使用 pageContext.setAttribute("path", pathValue);
	 * 				<woo:navegator name="goodsDataSet" path="${path}" type="pnfl"/>
	 */
	public void setPath(String path) {
		if(path==null)
			return;
		path=path.trim();
		if(path.trim().startsWith("${")){
			String str_t=path.substring(2,path.length()-1);
			//System.out.println(str_t);
			if(pageContext.getAttribute(str_t)==null){
				System.out.println("cann't find "+str_t+" in scope pageContext.");
			}
   			this.path = pageContext.getAttribute(str_t).toString();
    	}else{
    		this.path = path;
    	}

	}
	
	public int doStartTag() throws JspException {  
    	Object obj=null;
    	
    	//1。根据Name属性，获取对象。
    	if(this.scope == null){
    		// 如果没有上层标签，从设置的参数中取。
    		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    		obj=request.getAttribute( this.getName() );
    	} else if (this.scope.equals("session")){
    		// 到session取。
    		obj=pageContext.getSession().getAttribute(this.getName());
    	} else if (this.scope.equals("page")){
    		// 如果有上层标签，从上层标签取。
    		obj=pageContext.getAttribute(this.getName());
    	} else {
    		// 如果没有上层标签，从设置的参数中取。
    		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    		obj=request.getAttribute( this.getName() );
    	}
    	
        String val="";
        // 根据导航类型，调用对象相应的导航方法。  path/commandName 参数都放入导航器了，
        // 不放在jsp，而是放在action中设置。
        if(obj!=null && obj instanceof Pagination ){
        	if(this.getType().equals("pnfl")){
        		val = ((Pagination)obj).getPagesPnfl( );
        		
        	}else if(this.getType().equals("pnfl_2")){
        		val = ((Pagination)obj).getPagesPnfl2( );
        		
        	}else if(this.getType().equals("pn")){
        		val = ((Pagination)obj).getPagesPn( );        	
        		
        	}else{
        		val = ((Pagination)obj).getPagesPnfl( );

        	}
        }

        JspWriter out = pageContext.getOut();
        try {
            out.write(val);
        } catch (IOException e) {
			log.error("debug", e);
        	/// e.pri ntStackTrace();
        	throw new JspException(e.getMessage());
        }

        //System.out.print("\nAbsolutePathTag:" + sb_uri.toString()) ;
        return EVAL_BODY_INCLUDE;
    }

	/**
	 * Releases all instance variables.
	 */
	public void release() {
		this.name = null;	
		this.type = null;
		this.scope = null;
		this.path = null;
		
	    super.release();
	}
}
