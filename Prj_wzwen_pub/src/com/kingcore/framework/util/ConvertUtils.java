/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ConvertUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    private static DecimalFormat numberFormat = new DecimalFormat();
	
	/**
	 * 数据库类型
	 */
	public static String database="ORA";			//默认为oracle数据库，如果设置oracle为gb2312，就不需要转换了。
	
	/**
	 * 设置数据库类型
	 * 2003-6-5
	 * @param String 数据库类型
	 * @return void
	 * @throws Exception
	 */
	public static void setDatabase(String db) {
		database = db;
	}
	/**
	 * 根据数据库类型转化字符串
	 * Create on 2003-5-20
	 * @param String 需转换的字符串
	 * @return String
	 * @throws Exception
	 * Modify on 2003-6-5
	 */
	public static String ConvertString(String str) throws Exception {
		String convertString = "";
		byte[] tmp;
		try {
			if (database.equals("SYB")) {
				tmp = str.getBytes("ISO-8859-1");
				convertString = new String(tmp);
			} else {
				convertString = str;
			}
		} catch (Exception e) {
		}
		return convertString;
	}
	/**
	 * 格式化数字的包装方法
	 * Create on 2003-6-18
	 * @param Double 数字
	 * @param int 保留几位有效小数 
	 * @return
	 */
	public static String formatNumber(Double d, int scalar) throws Exception {
		double temp = d.doubleValue();
		return formatNumber(temp, scalar);
	}
	/**
	 * 格式化数字的包装方法
	 * Create on 2003-6-18
	 * @param Float 数字
	 * @param int 保留几位有效小数 
	 * @return
	 */
	public static String formatNumber(Float d, int scalar) throws Exception {
		float temp = d.floatValue();
		return formatNumber(temp, scalar);
	}
	/**
	 * 格式化数字
	 * Create on 2003-6-18
	 * @param double 数字
	 * @param int 保留几位有效小数
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(double number, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(number);
	}
	/**
	 * 格式化数字
	 * Create on 2003-6-18
	 * @param float 数字
	 * @param int 保留几位有效小数
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(float number, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(number);
	}
	/**
	 * 格式化金额
	 * @param double 金额
	 * @param int 保留几位有效小数
	 * @param int 舍入时以多少位舍入的标准
	 * @return String
	 */
	public static String formatMoney(double money, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(money);
	}
	/**
	 * 格式化金额
	 * @param String 金额
	 * @param int 保留几位有效小数
	 * @param int 舍入时以多少位舍入的标准
	 * @return String
	 */
	public static String formatMoney(float money, int scalar)
		throws Exception {
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(money);
	}
	/**
	 * 格式化金额
	 * @param String 金额
	 * @param int 保留几位有效小数
	 * @param boolean 是否入
	 * @param int 舍入时以多少位舍入的标准
	 * @return String
	 */
	public static String formatMoney(
		String money,
		int scalar,
		boolean isround,
		int standard)
		throws Exception {
		String formatMoney = null;
		if (isround)
			formatMoney = round(money, scalar, standard);
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		java.text.NumberFormat nf = new java.text.DecimalFormat(format);
		return (nf.format(formatMoney));
	}
	
	/**
	 * 格式化金额
	 * @param double 金额
	 * @param int 保留几位有效小数
	 * @param boolean 是否入
	 * @param int 舍入时以多少位舍入的标准
	 * @return String
	 */
	public static String formatMoney(
		double money,
		int scalar,
		boolean isround,
		int standard)
		throws Exception {
		String formatMoney = null;
		if (isround)
			formatMoney = round(String.valueOf(money), scalar, standard);
		String zero = "000000000000000000000000000000";
		String format = "###,##0." + zero.substring(0, scalar);
		java.text.NumberFormat nf = new java.text.DecimalFormat(format);
		return (nf.format(formatMoney));
	}
	
	/**
	 * 格式化金额
	 * @param String 金额
	 * @param int 保留几位有效小数
	 * @param boolean 是否四舍五入
	 * @return String
	 */
	public static String formatMoney(String money, int scalar, boolean isround)
		throws Exception {
		return formatMoney(money, scalar, isround, 5);
	}
	/**
	 * 格式化金额
	 * @param String 金额
	 * @param int 保留几位有效小数
	 * @param boolean 是否四舍五入
	 * @return String
	 */
	public static String formatMoney(double money, int scalar, boolean isround)
		throws Exception {
		return formatMoney(String.valueOf(money), scalar, isround, 5);
	}
	/**
	 * 舍入方法
	 * 可以根据自定义的数值来做舍和入，
	 *  例如：以6作为舍入的标准，那么小于6的都舍掉，
	 *  大于或等于6的都进位。
	 * @param double 传入要做舍入操作的数字字符串
	 * @param int 保留小数点后几位
	 * @param int 作为标准的数
	 * @return String
	 */
	public static double round(double value, int scalar, int standard)
		throws Exception {
		return Double.parseDouble(
			round(String.valueOf(value), scalar, standard));
	}
	
	public static String round(String value, int scalar, int standard)
		throws Exception {

		String back = null;
		int point = value.indexOf(".");
		if (point > -1) {
			int len = value.length() - point + 1;
			if (len <= scalar) {
				back = value;
			} else {
				back = value.substring(0, point + scalar + 1);
				String vsTemp =
					value.substring(point + scalar + 1, point + scalar + 2);
				if (Integer.parseInt(vsTemp) >= standard) {
					back = String.valueOf(Integer.parseInt(back) + 1);
				}
			}
		} else {
			back = value;
		}
		return back;
	}
	/**
	 * 中文金额的大写转换的包装函数
	 * @param String 金额
	 * @return String
	 */
	public static String makeUpperCaseSum(String je) {
		return makeUpperCaseSum(Double.parseDouble(je));
	}
	/**
	 * 中文金额的大写转换
	 * @param double 金额
	 * @return String
	 */
	public static String makeUpperCaseSum(double je) {

		if (je < 0)
			je *= -1;

		final String[] upper_number =
			{ "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾" };
		final String[] number_dw =
			{
				"",
				"圆",
				"拾",
				"佰",
				"仟",
				"万",
				"拾",
				"佰",
				"仟",
				"亿",
				"拾",
				"佰",
				"仟",
				"万" };
		String operate, upper_str, vsDx;
		int i, j, point_pos, int_len;
		if (je < 0.01)
			return "零圆整";

		upper_str = "";

		DecimalFormat aFormat =
			(DecimalFormat) new java.text.DecimalFormat("##0.00");
		operate = new String(aFormat.format(je));

		point_pos = operate.indexOf(".");
		if (point_pos == -1)
			int_len = operate.length();
		else {
			if (operate.length() - point_pos <= 2)
				operate += "0";
			int_len = point_pos;
		}
		if (int_len > number_dw.length - 1)
			return "too long ！！！";

		if (je > 0) {
			for (i = 0; i < int_len; i++) {
				j = int_len - i;
				if (i > 0 && "0".equals(operate.substring(i, i + 1))) {
					String sss = number_dw[j];
					if (!"0".equals(operate.substring(i - 1, i))
						&& !"万".equals(sss)
						&& !"亿".equals(sss)
						&& !"圆".equals(sss))
						upper_str = upper_str + "零";
					else if (
						"万".equals(number_dw[j])
							|| "亿".equals(number_dw[j])
							|| "圆".equals(number_dw[j])) {
						int ssss = upper_str.length() - 1;
						String sssss = upper_str.substring(ssss);
						if (!sssss.equals("零")) {
							if (!upper_str
								.substring(upper_str.length() - 1)
								.equals("亿")
								&& !upper_str.substring(
									upper_str.length() - 1).equals(
									"万")) {
								if (upper_str
									.substring(upper_str.length() - 1)
									.equals("拾")
									&& !number_dw[j].equals("圆"))
									upper_str = upper_str + number_dw[j] + "零";
								else
									upper_str = upper_str + number_dw[j];
							}
						} else {
							if (!upper_str
								.substring(
									upper_str.length() - 2,
									upper_str.length() - 1)
								.equals("亿")
								&& !upper_str.substring(
									upper_str.length() - 2,
									upper_str.length() - 1).equals(
									"万")
								|| number_dw[j].equals("圆")) {
								upper_str =
									upper_str.substring(
										0,
										upper_str.length() - 1)
										+ number_dw[j];
								if (!number_dw[j].equals("圆")) {
									upper_str = upper_str + "零";
								}
							}
						}
					}
				} else {
					if (!operate.substring(i, i + 1).equals("0")) {
						int k = Integer.parseInt(operate.substring(i, i + 1));
						upper_str =
							upper_str + upper_number[k - 1] + number_dw[j];
					}
				}
			}
		}
		if (point_pos > 0) {
			if (!operate.substring(point_pos + 1, point_pos + 2).equals("0")) {
				int k =
					Integer.parseInt(
						operate.substring(point_pos + 1, point_pos + 2));
				upper_str = upper_str + upper_number[k - 1] + "角";
				if (!operate
					.substring(point_pos + 2, point_pos + 3)
					.equals("0")) {
					int m =
						Integer.parseInt(
							operate.substring(point_pos + 2, point_pos + 3));
					upper_str = upper_str + upper_number[m - 1] + "分";
				}
			} else {
				if (!operate
					.substring(point_pos + 2, point_pos + 3)
					.equals("0")) {
					int k =
						Integer.parseInt(
							operate.substring(point_pos + 2, point_pos + 3));
					upper_str = upper_str + "零" + upper_number[k - 1] + "分";
				}
			}
		}

		if (!upper_str.substring(upper_str.length() - 1).equals("分")
			&& !upper_str.equals(""))
			upper_str = upper_str + "整";

		return upper_str;
	}
	
	/**
	 * 将中英文字串转换成纯英文字串共用静态方法
	 * 
	 * 
	 */
	public static String toTureAsciiStr(String str)
	{  // 定义将中英文字串转换成纯英文字串共用静态方法
    	StringBuffer sb=new StringBuffer();
    	byte[] bt=str.getBytes();                       // 把要转换的字符串转换为字节形式
    	for(int i=0;i<bt.length;i++){
    	  if(bt[i]<0){                                  // 判断是否为汉字，如是则去高位1
    	    sb.append((char)(bt[i]&(0x7f)));
    	  }
      	  else{                                         // 是英文字符补0作记录
        	sb.append((char)0);
        	sb.append((char)bt[i]);
      }
    }
    return  sb.toString();                          // 返回转换后的英文字符串
  }
	
	/**
	 *
	 * 将经转换的字串还原方法
	 *
	 */
	public static String unToTrueAsciiStr(String str)
	{// 定义将经转换的字串还原方法
    	byte[] bt=str.getBytes() ;
    	int i;
    	int l=0;
    	int length=bt.length;
    	int j=0;
    	for(i=0;i<length;i++){                          // 判断有几个英文字符以去除Byte 0
    	  if(bt[i]==0){
    	    l++;
    	    }
    	}
    	byte[] bt2=new byte[length-l];                  // 定义返回的字节数组
    	for(i=0;i<length;i++){
    	  if(bt[i]==0){                                 // 是英文字符去掉0
    	    i++;
    	    bt2[j]=bt[i];
    	  }
    	  else{                                         // 是汉字补上高位1
    	    bt2[j]=(byte)(bt[i]|0x80);
      	}
      	j++;
    	}
    	String tt=new String(bt2);
    	return tt;                                     // 返回还原后的字符串
  }

	/**将一般的字符串转为 HTML字符串
	 *
	 *
	 *
	 */  
	public static String toHTMLString(String in) {
	    StringBuffer out = new StringBuffer();
	    for (int i = 0; in != null && i < in.length(); i++) {
	        char c = in.charAt(i);
	        if (c == '\'') {
	            out.append("&#39;");
	        }
	        else if (c == '\"') {
	            out.append("&#34;");
	        }
	        else if (c == '<') {
	            out.append("&lt;");
	        }
	        else if (c == '>') {
	            out.append("&gt;");
	        }
	        else if (c == '&') {
	            out.append("&amp;");
	        }
	        else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}
	
	/**
	 * Converts a String to a Date, using the specified pattern.
	 * (see java.text.SimpleDateFormat for pattern description)
	 *
	 * @param dateString the String to convert
	 * @param dateFormatPattern the pattern
	 * @return the corresponding Date
	 * @exception ParseException, if the String doesn't match the pattern
	 */
	public static Date toDate(String dateString, String dateFormatPattern) 
	    throws ParseException {
	    Date date = null;
	    if (dateFormatPattern == null) {
	        dateFormatPattern = "yyyy-MM-dd";
	    }
	    synchronized (dateFormat) { 
	        dateFormat.applyPattern(dateFormatPattern);
	        dateFormat.setLenient(false);
	        date = dateFormat.parse(dateString);
	    }
	    return date;
	}
	
	/**
	 * Converts a String to a Number, using the specified pattern.
	 * (see java.text.NumberFormat for pattern description)
	 *
	 * @param numString the String to convert
	 * @param numFormatPattern the pattern
	 * @return the corresponding Number
	 * @exception ParseException, if the String doesn't match the pattern
	 */
	public static Number toNumber(String numString, String numFormatPattern) 
	    throws ParseException {
	    Number number = null;
	    if (numFormatPattern == null) {
	        numFormatPattern = "######.##";
	    }
	    synchronized (numberFormat) { 
	        numberFormat.applyPattern(numFormatPattern);
	        number = numberFormat.parse(numString);
	    }
	    return number;
	}
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// System.out.println( StringUtils.toUTF_8_2("user.jhtml?id=123&pw=a中b文c") );
		String str =null;
		str = "ab'cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		str = "ab\"cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		str = "ab\"'''\"cd";
		System.out.println( ConvertUtils.toHTMLString( str ) );
		
		//System.out.println("--" + "a'b''c".replaceAll("[']", "''"));

		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "ab'cd".replaceAll("[\"]", "\\\"") );
		System.out.println( "--------------" );
		str = "ab'cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "'", "\\'") );

		str = "ab\"cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );

		str = "ab\"\"cd";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );

		str = "ab\"c\"d";
		System.out.println( str );
		System.out.println( org.apache.commons.lang.StringUtils.replace(str, "\"", "\\\"") );
		
	}

}
