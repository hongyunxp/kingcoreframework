/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util ;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>对String对象进行处理的工具类，所有的方法都是静态(static)的，不需要创建
 *			对象实例就可以调用。</p>
 * @author	WUZEWEN on 2004-09-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 **/


public class StringUtils {
	
	/**
	 * wzw: 转换 txt to link
	 * @param txt
	 * @return
	 */
	public static String convert2Link(String txt){
		return txt.replaceAll("(http|ftp|https)(://)\\w+\\.\\w+([\\.\\w+]*)([/\\w+]*)[\\.\\w+]*[?\\w+=\\w+][&\\w+=\\w+]*",
						"<a target='_blank' class='link_blueer12a' href='$0'>$0</a>"); //wzw:处理连接文本	

	}
	
	/**
	 * default Construtor.
	 *
	 */
	public StringUtils(){
	}
	
	
	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 *
	 * @param bytes an array of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	private static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
	
	/*
	 private static  String  toUTF_8_2(String inPara) throws Exception{
	 
	 char temChr;
	 String rtStr ="";
	 int i_char=0;
	 //java.net.
	  for(int i=0;i<inPara.length();i++)
	  {
	  temChr=inPara.charAt(i);
	  i_char = temChr + 0;
	  if( i_char<128 && i_char>0){
	  rtStr += temChr;
	  }else{
	  
	  //rtStr += "%"+toHex( temChr );
	   rtStr += java.net.URLEncoder.encode( temChr+"" );
	   } 
	   }
	   return rtStr;
	   
	   }
	   */
	
	/** toHex
	 *
	 * A function (method) in Java to convert integers (type int) to a hex string.
	 * This is one of my early contributions to Java. Back in Jan '96, the
	 * java.lang.Integer class lacked a toHexString method. This was a common
	 * request on Java newgroups and web pages of Java developers. Instead of
	 * making a package, I just wrote a function that people could cut and paste
	 * into their programs. Now that java.lang.Integer has a toHexString method,
	 * this is obsolete
	 *
	 * @version 1.00 1996/Feb/02
	 * Status: Obsolete. Use toHexString method of java.lang.integer.
	 *
	 * @author Rajiv Pant (Betul)   http://rajiv.org   betul@rajiv.org
	 *
	 */
	
	protected static  String toHex(int n)
	{
		String h = "" ;
		int r=0;
		int nn=n ;
		do
		{
			r=nn % 16 ;
			nn= nn / 16 ;
			switch (r)
			{
			case 10: h = "A" + h; break ;
			case 11: h = "B" + h; break ;
			case 12: h = "C" + h; break ;
			case 13: h = "D" + h; break ;
			case 14: h = "E" + h; break ;
			case 15: h = "F" + h; break ;
			default: h = r + h; break ;
			}
		}
		while (nn > 0) ;
		return h ;
	}
	
	
	/**
	 * <p>将指定字符串中所有的特定字符替换为新的字符，采用的是递归方法。
	 * 		this is as same as the StringUtils.replace method in common-lang.jar(Apache) file.</p>
	 * 
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
	 * @param text 需要替换的字符串
	 * @param repl 被替换的字符
	 * @param with 替换成字符
	 * @return 替换之后的字符串
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}
	/**
	 * <p>将指定字符串中所有的特定字符替换为新的字符，采用的是递归方法。
	 * 		this is as same as the StringUtils.replace method in common-lang.jar(Apache) file.</p>
	 * 
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
	 * @param text 需要替换的字符串
	 * @param repl 被替换的字符
	 * @param with 替换成字符
     * @param max  maximum number of values to replace, or <code>-1</code> if no maximum
	 * @return 替换之后的字符串
	 */
	public static String replace(String text, String repl, String with, int max) {    
		if (text == null || isEmpty(repl) || with == null || max == 0) {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int start = 0, end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buf.append(text.substring(start));
        return buf.toString();  
	} 
	
	
	/**
	 * <p>将字符串对象转换为数组对象。</p>
	 * 
	 * <pre>
	 * StringUtils.stringToArray("a,b,c", ",")		= {"a","b","c"}
	 * StringUtils.stringToArray("1_2_3", "_")		= {"1","2","3"}
	 * StringUtils.stringToArray("a$b$c", "$")		= {"a","b","c"}
	 * </pre>
	 * 
	 * @param s 要转换为数组的字符串
	 * @param c 分隔的字符串
	 * @return 转换之后的数组对象
	 */
	public static String[] stringToArray(String s,String c) {
		int i,j,len;
		String source,tmp;
		String[] value=null;
		Vector<String> v;
		if (s==null || s.length()<1)
			return value;
		v = new Vector<String>();
		source = s;
		j = 0;
		len = c.length();
		i = source.indexOf(c);
		//String.getto
		//tmp = new String[100];
		
		while (i>=0) {
			//tmp[j] = source.substring(0,i);
			tmp = source.substring(0,i);
			v.addElement(tmp);
			j = j + 1;
			source = source.substring(i+len,source.length());
			i = source.indexOf(c);
		}
		
		//tmp[j++] = source;
		tmp = source;
		v.addElement(tmp);
		j++;
		value = new String[j];
		for (i=0;i<j;i++)
			value[i] = (String)v.elementAt(i);

		//value = (String[]) v.toArray();   //Zeven on 2007-06-07
		
		return value;
	}
	
	/**
	 * <p>加密字符串。</p>
	 * @deprecated 
	 * @param s 要加密的字符串
	 * @throws Exception
	 * @return 加密之后的字符串
	 */
	public static  String  toSecretString(String s) throws Exception{
		byte[] bString=s.getBytes();
		byte b=0x68;
		for (int i=0;i<bString.length;i++)
			bString[i]=(byte)(bString[i]^b);
		return new String(bString,"GBK");
	}
	
	/**
	 * <P>Translate a string from ISO-8859-1 to GB2312 character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  toGB2312(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("ISO-8859-1");
		String sTmpStr = new String(temp_b,"GB2312");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to UTF-8 character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  toUTF_8(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("ISO-8859-1");
		String sTmpStr = new String(temp_b,"UTF-8");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from GB2312 to ISO-8859-1 character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  toISO(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("GB2312");
		String sTmpStr = new String(temp_b,"ISO-8859-1");
		return sTmpStr;
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  toGBK(String s) throws Exception{
		return toGBK(s, "ISO-8859-1");
	}
	
	/**
	 * <p>Translate a string from GB2312 to ISO-8859-1 character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  gbk2ISO(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("GB2312");
		String sTmpStr = new String(temp_b,"ISO-8859-1");
		return sTmpStr;
	}
	
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s 要转换的字符串
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  iso2GBK(String s) throws Exception{
		return toGBK(s, "ISO-8859-1");
	}
	
	/**
	 * <p>Translate a string from ISO-8859-1 to GBK character set.</p>
	 * @param s 要转换的字符串
	 * @param lang 现在的字符集
	 * @throws Exception
	 * @return 转换之后的字符串
	 */
	public static  String  toGBK(String s,String lang) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes(lang);
		String sTmpStr = new String(temp_b,"GBK");
		return sTmpStr;
	}
	
