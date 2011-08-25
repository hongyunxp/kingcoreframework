package com.kingcore.framework.util ;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

public class HtmlCalendarNotePad
{
public static final int MONDAY_FIRST = 1; //设置每星期是以星期一为第一天
public static final int SUNDAY_FIRST = 2; //设置每星期是以星期日为第一天
private Hashtable config;
public int  htmlCalendarYear,htmlCalendarMonth, htmlCalendarStyle ;

private static final String Mnth[] = {

"一月", "二月", "三月", "四月", "五月", "六月",
"七月", "八月", "九月", "十月", "十一月", "十二月"
};

private int year;
private int month;
private int style;
private String sFont;
private Locale loc;
private static String NEWLINE = "\n";



public HtmlCalendarNotePad () //构造函数
{
sFont = null;
GregorianCalendar gCalendar = new GregorianCalendar(); //?玫鼻暗娜绽ǜ窭锔呃锢?
config = new Hashtable(); //创建新的哈希表储存配置信息
NEWLINE = System.getProperty("line.separator");
style = 2; //设置每星期是以星期日为第一天
month = gCalendar.get(2); //?迷路?
year = gCalendar.get(1); //?哪攴?
loc = Locale.PRC; //设置国家名，默认为中华人民共和国
}

private String formatObject(String s, Object obj)
{
String s1 = "";
if(obj != null)
s1 = String.valueOf(String.valueOf(obj));
if(s == null)
return s1;
else
return s + s1 + "</font>";
}

private int getDay(Calendar calendar) //?媚橙赵谌绽械奈恢?
{
if(style == 2)
return calendar.get(7) - 1;
else
return (calendar.get(7) + 5)%7;
}

public String getHtml()
{
GregorianCalendar gCalendar = new GregorianCalendar(year, month - 1, 1);
GregorianCalendar gCalendar1 = new GregorianCalendar(2001, 3, 24);
SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE", loc);
//设置缩写格式，EEE是星期的缩写，如 Sun, 若EEEE则为Sunday。
int i = month - 1;
int j = 0;
StringBuffer stringbuffer = new StringBuffer(""); //创建新的字符串缓冲区
stringbuffer.append("<table><tr>\n"); //在stringbuffer上添加<table><tr>，为创建表格作准备
if(style == 2) //见上解释
{
stringbuffer.append("<th align=right>" + formatObject(sFont, simpledateformat.format(gCalendar1.getTime())) + "</th>\n");
gCalendar1.add(5, 1);
for(int k = 1; k < 7; k++)
{
stringbuffer.append("<th align=right>" + formatObject(sFont, simpledateformat.format(gCalendar1.getTime())) + "</th>\n");
gCalendar1.add(5, 1);
}

}
else
{
gCalendar1.add(5, 1);
stringbuffer.append("<th align=right>" + formatObject(sFont, simpledateformat.format(gCalendar1.getTime())) + "</th>\n");
for(int l = 2; l < 8; l++)
{
gCalendar1.add(5, 1);
stringbuffer.append("<th align=right>" + formatObject(sFont, simpledateformat.format(gCalendar1.getTime())) + "</th>\n");
}

}
stringbuffer.append("</tr>\n");
int i1 = 0;
j = 0;
if(getDay(gCalendar) > 0)
{
stringbuffer.append("<tr>");
for(; i1 < getDay(gCalendar); i1++)
{
stringbuffer.append("<td align=right>");
if(sFont != null)
stringbuffer.append(sFont + " </font>");
else
stringbuffer.append(" ");
stringbuffer.append("</td>\n");
j++;
}

}
for(; gCalendar.get(2) == i; gCalendar.add(5, 1))
{
int j1 = gCalendar.get(5);
int k1 = (i1 + j1)%7;
if(k1 == 1)
{
stringbuffer.append("<tr>" + NEWLINE);
j = 0;
}
stringbuffer.append("<td align=right>");
j++;
if(sFont != null)
stringbuffer.append(sFont);
String s;
if((s = (String)config.get(String.valueOf(j1))) != null)
{
stringbuffer.append("<a href=\"");
if(s.toUpperCase().startsWith("HTT") || s.indexOf(".") > 0)
{
stringbuffer.append(s);
if(s.indexOf("?") < 0)
stringbuffer.append("?date=" + stringDate(gCalendar));
else
stringbuffer.append("&date=" + stringDate(gCalendar));
}
else
{
stringbuffer.append("javascript:" + s + "('" + stringDate(gCalendar) + "');");
}
stringbuffer.append("\"");
if((s = (String)config.get(j1 + "target")) != null)
stringbuffer.append(" target=\"" + s + "\"");
stringbuffer.append(">");
stringbuffer.append(gCalendar.get(5));
stringbuffer.append("</a>\n");
}
else
{
stringbuffer.append(String.valueOf(j1));
}
if(sFont != null)
stringbuffer.append("</font>");
stringbuffer.append("</td>\n");
if(k1 == 0)
stringbuffer.append("</tr>\n");
}

if(j < 7)
{
for(; j < 7; j++)
{
stringbuffer.append("<td align=right>");
if(sFont != null)
stringbuffer.append(sFont);
stringbuffer.append(" ");
if(sFont != null)
stringbuffer.append("</font>");
stringbuffer.append("</td>\n");
}

stringbuffer.append("</tr>\n");
}
stringbuffer.append("</table>\n");
return stringbuffer.toString();
}

public Locale getLocale() //获?厍?
{
return loc;
}
public int getYear() //?媚攴?
{
return htmlCalendarYear;
}
public int getMonth() //?迷路?
{
return htmlCalendarMonth;
}

public int getStyle() //?萌绽难?
{
return htmlCalendarStyle;
}
//设置动作的URI，target_frame 的值可以为_blank、 _parent、 _top、 self。
public void setAction(int day, String actionUri, String target_frame)
{
if(actionUri != null)
{
config.put(String.valueOf(day), actionUri);
if(target_frame != null && target_frame.length() > 0)
config.put(day + "target", target_frame);
}
}
//设置一个月的所有天的超链接
public void setActions(String actionUri, String target_frame)
{
for(int day = 1; day <= 31; day++)
setAction(day, actionUri, target_frame);

}
//设置地区
public void setLocale(Locale locale)
{
loc = locale;
}
//设置年份
public void setYear(int htmlCalendarYear)
{
if(htmlCalendarYear > 0)
{
year = htmlCalendarYear;
config.clear();
}
}
//设置月份
public void setMonth(int htmlCalendarMonth)
{
if(htmlCalendarMonth >= 1 && htmlCalendarMonth <= 12)
{
month = htmlCalendarMonth;
config.clear();
}
}
//设置日历的样式
public void setStyle(int htmlCalendarStyle)
{
style = htmlCalendarStyle;
}
private String stringDate(Calendar calendar)
{
String strDay = String.valueOf(calendar.get(1));
return strDay + twoDigits(calendar.get(2) + 1) + twoDigits(calendar.get(5));
}
private String twoDigits(int day) //为了日历中数字能够对齐，所以1-9 前将加0
{
String stringDay = String.valueOf(day); //?胐ay的值
if(stringDay.length() == 1) //如果字符串长度为1
return "0" + stringDay; //则在字符串前加零
else
return stringDay;
}
}
