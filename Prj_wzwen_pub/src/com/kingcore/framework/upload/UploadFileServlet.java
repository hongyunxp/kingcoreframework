package com.kingcore.framework.upload ;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadFileServlet extends HttpServlet
{
	//default maximum allowable file size is 100k
	static final int MAX_SIZE = 102400;
	//instance variables to store root and success message
	String rootPath, successMessage;
	/**
	 * init method is called when servlet is initialized.
	 */
	public void init(ServletConfig config) throws ServletException
	{
	super.init(config);
	//get path in which to save file
	rootPath = config.getInitParameter("RootPath");
	if (rootPath == null)
	{
	rootPath = "/";
	}
	/*Get message to show when upload is complete. Used only if
	a success redirect page is not supplied.*/
	successMessage = config.getInitParameter("SuccessMessage");
	if (successMessage == null)
	{
	successMessage = "File upload complete!";
	}
	}
	/**
	 * doPost reads the uploaded data from the request and writes
	 * it to a file.
	 */
	public void doPost(HttpServletRequest request,
	HttpServletResponse response)
	{
	ServletOutputStream out=null;
	DataInputStream in=null;
	FileOutputStream fileOut=null;
	try
	{
	/*set content type of response and get handle to output
	stream in case we are unable to redirect client*/
	response.setContentType("text/plain");
	out = response.getOutputStream();
	}
	catch (IOException e)
	{
	//print error message to standard out
	System.out.println("Error getting output stream.");
	System.out.println("Error description: " + e);
	return;
	}
	try
	{
	//get content type of client request
	String contentType = request.getContentType();
	//make sure content type is multipart/form-data
	if(contentType != null && contentType.indexOf(
	"multipart/form-data") != -1)
	{
	//open input stream from client to capture upload file
	in = new DataInputStream(request.getInputStream());
	//get length of content data
	int formDataLength = request.getContentLength();
	//allocate a byte array to store content data
	byte dataBytes[] = new byte[formDataLength];
	//read file into byte array
	int bytesRead = 0;
	int totalBytesRead = 0;
	int sizeCheck = 0;
	while (totalBytesRead < formDataLength)
	{
	//check for maximum file size violation
	sizeCheck = totalBytesRead + in.available();
	if (sizeCheck > MAX_SIZE)
	{
	out.println("Sorry, file is too large to upload.");
	return;
	}
	bytesRead = in.read(dataBytes, totalBytesRead,
	formDataLength);
	totalBytesRead += bytesRead;
	}
	//create string from byte array for easy manipulation
	String file = new String(dataBytes);
	//since byte array is stored in string, release memory
	dataBytes = null;
	/*get boundary value (boundary is a unique string that
	separates content data)*/
	int lastIndex = contentType.lastIndexOf("=");
	String boundary = contentType.substring(lastIndex+1,
	contentType.length());
	//get Directory web variable from request
	String directory="";
	if (file.indexOf("name=\"Directory\"") > 0)
	{
	directory = file.substring(
	file.indexOf("name=\"Directory\""));
	//remove carriage return
	directory = directory.substring(
	directory.indexOf("\n")+1);
	//remove carriage return
	directory = directory.substring(
	directory.indexOf("\n")+1);
	//get Directory
	directory = directory.substring(0,
	directory.indexOf("\n")-1);
	/*make sure user didn't select a directory higher in
	the directory tree*/
	if (directory.indexOf("..") > 0)
	{
	out.println("Security Error: You can't upload " +
	"to a directory higher in the directory tree.");
	return;
	}
	}
	//get SuccessPage web variable from request
	String successPage="";
	if (file.indexOf("name=\"SuccessPage\"") > 0)
	{
	successPage = file.substring(
	file.indexOf("name=\"SuccessPage\""));
	//remove carriage return
	successPage = successPage.substring(
	successPage.indexOf("\n")+1);
	//remove carriage return
	successPage = successPage.substring(
	successPage.indexOf("\n")+1);
	//get success page
	successPage = successPage.substring(0,
	successPage.indexOf("\n")-1);
	}
	//get OverWrite flag web variable from request
	String overWrite;
	if (file.indexOf("name=\"OverWrite\"") > 0)
	{
	overWrite = file.substring(
	file.indexOf("name=\"OverWrite\""));
	//remove carriage return
	overWrite = overWrite.substring(
	overWrite.indexOf("\n")+1);
	//remove carriage return
	overWrite = overWrite.substring(
	overWrite.indexOf("\n")+1);
	//get overwrite flag
	overWrite = overWrite.substring(0,
	overWrite.indexOf("\n")-1);
	}
	else
	{
	overWrite = "false";
	}
	//get OverWritePage web variable from request
	String overWritePage="";
	if (file.indexOf("name=\"OverWritePage\"") > 0)
	{
	overWritePage = file.substring(
	file.indexOf("name=\"OverWritePage\""));
	//remove carriage return
	overWritePage = overWritePage.substring(
	overWritePage.indexOf("\n")+1);
	//remove carriage return
	overWritePage = overWritePage.substring(
	overWritePage.indexOf("\n")+1);
	//get overwrite page
	overWritePage = overWritePage.substring(0,
	overWritePage.indexOf("\n")-1);
	}
	//get filename of upload file
	String saveFile = file.substring(
	file.indexOf("filename=\"")+10);
	saveFile = saveFile.substring(0,
	saveFile.indexOf("\n"));
	saveFile = saveFile.substring(
	saveFile.lastIndexOf("\\")+1,
	saveFile.indexOf("\""));
	/*remove boundary markers and other multipart/form-data
	tags from beginning of upload file section*/
	int pos; //position in upload file
	//find position of upload file section of request
	pos = file.indexOf("filename=\"");
	//find position of content-disposition line
	pos = file.indexOf("\n",pos)+1;
	//find position of content-type line
	pos = file.indexOf("\n",pos)+1;
	//find position of blank line
	pos = file.indexOf("\n",pos)+1;
	/*find the location of the next boundary marker
	(marking the end of the upload file data)*/
	int boundaryLocation = file.indexOf(boundary,pos)-4;
	//upload file lies between pos and boundaryLocation
	file = file.substring(pos,boundaryLocation);
	//build the full path of the upload file
	String fileName = new String(rootPath + directory +
	saveFile);
	//create File object to check for existence of file
	File checkFile = new File(fileName);
	if (checkFile.exists())
	{
	/*file exists, if OverWrite flag is off, give
	message and abort*/
	if (!overWrite.toLowerCase().equals("true"))
	{
	if (overWritePage.equals(""))
	{
	/*OverWrite HTML page URL not received, respond
	with generic message*/
	out.println("Sorry, file already exists.");
	}
	else
	{
	//redirect client to OverWrite HTML page
	response.sendRedirect(overWritePage);
	}
	return;
	}
	}
	/*create File object to check for existence of
	Directory*/
	File fileDir = new File(rootPath + directory);
	if (!fileDir.exists())
	{
	//Directory doesn't exist, create it
	fileDir.mkdirs();
	}
	//instantiate file output stream
	fileOut = new FileOutputStream(fileName);
	//write the string to the file as a byte array
	fileOut.write(file.getBytes(),0,file.length());
	if (successPage.equals(""))
	{
	/*success HTML page URL not received, respond with
	generic success message*/
	out.println(successMessage);
	out.println("File written to: " + fileName);
	}
	else
	{
	//redirect client to success HTML page
	response.sendRedirect(successPage);
	}
	}
	else //request is not multipart/form-data
	{
	//send error message to client
	out.println("Request not multipart/form-data.");
	}
	}
	catch(Exception e)
	{
	try
	{
	//print error message to standard out
	System.out.println("Error in doPost: " + e);
	//send error message to client
	out.println("An unexpected error has occurred.");
	out.println("Error description: " + e);
	}
	catch (Exception f) {}
	}
	finally
	{
	try
	{
	fileOut.close(); //close file output stream
	}
	catch (Exception f) {}
	try
	{
	in.close(); //close input stream from client
	}
	catch (Exception f) {}
	try
	{
	out.close(); //close output stream to client
	}
	catch (Exception f) {}
	}
	}
}