	/** 
	 * <p>把字符串转换成Unicode码，格式为\\uXXXX\\uXXXX</p> 
	 * @param strText 待转换的字符串(中英文、数字)
	 * @return 转换后的Unicode码字符串 
	 */ 
	public static String toUnicode(String strText) throws UnsupportedEncodingException{
		char c; 
		String strRet = "" ; 
		int intAsc; 
		String strHex;
		
		for ( int i = 0; i < strText.length(); i++ ){
			c = strText.charAt(i); 
			intAsc = (int)c; 
			// System.out.println(i+ "  intAsc ss="+intAsc);
			if(intAsc>128){ 
				strHex = Integer.toHexString(intAsc); 
				strRet = strRet + "\\u" + strHex.toUpperCase(); 	//&#x
			} else { 
				strHex = Integer.toHexString(intAsc); 
				strRet = strRet + "\\u00" + strHex.toUpperCase(); 	//&#x
			}
		}
		return strRet;
	}
	
	/**
	 * <p>获取字符串字节长度，因为插入到数据库中时，一个中文字符相当于两个英文字符。</p>
	 * 
	 * <pre>
	 * StringUtils.getByteLength("abc")		= 3
	 * StringUtils.getByteLength("中文")		= 4
	 * StringUtils.getByteLength("中文abc")	= 7
	 * StringUtils.getByteLength(null)		= 0
	 * StringUtils.getByteLength("")		= 0
	 * </pre>
	 * @param str 需要计算长度的字符串对象
	 * @return 字符串的字节长
	 */
	public static int getByteLength(String str){
		if( str==null) {
			return 0;
		}
		int count=0;
		for(int i=0; i<str.length() ; i++){
			//if(hello.value.charCodeAt(i)<=8192)
			if(str.charAt(i)<128 && str.charAt(i)>0){
				
				count+=1;//alert("单字节符:"+hello.value.charAt(i));
			}else{
				count+=2;// alert("双字节符:"+hello.value.charAt(i));
			}
		}
		return count ;
	}
	
	
	/**
	 * <p>检查一个 String 对象为null 或者"".</p>
	 *
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * <p>说明: 本方法不对String 对象执行trims操作.</p>
	 *
	 * @param str  要检查的字符串
	 * @return <code>true</code> 如果字符串既不是null，也不是""。
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * <p>计算子字符串在字符串中出现了多少次.</p>
	 *
	 * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
	 *
	 * <pre>
	 * StringUtils.countMatches(null, *)       = 0
	 * StringUtils.countMatches("", *)         = 0
	 * StringUtils.countMatches("abba", null)  = 0
	 * StringUtils.countMatches("abba", "")    = 0
	 * StringUtils.countMatches("abba", "a")   = 2
	 * StringUtils.countMatches("abba", "ab")  = 1
	 * StringUtils.countMatches("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param str  被检查的字符串，不能是 null
	 * @param sub  计算出现多少次的字符串，不能是 null
	 * @return 出现的次数，如果是null，返回0。
	 */
	public static int countMatches(String str, String sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != -1) {
			count++;
			idx += sub.length();
		}
		return count;
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// System.out.println( StringUtils.toUTF_8_2("user.jhtml?id=123&pw=a中b文c") );
		
		System.out.println( StringUtils.encodeHex("中".getBytes() ) );
		
		System.out.println("--" + StringUtils.replace("a'b''c", "'", "''"));
	}
}

