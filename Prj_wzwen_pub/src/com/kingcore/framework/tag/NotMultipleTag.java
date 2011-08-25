/*
 * $Id: EqualTag.java 54929 2004-10-16 16:38:42Z germuska $ 
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


import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>判断当前获取的值是否是某个值的整数倍。</p>
 * @author	WUZEWEN on 2006.09.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class NotMultipleTag extends TagSupport {


	private String name=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private String radix=null;
	
	public String getRadix() {
		return radix;
	}
	public void setRadix(String radix) {
		this.radix = radix;
	}
	/**
	 * 
	 */
    public int doStartTag() throws JspException {
    	boolean isMatch=false;
    	// 查找是否有外层循环标签存在
    	Tag tag=TagSupport.findAncestorWithClass(this, com.kingcore.framework.tag.IterateTag.class);
    	Object obj=null;
    	Object val="";

    	if( pageContext.getAttribute(this.getName())!=null){
    		val=pageContext.getAttribute(this.getName());
    	}
    	
    	if(val==null){
            return (SKIP_BODY);
    	}
    	
    	String radix = this.getRadix();
    	if( Integer.parseInt( val.toString() ) % Integer.parseInt( radix ) ==0 ){
    		isMatch = true ;
    	}

        // 输出信息到页面
        if ( isMatch )
            return (SKIP_BODY);
        else
            return (EVAL_BODY_INCLUDE);
    }


    /**
     * Evaluate the remainder of the current page normally.
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        name = null;
        radix = null;

    }

}
