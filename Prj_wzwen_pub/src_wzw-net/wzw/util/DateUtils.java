/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * <p>日期操作工具集。</p>
 * 
 * <pre>
 * getCurrentDate()                   =2007-06-07
 * getCurrentDate('.')                =2007.06.07
 * getCurrentTime()                   =11:13:44
 * getCurrentTime('.')                =11.13.44
 * getCerrentDateAndTime()            =2007-06-07 11:13:44
 * getYear(“2002-09-13”)              =2002
 * getYear(“2002-9-13”)               =2002
 * getYear(“2002-9-1”)                =2002
 * getMonth(“2002-09-13”)             =09
 * getMonth(“2002-9-13”)              =9
 * getMonth(“2002-9-1”)               =9
 * getDay(“2002-09-13”)               =13
 * getDay(“2002-9-13”)                =13
 * getDay(“2002-9-1”)                 =1
 * getDay(“2002-9-01”)                =01
 * getDay(“2002-9-1 12:02:05”)        =1
 * </pre>
 * 
 * @author WZWEN on 2006-09-01
 *
 */
public class DateUtils {	

	/**
	 * 获取当前日期，返回的默认格式为 yyyy-mm-dd
	 * @return 当前日期字符串
	 */
	public static String getCurrentDate(){
	    return getXXXDate(0,"-");
	}
	
	/**
	 * 获取当前日期，可以指定yyyy, mm, dd 之间的连接符
	 * @param sep 分割符，如"-",".","/"
	 * @return
	 */
	public static String getCurrentDate( String sep)
	{
	    return getXXXDate(0,sep);
	}
	
