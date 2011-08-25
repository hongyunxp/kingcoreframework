/*
 * @(#)UpdateAction..java		    2004/04/23
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.upload ;


import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import com.kingcore.framework.util.ObjectManager;
/**
 * @version		1.00 2004.04.23
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
public class UploadFileAction extends Action {




public ActionForward execute(
						ActionMapping mapping,
						ActionForm form , 
						HttpServletRequest request, 
						HttpServletResponse response ) throws Exception
	{
		
	response.setContentType( "text/html;charset=gb2312") ;//"text/html; charset=GBK" );
	PrintWriter out = response.getWriter();
	
	//get the config
	ActionServlet servlet = (ActionServlet)ObjectManager.getObjectInApplication(request, Globals.ACTION_SERVLET_KEY) ;
	ServletConfig  config = servlet.getServletConfig() ;
	String temppath = config.getInitParameter("temppath") ;
	System.out.print("\ntempath" + temppath); 
	String savepath = config.getInitParameter("savepath") ;
	
	if (!savepath.endsWith("/")) ;
		savepath = savepath + "/" ;
	System.out.print("\nsavepath" + savepath); 	
	
	DiskFileUpload diskFileUpload = new DiskFileUpload();
	// maximum size before a FileUploadException will be thrown
	diskFileUpload.setSizeMax( 100*1024*1024 );
	// maximum size that will be stored in memory bofore flush into disk
	diskFileUpload.setSizeThreshold( 4096 );
	// the tmporary location for saving data that is larger than getSizeThreshold()
	diskFileUpload.setRepositoryPath( temppath );
	
	List fileItems;
	try { fileItems = diskFileUpload.parseRequest( request ); }
	catch( FileUploadException ex ) 
	{
	    System.out.print("\nerror happen when fileItem.write") ;
	    return mapping.findForward("fail");
		//throw new ServletException("DiskFileUpload.parseRequest() fail",ex); 
	}
	Iterator iter = fileItems.iterator();
	for( ; iter.hasNext(); ) {
	    // form item
	    FileItem fileItem = (FileItem) iter.next();
	    if( fileItem.isFormField() ) {
	        System.out.print( "\nform field : " + fileItem.getFieldName() + ", " + fileItem.getString() + "&lt;br&gt;" );
	        continue;
	    }
	    String fileName = fileItem.getName();
	    // empty item -- user haven't chosen file
	    if( fileName.equals("") ) continue;
	    
	    //get the fileName only
	    // file name maybe include path info ( e.g. IE )
	    // get pure file name from possible path-file name
	    String[] possibleSeparator = new String[] { "/", "\\" };
	    for( int i=0;i<possibleSeparator.length; i++ ) {
	        String separator = possibleSeparator[i];
	        int index = fileName.lastIndexOf( separator );
	        if( index!=-1 ) {
	            fileName = fileName.substring(index+separator.length());
	            break;
	        }
	    }
	    try { fileItem.write( new File( savepath + fileName) ); }
	    catch( Exception ex ) 
	    {
	    	System.out.print("\nerror happen when fileItem.write"+savepath + fileName ) ;	    
	    	//throw new ServletException("FileItem.write() fail",ex); 
	    	return mapping.findForward("fail") ;
	    }
	}
	
	return mapping.findForward("success") ;

}

}
