package com.kingcore.framework.tag ;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.base.database.DataSourcImpl;
import com.kingcore.framework.util.ConvertUtils;


/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 
 * @modify WUZEWEN 2005.04.02 增加 value.trim() ;
 *
 */
 
public class SqlListTag extends TagSupport
{

	/**
	 * 是否可以多选。
	 */
	private String multiple = null;
	
	
    /**
    * 要查询的sql语句
    */
    protected String sql = null;
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

    private String name;
    private String onClick;
    private String ondblClick;
    private String onChange;
    private String onBlur;
    private String onFocus;
    private String style;
    private String size;
    private String defaultValue;

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * WZW on 2006-10-16 支持变量设置，如果是${value}格式，表示是一个放在pageContext域中的对象。 
     * @param sql2 要执行的sql语句值，或者是保存sql语句的位于pageContext域中的对象。
     */
    public void setSql(String sql2)
    {
    	if(sql2.startsWith("${")){
   		String str_t=sql2.substring(2,sql2.length()-1).trim();
			if(pageContext.getAttribute(str_t)==null){
				System.out.println("cann't find "+str_t+" in scope pageContext.");
			}
   			this.sql = pageContext.getAttribute(str_t).toString();
    	}else{
    		this.sql = sql2;
    	}
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

    public int doStartTag() throws JspException
    {
        ServletContext context = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet set = null;

        Vector shows = new Vector();
        Vector values = new Vector();
        StringBuffer sb = new StringBuffer();
        try
        {
            context = (ServletContext) pageContext.getServletContext();
            conn = DataSourcImpl.getDefConnection(context);
            stmt = conn.createStatement();
            set = stmt.executeQuery(sql);
            while (set.next())
            {
                shows.add(set.getString(show));
                values.add(set.getString(value));
            }
            sb.append("<select name=\"");
            sb.append(name);
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
            if (defaultValue != null)
            {
                sb.append(" value=\"" + defaultValue + "\"");
            }
            sb.append(">");
            if (blank != null)
            {
                sb.append("<option value=\"\">" + blank + "</option>");
            }
            int count = shows.size();
            if (count > 0)
            {
                for (int i = 0; i < count; i++)
                {
                	value = (String)values.elementAt(i);
                	show = (String)shows.elementAt(i);
                	value = ConvertUtils.ConvertString(value);
                	show = ConvertUtils.ConvertString(show);
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
            }
            sb.append("</select>");

            JspWriter out = pageContext.getOut();
            try
            {
                out.write(sb.toString());
            }
            catch (IOException e)
            {
            }
        }
        catch (SQLException se)
        {
        }
        catch (Exception e)
        {
        }
        finally
        {
        	try{
	        	if(set!=null)
	        		set.close() ;
        	}catch(SQLException e){
        		System.out.println("fail when close ResultSet!") ;
        	}
        	try{
	        	if(stmt!=null)
	        		stmt.close() ;
        	}catch(SQLException e){
        		System.out.println("fail when close Statement!") ;
        	}
        	try{
	        	if(conn!=null)
	        		conn.close() ;
        	}catch(SQLException e){
        		System.out.println("fail when close Connection!") ;
        	}
        }
        JspWriter out = pageContext.getOut();
        try
        {
            out.write("");
        }
        catch (IOException e)
        {
        }
        return this.SKIP_BODY;
    }
    
   	/**
	 * Releases all instance variables.
	 */
	public void release() {
		blank = null;	
		sql = null;
		show = null;
		value = null;
		together = null;	
		style = null;
		onChange = null;
		onBlur = null;
		onFocus = null;
		defaultValue  = null;
		multiple  = null;
	    super.release();
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

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

}
