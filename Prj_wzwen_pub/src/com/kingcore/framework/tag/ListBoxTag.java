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

package com.kingcore.framework.tag ;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

/**
 * @version		1.00 2006.11.16
 * @author		Zeven.WU
 *
 * 取request域中的RowSet对象，生成一个界面的ListBox对象。
 * 
 *  mod by Zeven on 2007-08-08, 加入 id 属性，id如果没有设置则取name的值；
 */

public class ListBoxTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger( com.kingcore.framework.tag.ListBoxTag.class);
	
	/**
	 * 是否可以多选。
	 */
	private String multiple = null;
	
    /**
     * 位于request域中，存放了listBox对象的数据集合的RowSet对象的名称。
     */
	protected String dataSetName=null;
    /**
     * 在空白处显示的内容，如果没有就不允许为空
     */
    protected String blank = null;
    /**
     * 显示对应sql语句的列名
     */
    protected String show = null;
    /**
     * 取值的列名
     */
    protected String value = null;
    /**
     * 是否显示取值的列
     */
    protected String together = null;

    private String id = null;
    private String name;
    private String disabled;
    private String onClick;
    private String ondblClick;
    private String onChange;
    private String onBlur;
    private String onFocus;
    private String style;
    private String size;
    private String defaultValue;

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

    public void setName(String name)
    {
        this.name = name;
    }

    public void setBlank(String blank)
    {
        this.blank = blank;
    }

    public void setShow(String show)
    {
        this.show = show;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public void setTogether(String together)
    {
        this.together = together;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

	public String getDataSetName() {
		return dataSetName;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getondblClick() {
		return ondblClick;
	}

	public void setondblClick(String ondblClick) {
		this.ondblClick = ondblClick;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

    public void setOnChange(String onChange)
    {
        this.onChange = onChange;
    }

    public void setOnBlur(String onBlur)
    {
        this.onBlur = onBlur;
    }

    public void setOnFocus(String onFocus)
    {
        this.onFocus = onFocus;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

	/**
	 * 获取 request域中的名称为 dataSetName的RowSet对象，做为select选项值输出。
	 */
    public int doStartTag() throws JspException
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            RowSet obj = (RowSet)pageContext.getRequest().getAttribute( getDataSetName() );
            
            if(obj==null){
                return SKIP_BODY;	//this.
            }
            sb.append("<select id=\"");
            sb.append( getId() );
            sb.append("\" name=\"");
            sb.append( name );
            sb.append("\"");
            if (onClick != null)
            {
                sb.append(" onClick=\"");
                sb.append(onClick);
                sb.append("\"");
            }
            if (ondblClick != null)
            {
                sb.append(" ondblClick=\"");
                sb.append(ondblClick);
                sb.append("\"");
            }
            if (size != null)
            {
                sb.append(" size=\"");
                sb.append(size);
                sb.append("\"");
            }
            if (multiple != null)
            {
                sb.append(" multiple=\"");
                sb.append(multiple);
                sb.append("\"");
            }
            if (onChange != null)
            {
                sb.append(" onChange=\"");
                sb.append(onChange);
                sb.append("\"");
            }
            if (onBlur != null)
            {
                sb.append(" onBlur=\"" + onBlur + "\" ");
            }
            if (onFocus != null)
            {
                sb.append(" onFocus=\"" + onFocus + "\" ");
            }
            if (style != null)
            {
                sb.append(" style=\"" + style + "\" ");
            }
            if (disabled != null && disabled.equals("true") )
            {
                sb.append(" disabled ");
            }
            if (defaultValue != null)
            {
                sb.append(" value=\"" + defaultValue + "\"");
            }
            sb.append(">");
            if (blank != null)
            {
                sb.append("<option value=\"\">" + blank + "</option>");
            }
            // data list
            obj.beforeFirst();
            while (obj.next())
            {
                value =obj.getString(1);
                show=obj.getString(2);
                show = (show==null?"":show);
                value= (value==null?"":value);
                //log.debug("show="+show + " value="+value );
                
                sb.append(
                    "<option value=\""
                        + value.trim()
                        + "\">");
                if (together != null)
                {
                    sb.append(value.trim() + ": ");
                }
                sb.append(show);
                sb.append("</option>");
                    
            }
            sb.append("</select>");

        } catch (SQLException se) {
			log.debug("debug", se);
        	/// se.pri ntStackTrace();
        	
        } catch (Exception e) {
			log.debug("debug", e);
        	/// e.pri ntStackTrace();
        	
        } finally {
        	
        }

        JspWriter out = pageContext.getOut();
        try
        {
            out.write( sb.toString() );
            
        } catch (IOException e) {
			log.debug("debug", e );
        	/// e.pri ntStackTrace();
        	
        }
        
        return SKIP_BODY;	//this.
    }
    
   	/**
	 * Releases all instance variables.
	 */
	public void release() {
	    super.release();
	    
		this.id = null;
		this.name = null;
		this.dataSetName = null;
		this.blank = null;	
		this.disabled = null;
		this.show = null;
		this.value = null;
		this.together = null;	
		this.style = null;
		this.onChange = null;
		this.onBlur = null;
		this.onFocus = null;
		this.defaultValue  = null;
		this.multiple  = null;
	}

	/**
	 * 获取页面select对象是否可以多选，true or false。
	 */
	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	/**
	 * 获取页面select对象的id 属性值，
	 * 		如果设置了id属性则取id属性的值作为select对象的id属性，
	 * 		没有设置则取name属性作为id属性的值。
	 */
	public String getId() {
		return id==null?name:id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
