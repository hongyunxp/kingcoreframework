package com.kingcore.framework.tag ;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * @author wuzewen
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TabpageChangeTag extends TagSupport
{
	protected String path = null;

    protected String tabpage = null;

    protected String action = null;

	public String getPath() {
        return (this.path);
    }

    public void setPath(String path) {
        this.path = path;
    }

	public String getTabpage() {
        return (this.tabpage);
    }

    public void setServer(String server) {
        this.tabpage = tabpage;
    }

    public String getAction(){
    	return (this.action);
    }

    public void setAction(String action){
    	this.action = action;
    }

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String tabpage = request.getParameter("tabpage");

        JspWriter out = pageContext.getOut();

        tabpage = request.getRequestURI() +"?tabpage="+ tabpage ;
        System.out.print("TabpageChangeTag: "+tabpage ) ;
        try {
            out.write( tabpage );
        } catch (IOException e) {
        }
        return EVAL_BODY_INCLUDE;
    }
}
