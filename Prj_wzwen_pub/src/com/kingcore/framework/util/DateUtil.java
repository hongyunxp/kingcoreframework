package com.kingcore.framework.util ;


import java.util.Calendar;
import java.util.Date;


public class DateUtil {

	//返回要存到数据库里的日期字符串
	//即：将yyyy-mm-dd格式变为yyyymmdd格式
	public static String formatDateForDB(String str){
	    if(!isLegalDate(str)) return null;
	    return str.substring(0,4)+str.substring(4,6)+str.substring(6,8);
	}

	//返回显示给用户的格式的日期
	//即：将yyyymmdd格式变为yyyy/mm/dd格式
	public static String formatDateForUser(String date){
	  return formatDateForUser( date, "-");
	}

	//返回显示给用户的格式的日期
	//即：将yyyymmdd格式变为用户指定的格式格式
	public static String formatDateForUser(String date,String del){
	    if (null == date)
	       return "&nbsp;";
	    if ( date.length() < 8 )
	       return date;
	    return date.substring(0,4)+del+date.substring(4,6)+del+date.substring(6,8);
	}

	private static String formatString(int i, int j)
	{
	    String s;
	    for (s = String.valueOf(i); s.length() < j; s = "0" + s);
	    return s;
	}

	public  static String[] getYMDFromDateStr( String s )
	{
	 if (s ==null || s.length() < 8)
	    return null;
	 String[] ss = new String[3];
	 ss[0] = s.substring(0,4);
	 ss[1] = s.substring(4,6);
	 ss[2] = s.substring(6,8);
	 return ss;
	}

	//返回要存到数据库里的时间字符串
	//即：将hh:mm:ss格式变为hhmmss格式
	public static String formatTimeForDB(String time){
	    return time.substring(0,2)+time.substring(2,4)+time.substring(4,6);
	}
	//返回显示给用户的格式的时间
	//即：将hhmmss格式变为hh:mm:ss格式
	public static String formatTimeForUser(String time){
	  if (time ==null)
	     return "&nbsp;";

	  if( time.length() < 4)
	     return time;
	  if (time.length() == 4)
	     return time.substring(0,2)+":"+time.substring(2,4);
	  if (time.length() == 6)
	     return time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
	  return time;
	}
	//返回显示给用户的格式的日期
	//即：将yyyymm格式变为yyyy/mm格式
	public static String formatYMDateForUser(String date){
	    return date.substring(0,4)+"/"+date.substring(4,6);
	}

    /**
     * @deprecated format to Date object to String accordding to the given patten<br>
     *		the pattern like 'yyyy-MM-dd';'yyyy.MM.dd'
     * @author WUZEWEN on 2005-07-17
     * @param String pattern
     * @exception Exception
     */
    public String formatDate(java.util.Date date,String pattern) throws Exception{
        //替换getCurdate(int type)
        return new java.text.SimpleDateFormat(pattern).format(date) ;
    }

	public static String getCerrentDandT()
	{
	    return getCurrentDate("")+getCurrentTime("").substring(0,4);
	}

	/**
	* 此处插入方法说明。
	* 创建日期：(2001-8-24 15:26:09)
	*/
	public static String getCurrentDate() {
	    return getXXXDate(0,"-");
	}
	public static String getCurrentDate( String del)
	{
	    return getXXXDate(0,del);
	}

	//返回当前日期
	public static Date getCurrentDateFormatData(){
	    Date date=new Date();
	    //
	    return date;
	}
	//返回当前时间 ，格式为hhmmss格式
	public static String getCurrentTime(){
	     return getXXXTime(":");
	}
	/**
	* 此处插入方法说明。
	* 创建日期：(2001-8-24 15:26:09)
	*/
	public static String getCurrentTime(String del){
	     return getXXXTime(del);

	}

	//返回参数n天以前的相对某个日期的日期 ，格式为yyyymmdd格式
	//  public static String getDateBeforeSomeDate(String dateStr,int offsetDay,String x){
	//	Calendar calendar = Calendar.getInstance();
	//	Date someDate = new Date(2002,06,03);
	//        someDate.set
	//        calendar.add(5, -offsetDay); //设置提前时间还是晚几天i=0 is today
	//	int j = calendar.get(1);
	//	int k = calendar.get(2) + 1;
	//	int l = calendar.get(5);
	//	return j + x + formatString(k, 2) + x + formatString(l, 2);
	//  }



	//返回参数number天以前的日期 ，格式为yyyymmdd格式 day 大于零表示提前天数
	public static String getDateBeforeDay(int day){
	      String  dateStr = getXXXDate(day,"-");
	      return dateStr;
	}
	public static String getDateBeforeDay(int day,String del){
	      String  dateStr = getXXXDate(day,del);
	      return dateStr;
	}
	//返回参数number月以前的日期 ，格式为yyyymmdd格式
	public static String getDateBeforeMonth(int month){
	      String  dateStr = getYYYDate(month,"-");
	      return dateStr;
	}


	private static String getXXXDate(int i, String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(5, -i); //设置提前时间还是晚几天i=0 is today
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + x + formatString(k, 2) + x + formatString(l, 2);
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
	* 此处插入方法说明。
	* 创建日期：(2001-8-24 15:26:09)
	*/
	//提前几个月，晚几个月
	private static String getYYYDate(int i, String x)
	{
	    //if (x.equals("") || x == null)
	    //	x = "-";
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(2, -i); //设置提前几个月还是晚几个月i=0 是本月
	    int j = calendar.get(1);
	    int k = calendar.get(2) + 1;
	    int l = calendar.get(5);
	    return j + x + formatString(k, 2) + x + formatString(l, 2);
	}

	//判断给定的字符串是否是一个合法的日期表示
	//本系统中的日期表示均为：YYYYMMDD。 ?表示任一字符
	//注：这个格式是用户输入和显示给用户的，实际存到数据库里时要去掉?
	public static boolean isLegalDate(String str){
	    String tmp=str.trim();
	    //if(tmp.length()!=8) return false;
	    try{
	      int year=Integer.parseInt(tmp.substring(0,4));
	      if(year<1900||year>3000) return false;
	      int month=Integer.parseInt(tmp.substring(4,6));
	      if(month<1||month>12) return false;
	      int day=Integer.parseInt(tmp.substring(6,8));
	      if(day<1) return false;
	      if(month==2){
	            if((year%400==0)||((year%4==0)&&(year%100!=0))){
	              if(day>29) return false;
	            }
	            else
	              if(day>28) return false;
	      }else if(day>(30+(month%2)))
	            return false;
	    }catch(Exception e){
	      return false;
	    }
	    return true;
	}
	    //private static Calendar cal_ = Calendar.getInstance(new Locale("zh","CN"));
	    public static void main(String[] s) {
	    }
}
