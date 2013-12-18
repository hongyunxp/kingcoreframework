/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * <p>对象格式化工具。</p>
 * 
 * <pre>
 * fmt.formatObject( new Double("12345678"),"￥##,##0.##")      =￥12,345,678
 * fmt.formatObject( new Double("12345678"),"￥##,##0.00")      =￥12,345,678.00
 * fmt.formatObject( new Double("12345678"),"##,##0.0#")       =12,345,678.0
 * fmt.formatObject( new Double("12345678"),"##0.000")         =12345678.000
 * </pre>
 * @author	WUZEWEN on 2005-09-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 **/

public class Formatter {


	/**
	 * 日志处理对象。
	 */
	protected static Logger log = Logger.getLogger( wzw.text.Formatter.class);

    /**
     * Format value according to specified format string (as tag attribute or
     * as string from message resources) or to current user locale.
     *
     * When a format string is retrieved from the message resources,
     * <code>applyLocalizedPattern</code> is used. For more about localized
     * patterns, see
     * <http://www.dei.unipd.it/corsi/fi2ae-docs/source/jdk1.1.7/src/java/text/resources/>.
     * (To obtain the correct value for some characters, you may need to view
     * the file in a hex editor and then use the Unicode escape form in the
     * property resources file.)
     *
     * @param valueToFormat value to process and convert to String
     * @exception JspException if a JSP exception has occurred
     */
    public static String formatObject(Object valueToFormat) throws JspException {
    	//String formatString="";
    	return formatObject( valueToFormat, null);
    }

    public static String formatObject(Object valueToFormat,String formatString) throws JspException {
    	
    	//Locale locale = Locale.getDefault();//TagUtils.getInstance().getUserLocale(pageContext, null);//this.localeKey
        return formatObject( Locale.getDefault(), valueToFormat, formatString);
    }
    
    /**
     * 格式化对象。
     * @param locale 本地风格类
     * @param valueToFormat 要格式化的对象
     * @param formatString 格式化的模式
     * @return 格式化之后的值
     * @throws JspException
     */
    public static String formatObject(Locale locale,Object valueToFormat,String formatString) throws JspException {
    	if(valueToFormat==null || valueToFormat.toString().trim().equals("")){
    		return "";
    	}
    	
    	Format format = null;
    	Object value = null;
		if(value instanceof String){	//支持参数为String
			value =Double.valueOf( (String)valueToFormat );
		}else{
			value = valueToFormat;
		}
		
    	boolean formatStrFromResources = true;
    	//Locale locale=Locale.getDefault();
    	
    	try{
    		//  	if (value instanceof java.lang.String) {
    		//  	return (String) value;
    		//  	} else {
    	
	    	// Prepare format object for numeric values.
	    	if (value instanceof Number || value instanceof String) {
	    			
	    		if (formatString == null) {
	    			if ((value instanceof Byte)
	    					|| (value instanceof Short)
	    					|| (value instanceof Integer)
	    					|| (value instanceof Long)
	    					|| (value instanceof BigInteger)) {
	    				
	    				formatString = "######"; 
	    				
	    			} else if (
	    					(value instanceof Float)
	    					|| (value instanceof Double)
	    					|| (value instanceof BigDecimal)) {
	    				
	    				formatString = "######.00";
	    			}
	    			
	    			if (formatString != null) {
	    				formatStrFromResources = true;
	    			}
	    		}
	    		
	    		if (formatString != null) {
	    			try {
	    				
	    				format = NumberFormat.getNumberInstance(locale);
	    				if (formatStrFromResources) {
	    					((DecimalFormat) format).applyLocalizedPattern(
	    							formatString);
	    				} else {
	    					((DecimalFormat) format).applyPattern(formatString);
	    				}
	    				
	    			} catch (IllegalArgumentException e) {
	    				JspException ex =
	    					new JspException("write.format "+formatString );
	    				//TagUtils.getInstance().saveException(pageContext, ex);
	    				throw ex;
	    			}
	    		}
	    		
	    	} else if (value instanceof java.util.Date) {
	    		
	    		/*   if (formatString == null) {
	    		 
	    		 if (value instanceof java.sql.Timestamp) {
	    		 formatString =
	    		 retrieveFormatString(SQL_TIMESTAMP_FORMAT_KEY);
	    		 
	    		 } else if (value instanceof java.sql.Date) {
	    		 formatString = retrieveFormatString(SQL_DATE_FORMAT_KEY);
	    		 
	    		 } else if (value instanceof java.sql.Time) {
	    		 formatString = retrieveFormatString(SQL_TIME_FORMAT_KEY);
	    		 
	    		 } else if (value instanceof java.util.Date) {
	    		 formatString = retrieveFormatString(DATE_FORMAT_KEY);
	    		 }
	    		 
	    		 }
	    		 
	    		 if (formatString != null) {
	    		 format = new SimpleDateFormat(formatString, locale);
	    		 }*/
	    	}
	    	
	    	if (format != null) {
	    		return format.format(value);
	    	} else {
	    		return value.toString();
	    	}
	    	
    	}catch( JspException e){

			log.info("The format value is:["+value.toString()+"]" );
			log.debug("debug", e );
    		/// e.pri ntStackTrace();
    		throw e;
    	}
    	
    }

	/**
	 * @param args
	 * @throws JspException 
	 */
	public static void main(String[] args) throws Exception {
		
		/// Formatter fmt = new Formatter();

		System.out.println( StringUtils.rightPad("fmt.formatObject( new Double(\"12345678\"),\"￥##,##0.##\") ", 60," ") + "=" 
												+ Formatter.formatObject( new Double("12345678"),"￥##,##0.##") );

		System.out.println( StringUtils.rightPad("fmt.formatObject( new Double(\"12345678\"),\"￥##,##0.00\") ", 60," ") + "=" 
												+ Formatter.formatObject( new Double("12345678"),"￥##,##0.00") );

		System.out.println( StringUtils.rightPad("fmt.formatObject( new Double(\"12345678\"),\"$##,##0.0#\") ", 60," ") + "=" 	
												+ Formatter.formatObject( new Double("12345678"),"$##,##0.0#") );

		System.out.println( StringUtils.rightPad("fmt.formatObject( new Double(\"12345678\"),\"##0.000\") ", 60," ") + "=" 
												+ Formatter.formatObject( new Double("12345678"),"##0.000") );
		
		//System.out.println( StringUtils.rightPad("fmt.formatObject.add(1.2, 1.32) ", 35," ")
		//		  						  + "=" + fmt.formatObject( ("12345678"),"##0.000") );

	}

}
