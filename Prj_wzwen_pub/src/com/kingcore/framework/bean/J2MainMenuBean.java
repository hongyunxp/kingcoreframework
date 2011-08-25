/*
 *
 * Copyright (c) 1998- personal Zewen.Woo
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */

package com.kingcore.framework.bean;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * @version		1.00 2004.05.26
 * @author		Zewen.Woo
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */

public class J2MainMenuBean
{

	//the position of menu config file
	protected static String filePath = "jsp/conf/main_menu.xml";
	public HttpSession session  ;
	public HttpServletRequest request  ;

	//list of private
	//String strPri = (String)session.getValue("UserModList");

	static { System.out.print("this is execute only when first load") ; }

	public J2MainMenuBean()
	{
		//System.out.print("\n now construction!") ;
		//sbHtml = new StringBuffer[10] ;
	}

	/**
	 *	parser the xmlfile
	 *  @param fileURI				fileName
	 *  @param sbHtml				put the context
	 */
	private void parserXMLFile(String fileURI,StringBuffer sbHtml )
	{
		StringBuffer sb01 = new StringBuffer() ;
		//StringBuffer sb02 = new StringBuffer() ;
		StringBuffer sb03 = new StringBuffer() ;

		sb01
				.append("<TABLE cellSpacing=\"0\" cellPadding=\"0\" width=\"130\">")
				.append("<TBODY>")
				.append("<TR style=\"CURSOR: hand\">") ;

		sb03
				.append("</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>");

		try
		{
			//create a new Document
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(new File( fileURI ));
			//get the Namespace
			//Namespace ns = Namespace.getNamespace("LIT", "http://www.lit.edu.cn/student/");
			//get the root Element

			Element menuInfo = doc.getRootElement();
			//get list of 01
			//p( "next menu is empty! " +elmtStuInfo.getName()) ;
			List menu01s = menuInfo.getChildren("menu");
			//p( "next menu is empty! " +menu01s.size()) ;

			//list_01s
			for (int i = 0; i < menu01s.size(); i++)
			{
				//one of list_01s
				Element menu01 = (Element)menu01s.get(i);

				if (menu01.getChildren("menu").isEmpty())
				{
					//p( "next menu is empty!") ;
					//p( menu01) ;
					continue ;
				}
				//list_02s
				List menu02s = menu01.getChildren("menu" ) ;
				for( int j=0 ;j< menu02s.size(); j++)
				{
					//one of list_02s
					Element menu02 = (Element)menu02s.get(j) ;

//------------------------	Level One menu ---------
					if (menu02.getAttribute("href") != null && menu02.getAttribute("href").getValue() != "")
					{
						sbHtml
								.append("<tr level=\"" + menu02.getAttribute("id").getValue() +"\"")
								.append(" flagHasUrl='false' onclick='showHasUrlHideSubMenu();'>" )
								.append(" <td onmouseoverNull=\"menuUrlOver(this)\"  onmouseoutNull=\"menuUrlOut(this)\" class=\"menuHasUrl\" style=\"cursor:hand;text-align:center;height:20;\"")
								.append(" onclick=\"doLinkHasUrlSubMenu(\"" + menu02.getAttribute("url").getValue() +"\")>" )
								.append(" " + menu02.getAttribute("text").getValue() + "</td></tr>" ) ;
					}
					else
					{
								//System.out.println("\naaa");

								sbHtml
										.append( sb01 )
										.append("<TD class=\"menu_title\" onmouseover=\"this.className='menu_title2';\"")
										.append("onclick=\"menuChange(this," + menu02.getAttribute("id").getValue() + ");\" onmouseout=\"this.className='menu_title';\"")
										.append("background=\"" + menu02.getAttribute("background").getValue() + "\" height=\"25\"><SPAN>")
										.append(menu02.getAttribute("text").getValue())
										.append("</SPAN></TD></TR>")
										.append("<TR><TD><DIV class=\"sec_menu\" ")
										.append("id=\""+menu02.getAttribute("id").getValue()+"\" style=\"WIDTH:130px;display:none\">")
										.append("<TABLE cellSpacing=\"0\" cellPadding=\"0\" width=\"120\" align=\"center\"><TBODY>") ;
					}

//-----------------------------	Level One menu end ----------

						if (!menu02.getChildren("menu" ).isEmpty())
						{
							//list_03s
							List menu03s = menu02.getChildren("menu" ) ;
							for( int k=0; k<menu03s.size(); k++)
							{
								//on of list_03s
								Element menu03 = (Element)menu03s.get(k) ;

//-----------------------------	Level Two menu ----------
										//System.out.println("\nbbb");

										sbHtml
												.append("<TR height=\"20\"><TD>")
												.append("<img src=\""+menu03.getAttribute("src").getValue()+"\">")
												.append("<A href=\"" +menu03.getAttribute("href").getValue()+"\" target=\"Right\">")
												.append(menu03.getAttribute("text").getValue() + "</A>")
												.append("</TD></TR>") ;

//-----------------------------	Level Two menu end ----------

								if (!menu03.getChildren("menu").isEmpty())
								{
									//p( "next menu is empty!") ;
									p( "Warning:the depth of the menu file out of 3 layers!" ) ;
								}
							}
						}
						sbHtml.append(sb03) ;
				}

				//p(sbHtml.toString()) ;
			}

		}
		catch (JDOMException jdome)
		{
			System.out.println(jdome.getMessage());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void p( String str )
	{
		System.out.print( "\n"+ str );
	}

	public static void p( char[] s )
	{
		System.out.print( "\n"+ s );
	}
	public static void p( Element menu )
	{

	}

    /**
     * Process an HTTP "GET" request.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGeta(HttpServletRequest request,
              HttpServletResponse response)
        throws IOException, ServletException {


      	System.out.print("\n doGet()") ;
	//	return ;
    //    process(request, response);

    }


    /**
     * Process an HTTP "POST" request.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPosta(HttpServletRequest request,
               HttpServletResponse response)
        throws IOException, ServletException {

      System.out.print("\n doPost()") ;
		//return ;
      //  process(request, response);

    }
    /**
     * Perform the standard request processing for this request, and create
     * the corresponding response.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception is thrown
     */
    protected void doProcess(StringBuffer sbHtml ){

		String fileURI = null ;
		//StringBuffer sbHtml = new StringBuffer() ;
		fileURI = this.request.getRealPath( getFilePath()) ;

		sbHtml.append("<table border=\"0\" cellpadding=\"0\" ") ;
		sbHtml.append("cellspacing=\"0\" width=\"100%\">") ;

		//System.out.println("Usage:java MyJDOM [XML file URI]");

		addFunctions( ) ;
		parserXMLFile( fileURI , sbHtml) ;

		sbHtml.append("</table>") ;

		//response.setContentType("text/html;charset=gbk") ;
		//request.setCharacterEncoding("GBK") ;
		//PrintWriter out = response.getWriter() ;
		//out.print( sbHtml.toString() ) ;
    }


   public void inita(ServletConfig config) throws ServletException{
      //super.init(config);

      System.out.print("\n init()") ;
   }

	// return String
	public String toString()
	{
		StringBuffer sbHtml = new StringBuffer() ;
		//for(int i=0;i<10;i++)
		//	sbHtml[i] = new StringBuffer() ;
		//sbHtm = new StringBuffer() ;
		doProcess(sbHtml) ;
		//p("html   "+ sbHtml.toString());

		return sbHtml.toString() ;
	}

	// add javascript functions here!
	private void addFunctions(){

		return ;
	}

	//	getter and setter for session
	public void setSession(HttpSession session)
	{
		this.session = session ;
	}
	public HttpSession getSession()
	{
		return this.session ;
	}
	//	getter and setter for request
	public void setRequest(HttpServletRequest request)
	{
		this.request = request ;
	}
	public HttpServletRequest getRequest()
	{
		return this.request;
	}

	//	getter and setter for filePath
	public void setFilePath(String filePath)
	{
		this.filePath = filePath ;
	}
	public String getFilePath()
	{
		return this.filePath;
	}

}
