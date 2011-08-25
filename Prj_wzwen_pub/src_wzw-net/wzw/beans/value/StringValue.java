package wzw.beans.value ;

import wzw.beans.Value;
import wzw.util.StringUtils;



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
    		System.out.print("\nStringValue exception!") ;
    	}
    }

    public String getString() {
        return value;
    }
}
