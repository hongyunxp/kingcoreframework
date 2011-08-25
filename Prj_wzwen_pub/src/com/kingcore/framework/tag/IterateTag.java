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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.sql.RowSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.taglib.TagUtils;

import com.kingcore.framework.bean.NavigableDataSet;


/** 
 * <p>对request or session or application  域中的 RowSet 对象进行循环，改变其指针位置，
 * 		一般内部嵌套 WriteTag标签使用。
 *   修改，无论对于内部有没有标签，是否设置了 id 属性，建议对象都放在 request中，而不是pageContext中。
 * 	  ** 建议以后使用 columnName,columnIndex 替换 property,columnIndex.
 *    ** NavigableDataSet 对象Iterate 标签中默认取其Datas属性，也是一个RowSet。
 *    
 *    </p>
 * @author	WUZEWEN on 2006.09.21
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5 
 */

public class IterateTag extends BodyTagSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * log4j日志对象。
     */
    protected final static Logger log = Logger.getLogger(com.kingcore.framework.tag.IterateTag.class);

    // ----------------------------------------------------- Instance Variables

    /**
     * Iterator of the elements of this collection, while we are actually
     * running.
     */
    protected Iterator iterator = null;
    /**
     * 要循环的是RowSet对象
     */
    protected RowSet crs = null;
    /**
     * 要循环的是DataSet对象
     */

    /**
     * The name of the scripting variable to be exposed as the current index.
     */
    protected String indexId = null;

    /**
     * The actual offset value (calculated in the start tag).
     */
    protected int offsetValue = 0;

    /**
     * i 从0开始，是否考虑为了显示，i从 1开始？
     */
    protected int rowNum = 0;

    /**
     * Has this tag instance been started?
     */
    protected boolean started = false;


    // ------------------------------------------------------------- Properties


    /**
     * The name of the scripting variable to be exposed.
     */
    protected String id = null;

    public String getId() {
        return (this.id);
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * The name of the collection or owning bean.
     */
    protected String name = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The property name containing the collection.
     */
    protected String property = null;

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
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
     * The property name containing the collection.
     */
	protected String columnIndex = null;
    //protected String columnIndex = null;
	private int columnIndexNumber = -1 ;


	public String getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(String columnIndex) {
		this.columnIndex = columnIndex; 
		
			try {
				this.columnIndexNumber = Integer.parseInt( columnIndex );
			} catch(Exception e) {
				this.columnIndexNumber = -1;
			}
	}

	public int getColumnIndexNumber() {
		return columnIndexNumber;
	}
	
    
    /**
     * The scope of the bean specified by the name property, if any.
     * By WZW, the default scope is request.
     */
    protected String scope = null;

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * The Java class of each exposed element of the collection.
     */
    protected String type = null;

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * <p>Return the zero-relative index of the current iteration through the
     * loop.  If you specify an <code>offset</code>, the first iteration
     * through the loop will have that value; otherwise, the first iteration
     * will return zero.</p>
     *
     * <p>This property is read-only, and gives nested custom tags access to
     * this information.  Therefore, it is <strong>only</strong> valid in
     * between calls to <code>doStartTag()</code> and <code>doEndTag()</code>.
     * </p>
     */
    public int getIndex() {
        return rowNum++;
		//if (started)
        //   return (offsetValue + lengthCount - 1);
        //else
        //    return (0);
    }

    public String getIndexId() {
        return (this.indexId);
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Construct an iterator for the specified collection, and begin
     * looping through the body once per element.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
    	//log.debug( "  IterateTag ----------doStartTag " );
    	Object obj=null;

		if(this.getProperty()==null && this.getColumnIndex()==null ){
	        obj = TagUtils.getInstance().lookup(this.pageContext, this.name, this.scope);
	        if( obj instanceof NavigableDataSet){  // 转一次，处理了RowSet和NavigableDataSet 不带属性情况
	        	try {
	                obj = PropertyUtils.getProperty(obj, "datas");

	            } catch (Exception e) {
	                throw new JspException( "获取对象 "+ this.getName() +"的属性"+this.property +"失败!!");

	            }
	        }
	        
		}else {	// 这种现在没有，2008-03-12，以后扩充。
			Object tmpObj = TagUtils.getInstance().lookup(this.pageContext, this.name, this.scope);;
			
			if(tmpObj instanceof NavigableDataSet ){
				try {
					RowSet rs = ((NavigableDataSet)tmpObj).getDatas();
					String tmpName="";
					if( this.getColumnIndex()!=null ) {
						tmpName= rs.getString(this.getColumnIndexNumber());
					}else if( this.getProperty()!=null ) {
						tmpName= rs.getString(this.getProperty());
					}
					
					//log.debug(tmpName);
					//tmpName="stockOrdersMx";
					if(pageContext.getAttribute("i")!=null){
						obj=pageContext.getRequest().getAttribute(tmpName+ pageContext.getAttribute("i").toString());
						pageContext.setAttribute(getId(), obj);
						//log.debug("obj name is "+tmpName+ pageContext.getAttribute("i").toString() );
					
					}else{
						log.error("没有设置上层的循环编号i在pageContext中。");
					}
					
				} catch (SQLException e) {
					log.debug("debug", e);
					/// e.pri ntStackTrace();
				}
			}
		}
    	
		if ( this.indexId != null) {
			log.debug("pageContext.setAttribute( this.indexId, new Integer(getIndex()))"+this.indexId+"---1-----"+rowNum );
			pageContext.setAttribute( this.indexId, new Integer(getIndex()));
		}

		//2。根据对象、对象类型、id属性，移动指针，并且放置在pageContext域中。
    	//分别处理两种情况，因为没有公用的接口
    	if( obj instanceof RowSet ){
			this.crs = (RowSet)obj;
	        try {
	        	crs.beforeFirst();
				if(crs.next()){
					if(getId()!=null){
		            	pageContext.setAttribute(getId(), crs);
		            	//pageContext.getRequest().setAttribute(getId(), crs);
					}else{  
						// if no id property , then set by name.
						// id is prior to name.
						pageContext.setAttribute(getName(), crs);
					}
		            return EVAL_BODY_AGAIN;	//replace EVAL_BODY_TAG;
		            
		        } else {					
					///bodyContent.clearBody();
		            return (SKIP_BODY);
		        	
		        }
			} catch (SQLException e1) {
				log.debug("debug", e1);
				/// e1.pri ntStackTrace();
				throw new JspException(e1.getMessage());
			}
        }
    	
    	return (SKIP_BODY);
    }

    /**
     * Make the next collection element available and loop, or
     * finish the iterations if there are no more elements.
     *
     * @exception JspException if a JSP exception has occurred
     */
	public int doAfterBody() throws JspException {
		//log.debug( "  IterateTag ----------doAfterBody " );

        if (bodyContent != null) {
            TagUtils.getInstance().writePrevious(pageContext,
                bodyContent.getString());
            bodyContent.clearBody();
        }

    	try {

			if ( this.indexId != null) {
				log.debug("pageContext.setAttribute( this.indexId, new Integer(getIndex()))"+this.indexId+"---2-----"+rowNum);
				pageContext.setAttribute(indexId, new Integer(getIndex()));
			}

    		if(this.crs!=null){
				//log.debug(" -it doAfterBody ... this.crs!=null ");
    			if(this.crs.next()){
    				if(getId()!=null){
		            	pageContext.setAttribute(getId(), this.crs);
		            	//pageContext.getRequest().setAttribute(getId(), this.crs);
    				}else{
    					pageContext.setAttribute(getName(), this.crs);
    				}
    				
		            return EVAL_BODY_AGAIN;	//replace EVAL_BODY_TAG;
    			}

    		}
    		
    		return (SKIP_BODY);
	        
		} catch (SQLException e1) {
			log.debug("debug", e1);
			/// e1.pri ntStackTrace();
			throw new JspException(e1.getMessage());
		}

    }

    /**
     * Clean up after processing this enumeration.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
    	
    	if (getBodyContent() != null) {
            try {
                getPreviousOut().print(getBodyContent().getString());
				/// bodyContent.clearBody();	// add by wzw on 2007-08-08.
            }
            catch (Exception e) {
				log.debug("debug", e);
            	/// e.pri ntStackTrace();
    			throw new JspException(e.getMessage());
            }
        }

		release();		// by wzw on 2008-12-01, 本来可以不主动调用release方法的
		
        return EVAL_PAGE;
    	//return (SKIP_BODY);
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
		log.debug("=====================release()");
        super.release();

        this.iterator = null;
        this.id = null;
        this.crs=null;
        this.name = null;
        this.property = null;
        this.columnIndex = null;
        //this.columnName = null;
		this.indexId = null;
        this.scope = null;
        this.started = false;
        this.rowNum=0;
        
    }


}
