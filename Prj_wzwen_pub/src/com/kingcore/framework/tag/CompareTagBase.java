/*
 * $Id: CompareTagBase.java 54929 2004-10-16 16:38:42Z germuska $ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.kingcore.framework.tag;


import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import com.kingcore.framework.bean.DataSet;


/**
 * Abstract base class for comparison tags.  Concrete subclasses need only
 * define values for desired1 and desired2.
 *
 * @version $Rev: 54929 $ $Date: 2004-10-16 17:38:42 +0100 (Sat, 16 Oct 2004) $
 */

public abstract class CompareTagBase extends ConditionalTagBase {

	protected static Logger log = Logger.getLogger( com.kingcore.framework.tag.CompareTagBase.class);
	
    // ----------------------------------------------------- Instance Variables


    /**
     * We will do a double/float comparison.
     */
    protected static final int DOUBLE_COMPARE = 0;


    /**
     * We will do a long/int comparison.
     */
    protected static final int LONG_COMPARE = 1;


    /**
     * We will do a String comparison.
     */
    protected static final int STRING_COMPARE = 2;


    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
     MessageResources.getMessageResources
        ("org.apache.struts.taglib.logic.LocalStrings");


    // ------------------------------------------------------------ Properties


    /**
     * The value to which the variable specified by other attributes of this
     * tag will be compared.
     */
    public String value = null;

    public String getValue() {
        return (this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        value = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Evaluate the condition that is being tested by this particular tag,
     * and return <code>true</code> if the nested body content of this tag
     * should be evaluated, or <code>false</code> if it should be skipped.
     * This method must be implemented by concrete subclasses.
     *
     * @exception JspException if a JSP exception occurs
     */
    protected abstract boolean condition() throws JspException;


    /**
     * Zeven.Woo on 2006-11-23
     * @param desired1 First desired value for a true result (-1, 0, +1)
     * @param desired2 Second desired value for a true result (-1, 0, +1)
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean condition(int desired1, int desired2)
        throws JspException {

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
    				obj=request.getAttribute( this.getName() ); //pageContext
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
			if(this.getProperty()==null && this.getColumnIndex()==null ){	//没有属性值，输出序列i的情况，不取 RowSet 或者 DataSet.
				if( pageContext.getAttribute(this.getName())!=null){
					val=pageContext.getAttribute(this.getName());//pageContext
				}
			} else{
    			// 如果有上层标签，上层标签是放置在pageContex域中的，从pageContext中取。
    			obj=pageContext.getAttribute(this.getName());//pageContext.getRequest().
			}
    	}

    	//2。根据对象、对象的类型、Property属性，获取值。
        if(obj!=null && obj instanceof RowSet ){
    		RowSet crs = (RowSet)obj;
	        try {
	        	if(tag == null)
	            {
					try{
		        		//如果外层有标签，则由外层的Iterate标签改变指针，自己不用处理指针
						crs.absolute(1); // 可能会抛出异常
					}catch(Exception e){
						//第一行不存在，没有可取得值。
					}

	            }
	        	
				if( this.getProperty()!=null ){
					val=crs.getObject( this.getProperty());
				}else if( this.getColumnIndex() !=null ){
					val=crs.getObject( this.getColumnIndexNumber() );
				}

			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}

        } else if(obj!=null && obj instanceof DataSet ){
        	//没有公共的接口，RowSet、DataSet只好各自处理
        	DataSet qds = (DataSet)obj;
	        try {
	        	if(tag == null)
	            {
					try{
		        		//如果外层有标签，则由外层的Iterate标签改变指针，自己不用处理指针
						qds.absolute(1); // 可能会抛出异常
					}catch(Exception e){
						//第一行不存在，没有可取得值。
					}

	            }

				if( this.getProperty()!=null ){
					val=qds.getObject( this.getProperty());
				}else if( this.getColumnIndex()!=null ){
					val=qds.getObject( this.getColumnIndexNumber() );
				}
				
			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}
        }

    // Perform the appropriate comparison
    int result = 0;
    
    // 处理 null 值情况
    if(val==null) {
    	val = "";
    }
    result = val.toString().compareTo( getValue() );

    // Normalize the result
    if (result < 0)
        result = -1;
    else if (result > 0)
        result = +1;

    // Return true if the result matches either desired value
    return ((result == desired1) || (result == desired2));

    }

}

