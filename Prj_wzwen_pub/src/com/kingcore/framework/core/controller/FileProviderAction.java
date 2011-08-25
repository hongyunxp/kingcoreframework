/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.core.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kingcore.framework.base.controller.BaseAction;


/**
 * <p>本类给系统提供文件输出功能，包括直接输出文件内容和打印文件内容到页面两种方式。</p>
 * @author Zeven on 2007-12-4
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class FileProviderAction extends BaseAction {

	private Logger log = Logger.getLogger( this.getClass() );
	
	
	/* (non-Javadoc)
	 * @see com.kingcore.framework.base.controller.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) throws Exception {


		// 调用格式：var url = "/pda/core/fileProvider.jhtml?isPre=y&path="+file_path+"&type=dataFile&file="+ encodeURIComponent(fileName);
		// http://190.10.2.20:7001/fileProvider?file=20061228/1167279014828.jpg&type=appeal_file 
		// 其中数据库记录的内容为 "20061228/1167279014828.jpg"，存放路径根据 type 的值在配置文件中获取。
		//request.setCharacterEncoding("UTF-8");
		
		String fileName=request.getParameter("file");				// 文件名称
		///String projectName = request.getParameter("prjectName");	// 项目名称
		String type=request.getParameter("type");			// 文件类型
		String forwardName=request.getParameter("forwardName");
		String isPre=request.getParameter("isPre");			// 是否采用pre方式输出到页面

		if(forwardName==null){
			forwardName="common.login";
		}
		//this.checkSessionValid(request, "adminCode", forwardName );
		
		String outputFilePath = null; //PdaContext.getOutputFilePath() + File.separator + projectName;	//通过文件夹限制外部对本地的访问路径。
		String fileDirectory = null;
		if("dataFile".equalsIgnoreCase(type) ) {
			fileDirectory = outputFilePath + File.separator + "data";
		}else if("tableFile".equalsIgnoreCase(type) ) {
			fileDirectory = outputFilePath ;	//+ File.separator + "";
		}
		
		log.debug( fileDirectory  );
		if(fileName==null){
			throw new ServletException("file name is null");
		}
		
		try{
			//fileName=fileName.replaceAll("\\","/");
			//fileDirectory = this.getServlet().getServletContext().getInitParameter( fileDirectory );
			//log.debug("------fileDirectory--11111-----" + fileDirectory);
			
			
			//fileDirectory="D:/temp";
			File fileObj=new File( fileDirectory + File.separator + fileName);
			log.debug( "file is "+fileObj.getAbsolutePath() );
			if(!(fileObj).exists()){
				throw new ServletException("file not exist");
			}
			
			if("Y".equalsIgnoreCase(isPre)) {  // 采用内容预览输出
				response.setContentType("text/html;charset=gb2312");  // 解决中文乱码问题
				PrintWriter pw=response.getWriter();
				pw.write( "<pre>");
				pw.write( FileUtils.readFileToString( fileObj , "gb2312") );
				pw.write( "</pre>");
				return null;
			} // 否则采用流输出
	
			java.io.BufferedInputStream bis=new java.io.BufferedInputStream(
											new java.io.FileInputStream(fileObj));
			//输出
			//set contentType
//			String mimeType=request.getSession().getServletContext().getMimeType(fileName);
//			if(mimeType==null){
//				throw new ServletException("非法的文件扩展名！");
//			}else{
//				response.setContentType(mimeType); 		
//			}

			//response.setContentType("text/html"); 
			response.setContentType("text/html;charset=gb2312"); // 解决中文乱码问题
			//output bytes
			ServletOutputStream sos = response.getOutputStream(); 
			int blobsize = 1024; 
			byte[] blobbytes = new byte[blobsize]; 
			int bytesRead = 0; 
			//read() Returns: the number of bytes read, or -1 if the end of the stream has been reached. 
			while ((bytesRead = bis.read(blobbytes))>0) { 
				sos.write(blobbytes, 0, bytesRead); 
			} 
		
			//response.setContentType("text/html");
			sos.flush(); 
			//inputimage.close(); 
		}catch(java.lang.Exception e){
			//throw new ServletException(e) ;
			request.setAttribute("common.failDialog.message","打开认证文件出错...");
			return mapping.findForward("common.failDialog");		//点表示目录、逻辑
		}
		
		return null;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