	/**
	 * 获取日期，这是一个私有方法。
	 * @param i 需要提前的天数，单位为天
	 * @param sep 连接符，如"-",".","/"
	 * @return
	 */
	private static String getXXXDate(int i, String sep)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(5, -i); //设置提前时间还是晚几天i=0 is today
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + sep + formatString(k, 2) + sep + formatString(l, 2);
	}
	/**
	 * 将月份，日期，可能是一位的时候，补充到指定的长度，如 '2'补充为'02'。
	 * @param i 初始字符串
	 * @param j 补充到的长度
	 * @return
	 */
	private static String formatString(int i, int j)
	{
	    String s;
	    for (s = String.valueOf(i); s.length() < j; s = "0" + s);
	    return s;
	}

	/**
	 * 提前几个月，晚几个月
	 * @param i 提前的月数
	 * @param sep 分割符号
	 * @return
	 */
	private static String getYYYDate(int i, String sep)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(2, -i); //设置提前几个月还是晚几个月i=0 是本月
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + sep + formatString(k, 2) + sep + formatString(l, 2);
	}
	
	/**
	 * 获取当前时间，默认格式为 hh:mi:ss
	 * @return 当前时间字符串，如 '12:15:59'
	 */
	public static String getCurrentTime(){
	     return getXXXTime(":");
	}
	
	/** 
	 * 获取当前时间
	 * @param sep 指定小时、分钟、秒之间的连接字符串
	 * @return
	 */
	public static String getCurrentTime(String sep){
	     return getXXXTime(sep);

	}
	private static String getXXXTime(String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = ":";
	    Calendar calendar = Calendar.getInstance();
	    int h = calendar.get(11);
	    int m = calendar.get(12);
	    int s = calendar.get(13);
	    return formatString(h, 2) + x + formatString(m, 2) + x + formatString(s, 2);
	}
	
	/**
	 * 获取日期加时间
	 * @return 返回日期和时间字符串，如 '2006-09-12 11:25:30'
	 */
	public static String getCerrentDateTime()
	{
	    return getCurrentDate()+" "+getCurrentTime();	//.substring(0,4)
	}


	/**
	 * 返回参数number天以前的日期 ，格式为yyyymmdd格式 day 大于零表示提前天数
	 * @param day 提前的天数
	 * @return
	 */
	public static String getDateBeforeDay(int day){
	      String  dateStr = getXXXDate(day,"-");
	      return dateStr;
	}
	/**	 * 
	 * 返回参数number天以前的日期 ，格式为yyyymmdd格式 day 大于零表示提前天数
	 * @param day 提前的天数
	 * @param sep 分割符
	 * @return
	 */
	public static String getDateBeforeDay(int day,String sep){
	      String  dateStr = getXXXDate(day,sep);
	      return dateStr;
	}
	
	/**
	 * 返回参数number月以前的日期 ，格式为yyyymmdd格式
	 * @param month
	 * @return
	 */
	public static String getDateBeforeMonth(int month){
	      String  dateStr = getYYYDate(month,"-");
	      return dateStr;
	}
	
	/**
	 * 获取日期字符串的 year 部分，日期字符串一般是使用to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date 日期，如 2006-09-20，默认使用'-'连接
	 * @return
	 */
	public static String getYear(String str_date){
		return getYear(str_date,"-");
	}
	public static String getYear(String str_date,String sep){
		return str_date.substring( 0, str_date.indexOf(sep));
	}

	/**
	 * 获取日期字符串的 month 部分，日期字符串一般是使用to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date 日期，如 2006-09-20，默认使用'-'连接
	 * @return
	 */
	public static String getMonth(String str_date){
		return getMonth(str_date,"-");
	}
	public static String getMonth(String str_date,String sep){
		return str_date.substring( str_date.indexOf(sep)+1, str_date.lastIndexOf(sep) );
	}

	/**
	 * 获取日期字符串的 day 部分，日期字符串一般是使用to_char( DateColn,'yyyy-mm-dd')
	 * @param str_date 日期，如 2006-09-20，默认使用'-'连接
	 * @return
	 */
	public static String getDay(String str_date){
		return getDay(str_date,"-");
	}
	public static String getDay(String str_date,String sep){
		if(str_date.indexOf(" ")>0){
			return str_date.substring( str_date.lastIndexOf(sep)+1,str_date.indexOf(" "));			
		}else{
			return str_date.substring( str_date.lastIndexOf(sep)+1 );			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// 各静态方法测试如下：
		int pad = 35;
		System.out.println( StringUtils.rightPad("getCurrentDate()", pad," ") + "="
												+ getCurrentDate());
		
		System.out.println( StringUtils.rightPad("getCurrentDate('.')", pad," ") + "="
												+ getCurrentDate("."));
		
		System.out.println( StringUtils.rightPad("getCurrentTime()", pad," ") + "="
												+ getCurrentTime());
		System.out.println( StringUtils.rightPad("getCurrentTime('.')", pad," ") + "="
												+ getCurrentTime("."));
		
		System.out.println( StringUtils.rightPad("getCerrentDateAndTime()", pad," ") + "="
												+ getCerrentDateTime());

		System.out.println( StringUtils.rightPad("getYear(“2002-09-13”)", pad," ") + "="
												+ getYear("2002-09-13"));
		System.out.println( StringUtils.rightPad("getYear(“2002-9-13”)", pad," ") + "="
												+ getYear("2002-9-13"));
		System.out.println( StringUtils.rightPad("getYear(“2002-9-1”)", pad," ") + "="
												+ getYear("2002-9-1"));

		System.out.println( StringUtils.rightPad("getMonth(“2002-09-13”)", pad," ") + "="
												+ getMonth("2002-09-13"));
		System.out.println( StringUtils.rightPad("getMonth(“2002-9-13”)", pad," ") + "="
												+ getMonth("2002-9-13"));
		System.out.println( StringUtils.rightPad("getMonth(“2002-9-1”)", pad," ") + "="
												+ getMonth("2002-9-1"));
		
		System.out.println( StringUtils.rightPad("getDay(“2002-09-13”)", pad," ") + "="
												+ getDay("2002-09-13"));
		System.out.println( StringUtils.rightPad("getDay(“2002-9-13”)", pad," ") + "="
												+ getDay("2002-9-13"));
		System.out.println( StringUtils.rightPad("getDay(“2002-9-1”)", pad," ") + "="
												+ getDay("2002-9-1"));
		System.out.println( StringUtils.rightPad("getDay(“2002-9-01”)", pad," ") + "="
												+ getDay("2002-9-01"));
		System.out.println( StringUtils.rightPad("getDay(“2002-9-1 12:02:05”)", pad," ") + "="
												+ getDay("2002-9-1 12:02:05"));
		
	}

}
