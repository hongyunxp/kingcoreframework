package com.kingcore.framework.tag ;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kingcore.framework.util.CookieUtils;

/* wuzewen   2003.04.25
 *
 *  根据名称判断是否存在该名称的cookie，存在则向页面输出 checked。
 *
 */
public class IsCookieSetTag extends BodyTagSupport
{
	public String name  ;
	private String  value ;
	//获取参数
	public void setName( String name )
	{
		this.name = name   ;    // name 为空时web容器不会让jsp页面通过。
	}

	//执行行为
	public int doEndTag(  )
	{
		String checked ;
		HttpServletRequest req =
		                  (HttpServletRequest) pageContext.getRequest();
		checked = CookieUtils.isCookieSet(name, req ) ? "checked" : "";
		try{
		    pageContext.getOut().println( checked ) ;
	    }
	    catch( IOException  e ) {}   // Ignore
        return EVAL_PAGE;
	}

	//获取行为体
	public int doAfterBody( )
	{
        //BodyContent bodyContent = getBodyContent();
        //在BodyTagSuppor 中已经得到bodyContect
        JspWriter out = getPreviousOut();
		value = bodyContent.getString() ;
		return  SKIP_BODY ;
	}
    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        super.release();
    }

    /*  BodyTagSupport 实现的方法
    public void setPageContext (PageContext  pageContext) ;
    public int  doStartPage()
    public int  doInitBody()
    public int  doAfterBody()
    */
}
