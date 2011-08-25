/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util;


/**
 * <p>判断一个String对象是否符合各种数据类型</p>
 * @author	zewen.wu on 2004.03.18
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckUtils { 	

    private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    private static DecimalFormat numberFormat = new DecimalFormat();

	public static boolean isValidDate(String dateString, String dateFormatPattern) {
	    
	    Date validDate = null;
	    synchronized (dateFormat) { 
	        try {
	            dateFormat.applyPattern(dateFormatPattern);
	            dateFormat.setLenient(false);
	            validDate = dateFormat.parse(dateString);
	        }
	        catch (ParseException e) {
	            // Ignore and return null
	        }
	    }
	    return validDate != null;
	}
	
	/**
	 * Returns true if the specified number string represents a valid
	 * integer in the specified range.
	 *
	 * @param numberString a String representing an integer
	 * @param min the minimal value in the valid range
	 * @param min the maximal value in the valid range
	 * @return true if valid, false otherwise
	 */
	 
	public static boolean isValidInteger(String numberString, int min, int max) {
	    Integer validInteger = null;
	    try {
	        Number aNumber = numberFormat.parse(numberString);
	        int anInt = aNumber.intValue();
	        if (anInt >= min && anInt <= max) {
	            validInteger = new Integer(anInt);
	        }
	    }
	    catch (ParseException e) {
	        // Ignore and return null
	    }
	    return validInteger != null;
	}
	
	
	/**
	 *   wzwen   是否是有效的整数
	 *
	 */
	public static boolean isValidInteger(String numberString ) {
	    Integer validInteger = null;
	    try {
	        Number aNumber = numberFormat.parse(numberString);
	        int anInt = aNumber.intValue();
	        validInteger = new Integer(anInt);
	    }
	    catch (ParseException e) {
	        // Ignore and return null
	    }
	    return validInteger != null;
	}
	
	/**
	 *   wzwen   是否是有效的Double类型
	 *
	 */
	public static boolean isValidDouble( String str )
	{
		try{
			Double.parseDouble( str ) ;
			return true ;
		}
		catch( Exception e)
		{
			return false ;
		}
	}
	 
	/**
	 * Returns true if the email string contains an at sign ("@") and
	 * at least one dot ("."), i.e. "hans@gefionsoftware.com" is accepted
	 * but "hans@gefionsoftware" is not. Note! This rule is not always
	 * correct (e.g. on an intranet it may be okay with just a name) and
	 * does not gurantee a valid Internet email address but it takes
	 * care of the most obvious Internet mail address format errors.
	 *
	 * @param emailAddrString a String representing an email address
	 * @return true if valid, false otherwise
	 */
	public static boolean isValidEmailAddr(String emailAddrString) {
	    boolean isValid = false;
	    if (emailAddrString != null && 
	        emailAddrString.indexOf("@") != -1 &&
	        emailAddrString.indexOf(".") != -1) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	/**
	 * Returns true if the specified string matches a string in the set
	 * of provided valid strings, ignoring case if specified.
	 *
	 * @param value the String validate
	 * @param validStrings an array of valid Strings
	 * @param ignoreCase if true, case is ignored when comparing the value
	 *  to the set of validStrings
	 * @return true if valid, false otherwise
	 */
	public static boolean isValidString(String value, String[] validStrings, 
	        boolean ignoreCase) {
	    boolean isValid = false;
	    for (int i = 0; validStrings != null && i < validStrings.length; i++) {
	        if (ignoreCase) {
	            if (validStrings[i].equalsIgnoreCase(value)) {
	                isValid = true;
	                break;
	            }
	        }
	        else {
	            if (validStrings[i].equals(value)) {
	                isValid = true;
	                break;
	            }
	        }
	    }
	    return isValid;
	}

	//是否是合理的Email 地址。
	public static boolean isEmail( String str ) {
		
		if (str.indexOf("@")<0 || str.indexOf(".")<0 )
			return false ;
		else
			return true ; 
	}
	
	//是否是合理的主页。
	public static boolean isHomePage( String str ){
	
	if (str.indexOf("www.")<0 )
			return false ;
		else
			return true ;
	}
	
	
	//是否是合理的Email 地址。
	public static boolean IsEmail( String str ) {
		
		if (str.indexOf("@")<0 || str.indexOf(".")<0 )
			return false ;
		else
			return true ; 
	}
	
	//是否是合理的主页。
	public static boolean IsHomePage( String str ){
	
	if (str.indexOf("www.")<0 )
			return false ;
		else
			return true ;
	}
}