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

package wzw.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.codec.EncoderException;

/**
 * <p>包含对于 URL,URI对象的一些处理方法的类。
 * 
 * 	参考列表：
 * 		org.apache.commons.codec.* 
 * 
 * </p>
 * @author Zeven on 2007-5-24
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class UrlEncoder {

	/**
	 * <p>This is used for full-url encoding to some charset,default charset is UTF-8,other like GBK,BIG5.
	 * <pre>
	 * URLEncoder.encode("中文")								= "%E4%B8%AD%E6%96%87"
	 * URLEncoder.encode("getList.jhtml?id=123&name=中文")	= "getList.jhtml?id=123&name=%E4%B8%AD%E6%96%87"
	 * </pre>
	 * @param url
	 * @param currentEncodoing the default value is UTF-8,not ANSI(GBK,Big5,...)
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String encodeNonAscii( String url ) throws UnsupportedEncodingException {
		return encodeNonAscii( url, "UTF-8");
	}
	
	/**
	 * Zeven: 对url中的部分特殊字符转义，只是处理url中的非ascii码字符。 ascii字符不处理。
	 * @param url
	 * @param currentEncodoing
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeNonAscii( String url , String currentEncodoing) throws UnsupportedEncodingException {

	    char temChr;
		String rtStr ="";
		int i_char=0;
		//java.net.
	    for(int i=0;i<url.length();i++)
	    {
	      	temChr=url.charAt(i);
	      	i_char = temChr + 0;
	    	if( i_char<128 && i_char>0){
	      		rtStr += temChr;
	    	}else{

	    		//rtStr += "%"+toHex( temChr );
	     	 	rtStr += java.net.URLEncoder.encode( temChr+"" ,currentEncodoing ); // if no 'UTF-8' parameter,using platform's default encoding.
	    	} 
	    }
	    return rtStr;
	}
	

	public static String encode( String url ) throws UnsupportedEncodingException {
		return encode( url, "UTF-8");
	}
	/**
	 * Zeven，对url中的所有特殊字符转义，对应javaScript里面的encodeURIComponent。
	 * 	  "http://www.sina.com.cn/abc.jhtml?a=b&c=d"  -->
	 * 				http%3A%2F%2Fwww.sina.com.cn%2Fabc.jhtml%3Fa%3Db%26c%3Dd
	 * 
	 * @param url
	 * @param currentEncodoing
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode( String url , String currentEncodoing) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode( url ,currentEncodoing );
	}
	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws EncoderException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, EncoderException {
		String url = "http://www.sina.com.cn/abc.jhtml?a=b&c=d";
		System.out.println( UrlEncoder.encodeNonAscii( url ) );
		
		// wzw package
		System.out.println( UrlEncoder.encode( url ) );
		
		// apache common package
		org.apache.commons.codec.net.URLCodec urlc = new org.apache.commons.codec.net.URLCodec();
		System.out.println( UrlEncoder.encodeNonAscii( urlc.encode( url) ) );

		// java.net package
		System.out.println( java.net.URLEncoder.encode(url,"gbk")  );
		System.out.println( java.net.URLEncoder.encode(url,"UTF-8") );
		
	}

}
