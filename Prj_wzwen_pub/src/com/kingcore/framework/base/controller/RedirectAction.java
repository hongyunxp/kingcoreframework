package com.kingcore.framework.base.controller ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class is a custom action for sending a redirect request,
 * with possible parameter values URL encoded and the complete URL
 * encoded for URL rewriting (session tracking).
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class RedirectAction extends Action{


    /**
     * 定义一些常量
     */
    
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
    {
    	//System.out.print("TabpageChangeAction")  ;
        doInitialize(request);
        //doExecute(mapping, form, request, response);
        return doForword(mapping, form, request, response);
    }

    public void doInitialize(HttpServletRequest request)
    {

    }

    public void doExecute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse respone)
    {
        /*
        String tabpage = request.getParameter("tabpage");
        
        tabpage = request.getRequestURI() +"?tabpage="+ tabpage ; 
        System.out.print("TabpageChangeAction: "+tabpage ) ; 
        
        respone.sendRedirect(respone.encodeURL( tabpage ));
        */
        //System.out.print("TabpageChangeAction: doExecute ")  ;
        return ;
    }


    public ActionForward doForword(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
    {   
        String ation_name = "" ;
    	
    	ation_name = request.getParameter("forward").trim () ;
    	
    	//String uri = null ;
    	//int pos ;
        //pos = uri.lastIndexOf("/") ;
        //uri = uri.substring(pos, uri.length() ) ;
        //uri = uri +  request.getParameter("tabpage");  ;
        //                           nullnull?
        /*
        System.out.print( "getContentLength:"+request.getContentLength()+
                          "getPathInfo:"     +request.getPathInfo()+
        	              "getRealPath:"     +request.getRealPath("come.jsp")+
        	              "getRemoteAddr"    +request.getRemoteAddr()+
        	              "getRequestURI"    +request.getRequestURI()+
        				  "getServletPath"   +request.getServletPath() ) ;
        
        
        if( this.getObjectInSession(request, "dataSetDetailView") !=null){
			
				QueryDataSet dataSet = (QueryDataSet)this.getObjectInSession(request, "dataSetDetailView");
				//System.out.print("CRGRKH_KHBH="+(String)v.get(0,"CRGRKH_KHBH") ) ; 
				
        }
        */
        //System.out.print("\nRedirectAction  "+ation_name)  ;
        
        return (mapping.findForward( ation_name ));
    }
    
}
