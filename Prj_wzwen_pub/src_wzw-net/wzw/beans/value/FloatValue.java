package wzw.beans.value ;

import wzw.beans.Value;

/**
 * This class represents a float value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class FloatValue extends Value {
    private float value;

	//从页面得到的String变量构造
    public FloatValue(String value) {
        this.value = Float.parseFloat(value) ;
    }
    
    public FloatValue(float value) {
        this.value = value;
    }

    public float getFloat() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
