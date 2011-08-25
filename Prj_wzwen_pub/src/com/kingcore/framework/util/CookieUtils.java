/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util ;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;

import wzw.lang.Base64;

/**
 * This class contains a number of static methods that can be used to
 * work with javax.servlet.Cookie objects.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class CookieUtils {

	/**
	 * 日志处理对象。
	 */
	protected static Logger log = Logger.getLogger( com.kingcore.framework.util.CookieUtils.class);
	
    /**
     * Returns the value of the Cookie with the specified name,
     * or null of not found.
     *   根据请求找到请求cookie中名称为name 的cookie 值。 对cookie值做了Base64解码。
     * @param name cookie's name.
     * @param req response Object.
     * @return
     * @throws DecoderException 
     */
    public static String getCookieValue(String name, HttpServletRequest req) throws DecoderException {
		return getCookieValue(name ,req, true);
    }


    /**
     * Returns the value of the Cookie with the specified name,
     * or null of not found.
     *   根据请求找到请求cookie中名称为name 的cookie 值。
     * @param name cookie's name.
     * @param req response Object.
     * @param needEncode 是否需要对Cookie值解码，默认true.
     * @return
     * @throws DecoderException 
     */
    public static String getCookieValue(String name, HttpServletRequest req, boolean needDecode) throws DecoderException {
//    	check input information.
		if(name==null){
			return null;
		}

        Cookie[] cookies = req.getCookies();
		if(cookies==null){// 如果没有任何cookie
			//out.print("none any cookie");
			return null;
		}
        String value = null;
        for (int i = 0; i < cookies.length; i++) {
            if( name.equals( cookies[i].getName() ) ) {
                try {
                	value = cookies[i].getValue();
                	if(needDecode){
    					value = Base64.decode( value, "utf-8" ) ;
                	}
                	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					log.error("debug", e);
					//e.pri ntStackTrace();
				
				}	//new String( Hex.decode( cookies[i].getValue() ) );
                break;
            }
        }
        return value;
    }
    
    /**
     * Creates a Cookie with the specified name, value and max age,
     * and adds it to the response.
     * 		添加cookies  并且默认对cookie值做了Base64 编码。
     * @param name cookie's name.
     * @param value cookie's value.
     * @param maxAge the time cookie been keeped. the unit is second.
     *      age of the cookie in seconds,负数表示关闭浏览器即删除Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
     * @param res response Object.
     */
    public static void sendCookie(String name, String value, int maxAge,
    		HttpServletResponse response) {
    	sendCookie(name, value, maxAge, response, true);
    }
    
    /**
     * 
     * Creates a Cookie with the specified name, value and max age,
     * and adds it to the response.
     * 添加cookies  对cookie值做了Base64 编码。
     * @param name cookie's name.
     * @param value cookie's value.
     * @param maxAge the time cookie been keeped. the unit is second.
     *      age of the cookie in seconds,负数表示关闭浏览器即删除Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
     * @param res response Object.
     * @param needEncode 是否需要对Cookie值做Base64编码，默认true
     */
    public static void sendCookie(String name, String value, int maxAge,
			HttpServletResponse response, boolean needEncode) {
    	sendCookie(name, value, maxAge, response, needEncode, null);
    }

   /** 
    * Creates a Cookie with the specified name, value and max age,
    * and adds it to the response.
    * 添加cookies  对cookie值做了Base64 编码。
    * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
    * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
    * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
    * 		the server that sent them.
    * @param name cookie's name.
    * @param value cookie's value.
    * @param maxAge the time cookie been keeped. the unit is second.
    *      age of the cookie in seconds,负数表示关闭浏览器即删除Cookie.
	 *		an integer specifying the maximum age of the cookie in seconds; 
	 *		if negative, means the cookie is not stored; if zero, deletes the cookie.
    * @param res response Object.
    * @param needEncode 是否需要对Cookie值做Base64编码，默认true
    * @param domain Cookie's domain
    */
    public static void sendCookie(String name, String value, int maxAge,
    				HttpServletResponse response, boolean needEncode, String domain ) {

    	try {
    		if (needEncode) {
    			value = Base64.encode( value.getBytes("utf-8") );	//对应客户端解码
//    			 value = new String(Base64.encode( value.getBytes("utf-8")), "utf-8" );	//都使用utf-8
			}
			//System.out.println("value = " + value);
	        Cookie cookie = new Cookie(name, value);//Hex.encode(value.getBytes()) );
	        cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			if(domain!=null){
				cookie.setDomain( domain );	// 设置domain
			}
	        response.addCookie(cookie);
	        
		} catch (UnsupportedEncodingException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
		
    }

    
    /**
     *  不带 domain 的clearCookie。
     * clear a cookie from client side.
     * @param name the name of cookie will be cleared.
     * @param response HttpServletResponse Object.
     */
    public static void clearCookie(String name, HttpServletResponse response){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    
    /**
     * 带有 domain 的clearCookie。
     * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
     * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
     * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
     * 		the server that sent them.
     * @param name 需要清除Cookie的名字
     * @param response 响应对象
     * @param domain 在指定域清除Cookie，如果不指定则默认为当前域
     */
    public static void clearCookie(String name, HttpServletResponse response, String domain){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
		cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    /**
     * 带有 domain,path 参数的 clearCookie。
     * 	The form of the domain name is specified by RFC 2109. A domain name begins with a dot (.foo.com) 
     * 		and means that the cookie is visible to servers in a specified Domain Name System (DNS) zone 
     * 		(for example, www.foo.com, but not a.b.foo.com). By default, cookies are only returned to 
     * 		the server that sent them.
     * @param name 需要清除Cookie的名字
     * @param response 响应对象
     * @param domain 在指定域清除Cookie，如果不指定则默认为当前域
     * @param path 在指定目录清除Cookie，如果不指定则默认为根目录
     */
    public static void clearCookie(String name, HttpServletResponse response, 
    							   String domain, String path){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(path);
		cookie.setDomain(domain);
        response.addCookie(cookie);
    }
    
    
    /**
     * Returns true if a cookie with the specified name is
     * present in the request.
     * 判断是否有名称为name 的cookie， 对cooki值编码没有要求。
     * @param name the name of the cookie will be checked.
     * @param req response Object.
     * @return
     */
    public static boolean isCookieSet(String name, HttpServletRequest req) {
        try {
//        	check input information.
    		if(name==null){
    			return false;
    		}

            Cookie[] cookies = req.getCookies();
    		if(cookies==null){// 如果没有任何cookie
    			//out.print("none any cookie");
    			return false;
    		}
            for (int i = 0; i < cookies.length; i++) {
                if( name.equals( cookies[i].getName() ) ) {
                    return true;
                }
            }
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
		return false;
    }
}
