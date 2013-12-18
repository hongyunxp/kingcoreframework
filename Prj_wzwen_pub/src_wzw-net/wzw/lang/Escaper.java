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

package wzw.lang;

import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * <p> 特殊字符转义处理器类 
 * 
 * 	参考列表：
 * 		org.apache.commons.lang.* 
 * 
 * </p>
 * @author Zeven on 2007-6-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class Escaper {

	/**
	 * Zeven: Java 字符输出为 xml 的内容。
	 * 	对于xml值的处理，还有一个方法： <text><![CDATA[需要载入的内容]]></text>
	 * 
	 * org.apache.commons.lang.StringEscapeUtils.escapeXml(arg0);
	 *	 Escapes the characters in a String using XML entities.
	 * @param in
	 * @return
	 */
	public static String escape2Xml(String in) {
		return toXmlString( in ,false );
	}
	
//	/**
//	 * @deprecated replaced by escapeXml.
//	 * @param in
//	 * @return
//	 */
//	public String toXmlValue(String in) {
//		return toXmlString( in ,false );
//	}
	
	/**
	 *  same to escapeJavaScript( escapeXml(in) ).
	 * Java 字符输出为 xml 的内容，又做为Script的值，使用引号引起来，比如用在 JScript,Java 中 。
	 * @param in
	 * @return
	 */
	public static String escape2XmlForJavaScript(String in) {
		return toXmlString( in, true);
	}
	
	/**
	 * Zeven: 封装一层比直接使用第三方要好，比较适应变动，如果变动只需要改变这个类，
	 * 			不需要改变第三方或者大量使用的地方。 
	 * 将一般的字符串转为 HTML字符串代码。
	 * @asParam html代码是否会作为一个参数
	 *
	 */
	private static String toXmlString(String in, boolean asParam) {
		
		if( asParam ){

//			return StringEscapeUtils.escapeXml( 
//						StringEscapeUtils.escapeJavaScript(in) ) ;
			return StringEscapeUtils.escapeXml(escape2JavaScript(in) ) ;
		}else{
			return StringEscapeUtils.escapeXml(in) ;
			
		}
		
//		if(in==null) {
//			return null;
//		}
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//				if(asParam){
//					out.append("\\&#39;");
//				}else{
//					out.append("&#39;");
//				}
//	        }
//	        else if (c == '\"') {
//				if(asParam){
//					out.append("\\&#34;");
//				}else{
//					out.append("&#34;");
//				}
//	        }
//	        else if (c == '<') {
//	            out.append("&lt;");
//	        }
//	        else if (c == '>') {
//	            out.append("&gt;");
//	        }
//	        else if (c == '&') {
//	            out.append("&amp;");
//	        }
//	        else if (c == '\\') {
//				if(asParam){
//					// if just as a html code 
//					out.append("\\\\");
//				}else {
//		            out.append( c );
//				}
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	
	}
	

	/**
	 * <p>Zeven : escape java string to javaScript string.
	 * 		> 只需要处理 ' "" 这两个引号，避免与js冲突；
	 * 		> apache.common 的escapeJavaScript方法转换内容过多；
	 * 
	 * </p>
	 * @param in
	 * @return
	 */
	public static String escape2JavaScript(String in) {

		if(in==null) {
			return null;
		}

		return in.replaceAll("['|\\|\"]", "\\\\$0"); //wzw 正则表达式

//		///return org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(in);
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//	            //out.append("\\&#39;");
//	            out.append("\\'");
//	        }
//	        else if (c == '\"') {
//	            //out.append("\\&#34;");
//	            out.append("\\\"");
//	        }
////	        else if (c == '<') {
////	            out.append("\\&lt;");
////	        }
////	        else if (c == '>') {
////	            out.append("\\&gt;");
////	        }
////	        else if (c == '&') {
////	            out.append("\\&amp;");
////	        }
//	        else if (c == '\\') {
//	            out.append("\\\\");
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	}

	
	/**
	 * Zeven : escape java string to sql string.
	 * 		说明：	针对标准Sql 的 写法。
	 * 		  对于Oracle 数据库可以；
	 * 		  但是对于MySql数据库，需要转义"'"、"\"，则不能用；
	 * @param in
	 * @return
	 */
	public static String escape2Sql(String in) {
		return org.apache.commons.lang.StringEscapeUtils.escapeSql(in);
	}
	
	
	/**
	 *
	 * Zeven: 将一般的字符串转为 HTML字符串代码，作为标签的值。
	 * @asParam html代码是否会作为一个参数
	 * org.apache.commons.lang.StringEscapeUtils.escapeHtml(arg0);
	 *
	 */  
	public static String escape2Html(String in) {
		return toHtmlString( in, false);
	}
	
	
	/**
	 * escapeJavaScript( escapeHtml(in) )
	 * 将一般的字符串转为 HTML字符串代码，输出的代码是作为js的变量值。
	 * @asParam html代码是否会作为一个参数
	 *
	 */
	public static String escape2HtmlForJavaScript(String in) {
		return toHtmlString( in, true);
	}

	/**
	 *
	 * 将一般的字符串转为 HTML字符串代码。
	 * @asParam html代码是否会作为一个参数
	 *
	 */
	private static String toHtmlString(String in, boolean asParam) {

		if( asParam ){

//			return StringEscapeUtils.escapeHtml( 
//						StringEscapeUtils.escapeJavaScript(in) ) ;
			
			return StringEscapeUtils.escapeHtml( escape2JavaScript(in) ) ;
			
		}else{
			return StringEscapeUtils.escapeHtml(in) ;
			
		}
		
//		if(in==null) {
//			return null;
//		}
//	    StringBuffer out = new StringBuffer();
//	    for (int i = 0; in != null && i < in.length(); i++) {
//	        char c = in.charAt(i);
//	        if (c == '\'') {
//	        	//System.out.println("is '");
//				if(asParam){
//					out.append("\\&#39;");
//				}else{
//					out.append("&#39;");
//				}
//	        }
//	        else if (c == '\"') {
//				if(asParam){
//					out.append("\\&#34;");
//				}else{
//					out.append("&#34;");
//				}
//	        }
//	        else if (c == '<') {
//	            out.append("&lt;");
//	        }
//	        else if (c == '>') {
//	            out.append("&gt;");
//	        }
//	        else if (c == '&') {
//	            out.append("&amp;");
//	        }
//	        else if (c == '\\') {
//				if(asParam){
//					// if just as a html code 
//					out.append("\\\\");
//				}else {
//		            out.append( c );
//				}
//	        }
//	        else {
//	            out.append(c);
//	        }
//	    }
//	    return out.toString();
	
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println( Escaper.escape2Html("\"") );
		System.out.println( Escaper.escape2Html("") );
		
		// java正则表达式：
		String in="fdfdsf'\\'''fd\"'";
		System.out.println( in.replaceAll("['|\\|\"]", "\\\\$0")); //wzw：替换'"\三字符
		
		System.out.println("fdff[face02]fdaf".replaceAll("\\[face([0-9]{2})\\]",
			"<img border=\"0\" src=\"/images/face/$1.gif\">"));  //wzw：替换表情代码

		System.out.println("abchttp://fa.com.cn.cn/ftd/dfa.fdfd fdf".replaceAll("(http|ftp|https)(://)\\w+\\.\\w+([\\.\\w+]*)([/\\w+]*)",
						"<a target='_blank' class='link_blueer12a' href='$0'>$0</a>"));  //wzw：替换test为url
		
		System.out.println("\"\'\\fsd");
		
		in = "select * from user where code='ab\\c'";
		System.out.println( Escaper.escape2Sql(in) );
		System.out.println( Escaper.escape2Xml("&") );
	}

}
