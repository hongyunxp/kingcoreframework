package wzw.beans.value ;

import wzw.beans.Value;

/**
 * This class represents a int value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class IntValue extends Value {
    private int value;

	//从页面得到的String变量构造
    public IntValue(String value) {
        this.value = Integer.parseInt(value);
    }
    
    public IntValue(int value) {
        this.value = value;
    }

    public int getInt() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
