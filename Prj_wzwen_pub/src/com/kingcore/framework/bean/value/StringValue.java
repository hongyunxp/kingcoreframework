package com.kingcore.framework.bean.value ;

import com.kingcore.framework.bean.Value;
import com.kingcore.framework.util.StringUtils;

/**
 * This class represents a String value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class StringValue extends Value {
    private String value;

    public StringValue(String value) {
    	/*this.value = value ;*/
    	try{
        	this.value = StringUtils.toGB2312(value);
    	}catch( Exception e){
    		System.out.println("\nStringValue exception!") ;
    	}
    }

    public String getString() {
        return value;
    }
}
