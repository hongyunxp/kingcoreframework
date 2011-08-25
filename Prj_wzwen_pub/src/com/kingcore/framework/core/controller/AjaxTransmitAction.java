package com.kingcore.framework.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**<p>ajax转发类，实现了ajax跨域调用。</p>
 * 客户端格式为
 * function sendHttpClient(){  
	var url  = "http://190.10.2.55:8085/diary/testHttpClient.jsp"; 
		var myAjax = new Ajax.Request(
        "/ajaxTransmit.jthml?t="+new Date().getTime(), 
        {
        
            method: 'post',  
     		postBody: 'url='+url,
     		asynchronous: 'false',
      		onComplete: function(response){
            	var xml = response.responseXML;  
            	
            	alert(response.responseText); 
             
          }
        }
    );  
 return;
	
} 
 * <pre>
 * 
 * </pre>
 * @author Zeven on 2007-12-5 方法未使用，可能有待使用时调适完善。
 *
 */
public class AjaxTransmitAction extends Action {

	
	private static Logger log = Logger.getLogger(AjaxTransmitAction.class);

	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.debug("entering 'AjaxTransmitAction' method...");
    	
    	String string = "";
    	String urlTarget = request.getParameter("urlTarget");
    	/// urlTarget = "http://www.apache.org/";
		/// urlTarget = "http://190.10.2.55:8085/diary/testHttpClient.jsp?name=中文vinsun&sn=001";
    	log.info(urlTarget);

        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(urlTarget);
        
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
        		new DefaultHttpMethodRetryHandler(3, false));

        try {
          // Execute the method.
          int statusCode = client.executeMethod(method);

          if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
          }

          // Read the response body.
          byte[] responseBody = method.getResponseBody();

          // Deal with the response.
          // Use caution: ensure correct character encoding and is not binary data
          string = new String(responseBody, "UTF-8");
          log.info(string);

        } catch (HttpException e) {
          System.err.println("Fatal protocol violation: " + e.getMessage());
          e.printStackTrace();
        } catch (IOException e) {
          System.err.println("Fatal transport error: " + e.getMessage());
          e.printStackTrace();
        } finally {
          // Release the connection.
          method.releaseConnection();
        }   
     
     
    	response.setContentType("text/xml;charset=UTF-8");
 
    	try {
    		response.getWriter().println(string);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return null;
    }

}
