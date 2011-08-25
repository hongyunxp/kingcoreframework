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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.codec.DecoderException;

/**
 * <p>Base64 编码、解码实现。 
 *    常用的方式是：
 *    		在客户端采用 js/Java 对提交url参数进行 Base64 编码；
 *    		在服务器端采用 Java 对获取的参数进行 Base64 解码；
 *    
 *     对应有 js 的实现，
 * 		参考“E:\Work_Java\apps_src\prj_vangv\vcmarket\manager\note_dhtml\js_util_Base64.html”。</p>
 *
 * 	参考列表：
 * 		org.apache.commons.codec.* 
 *
 * 
 * <pre>
//		// 根据操作系统的默认编码
		System.out.println( "----------------------------------操作系统的默认编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes()) );
		System.out.println( new String(Base64.decode( "YWJj1tDOxC09XC88Pg==")) );  // 必须要 new String，否则输出：[B@480457
		
//		// 根据utf-8 编码
		System.out.println( "----------------------------------采用utf-8 编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes("utf-8")) );
		System.out.println( new String(Base64.decode( "YWJj5Lit5paHLT1cLzw+"), "utf-8") );

//		// 根据 gbk 编码
		System.out.println( "----------------------------------采用gbk 编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes("gbk")) );
		System.out.println( new String(Base64.decode( "YWJj1tDOxC09XC88Pg=="), "gbk") );

//		// 根据第三方提供的 codec
		System.out.println( "----------------------------------采用 apache.commons.codec");
		org.apache.commons.codec.binary.Base64 cb64 = 
				new org.apache.commons.codec.binary.Base64();
		
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.encodeBase64( "abc中文-=\\/<>".getBytes("gbk")) ) );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.decodeBase64( "YWJj1tDOxC09XC88Pg==".getBytes()), "gbk") );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.encodeBase64( "abc中文-=\\/<>".getBytes("utf-8")) ) );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.decodeBase64( "YWJj5Lit5paHLT1cLzw+".getBytes()), "utf-8") );
		 
 * 
 * </pre>
 * 
 * @author Zeven on 2007-9-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class Base64 {

    private static char[] base64EncodeChars = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/' };
 
    private static byte[] base64DecodeChars = new byte[] {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };


    public static String encode(String data) throws UnsupportedEncodingException {
    	return encode(data , "utf-8");
    }

    public static String encode(String data,String charsetName) throws UnsupportedEncodingException {
    	return new String(org.apache.commons.codec.binary.Base64.encodeBase64(data.getBytes(charsetName)) , charsetName);
    }

    public static byte[] encode2(byte[] data) {
    	return org.apache.commons.codec.binary.Base64.encodeBase64( data );
    }
    /**
     * 编码。
     * 	   注意：输入之前已经确定了编码格式，输入参数 data 与编码无关。
     * @deprecated 不具体指定字符集而依赖于操作系统，在多系统协作情况下会有乱码问题。 请使用 public static byte[] encode2(byte[] data)
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
    	//org.apache.commons.codec.binary.Base64.encodeBase64( data )
        
    	StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * 
     * @param inputBytes
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] decode(byte[] inputBytes) throws UnsupportedEncodingException {
    	return org.apache.commons.codec.binary.Base64.decodeBase64(inputBytes);
    }

    public static String decode(String str, String charsetName) throws UnsupportedEncodingException {
    	return new String( decode(str.getBytes(charsetName)), charsetName);
    }

//  public static String decode(String str) throws UnsupportedEncodingException {
//  	return decode(str, "utf-8");
//  }
    
    /**
     * 解码。 
     *    注意：输输出内容与编码无关,输出之之后再编码成String 对象。
     *    // org.apache.commons.codec.binary.Base64.decodeBase64(passStr.getBytes("gbk") );
     * @deprecated 不具体指定字符集而依赖于操作系统，在多系统协作情况下会有乱码问题。请使用 public static byte[] decode(byte[] inputBytes)
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] decode(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            /* b1 */
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) break;
            /* b2 */
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) break;
            sb.append((char)((b1 << 2) | ((b2 & 0x30) >>> 4)));
            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61) return sb.toString().getBytes("ISO-8859-1");
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) break;
            sb.append((char)(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61) return sb.toString().getBytes("ISO-8859-1");
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) break;
            sb.append((char)(((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes("ISO-8859-1");
    }

    
	/**
	 * Test。
	 *  Zeven: 输入内容输入前的编码与输出内容输出之后的编码要一致，才能正确的编码和解码。
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws DecoderException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException,
						DecoderException {
		
		if(true){
			
			// 下面的使用 utf-8，编码之后带有 + 号，如果使用gbk，则带有 /。 记住：url编码最好使用encodeComponentURI 方法。
			// Base64不蛮适合 url参数的编码；
			// url的编码，最好使用encodeComponentURI 方法，是专用的，不要使用base64，Base64只是用于那些反复需要url编码的地方或者其他地方。
			System.out.println( Base64.encode( "城苛地  要在 在".getBytes("utf-8") ) );
			
			System.out.println( new String( org.apache.commons.codec.binary.Base64.encodeBase64("abc".getBytes("utf-8"), true ) ,"utf-8") );
				System.out.println(  (Base64.decode( "L3Nob3AvZ29vZHNEZWFsLmpodG1sP3NwZWNBbmROdW09MSTlsI8k6JOdQCZzcGVja2ZsYWc9MiZzYWxlbW9kZWlkPTImQWN0aW9uPXRvSm9pblRlYW1idXkmZ29vZHNpZD0xOTYyJnNhbGVpZD0yOTAxJnNob3BpZD0xMTAxJg==", "utf-8")) );
				return;
		}
		
//		// 根据操作系统的默认编码
		System.out.println( "----------------------------------操作系统的默认编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes()) );
		System.out.println( new String(Base64.decode( "YWJj1tDOxC09XC88Pg==")) );  // 必须要 new String，否则输出：[B@480457
		
//		// 根据utf-8 编码
		System.out.println( "----------------------------------采用utf-8 编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes("utf-8")) );
		System.out.println( (Base64.decode( "YWJj5Lit5paHLT1cLzw+", "utf-8")) );

//		// 根据 gbk 编码
		System.out.println( "----------------------------------采用gbk 编码");
		System.out.println( Base64.encode( "abc中文-=\\/<>".getBytes("gbk")) );
		System.out.println( (Base64.decode( "YWJj1tDOxC09XC88Pg==", "gbk")) );

//		// 根据第三方提供的 codec
		System.out.println( "----------------------------------采用 apache.commons.codec");
		org.apache.commons.codec.binary.Base64 cb64 = 
				new org.apache.commons.codec.binary.Base64();
		
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.encodeBase64( "abc中文-=\\/<>".getBytes("gbk")) ) );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.decodeBase64( "YWJj1tDOxC09XC88Pg==".getBytes()), "gbk") );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.encodeBase64( "abc中文-=\\/<>".getBytes("utf-8")) ) );
		System.out.println( new String( 
				org.apache.commons.codec.binary.Base64.decodeBase64( "YWJj5Lit5paHLT1cLzw+".getBytes()), "utf-8") );
		

		if (true) {	// ------------------------- 比较 Base64与Hex 性能。
			 
//			DecimalFormat mbFormat = new DecimalFormat("#0.00");
//			mbFormat.format("5.6666");
			/**
			 * 测试结论：
			 * 	1。采用100000次测试UTF-8的格式，Hex解码比Base64快一倍；
			 *  2。Base64的长度要比Hex短30%左右；
			 *  3。Hex性能好，适合较多计算；Base64数据短，适合传输；
			 */
			//测试Base64和Hex的编码性能
			String str = "abcdefghijklmnopkrst中华人民共和国~!@#$%^&*()_+";
			String strBase64 = new String(Base64.encode( str.getBytes("UTF-8") ) );
			String strHex = Hex.encode( str.getBytes("UTF-8") );
			if(true){
				long beginTime = System.currentTimeMillis();
				for (int i = 0; i < 100000; i++) {
					str = ( Base64.decode( strBase64,"UTF-8" )) ;
				}
				System.out.println( System.currentTimeMillis() - beginTime);
				System.out.println( str + "---"+ strBase64);
				
			}
			
			if(true){
				long beginTime = System.currentTimeMillis();
				for (int i = 0; i < 100000; i++) {
					str = new String( Hex.decode( strHex ),"UTF-8") ;
				}
				System.out.println( System.currentTimeMillis() - beginTime);
				System.out.println( str + "---"+ strHex);
				
			}
			
		}
	}

}
