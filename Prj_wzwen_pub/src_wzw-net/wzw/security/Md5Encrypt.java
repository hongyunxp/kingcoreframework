/**
 * Alipay.com Inc. Copyright (c) 2004-2005 All Rights Reserved.
 * 
 * <p>
 * Created on 2005-7-9
 * </p>
 */
package wzw.security;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加密算法
 * Zeven: 采用utf-8字符集，在不同的操作系统平台没有中文问题。
 * 	MD5的结果可能因为以下因数而不同：长度32/16、大小写、采用字符集、初始值等。
 * 
 */
public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String text) {
		return md5(text, "utf-8");
	}
	/**
	 * 
	 * @param text 加密的明文 
	 * @param charsetName 采用字符集
	 * @return
	 */
	public static String md5(String text, String charsetName) {
		byte[] bytes = null;
		try {
			bytes = text.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}
		return md5( bytes );
	}

	public static String md5(byte[] inputBytes ) {
		return md5(inputBytes, "utf-8");
	}
	
	/**
	 * 
	 * @param inputBytes
	 * @param charsetName 采用字符集
	 * @return
	 */
	public static String md5(byte[] inputBytes, String charsetName) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}
		msgDigest.update( inputBytes );

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	
	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

    public static void main(String args[])
    {
        if(Array.getLength(args) == 0)
        {
            System.out.println("MD5 Test suite:");
            System.out.println("MD5(\"中华人民共和国\"):" + Md5Encrypt.md5("中华人民共和国"));
            System.out.println("MD5(\"中华人民共和国\"):" + new MD5().getMD5ofStr("中华人民共和国")); 
            
            System.out.println("MD5(\"中文\"):" + Md5Encrypt.md5("中文"));
            System.out.println("MD5(\"a\"):" + Md5Encrypt.md5("a"));
            System.out.println("MD5(\"abc\"):" + Md5Encrypt.md5("abc"));
            System.out.println("MD5(\"message digest\"):" + Md5Encrypt.md5("message digest"));
            System.out.println("MD5(\"abcdefghijklmnopqrstuvwxyz\"):" + Md5Encrypt.md5("abcdefghijklmnopqrstuvwxyz"));
            System.out.println("MD5(\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"):" + Md5Encrypt.md5("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
        } else
        {
            System.out.println("MD5(" + args[0] + ")=" + Md5Encrypt.md5(args[0]));
        }
    }
}
