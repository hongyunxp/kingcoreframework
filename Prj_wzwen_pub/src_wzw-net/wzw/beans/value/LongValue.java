package wzw.beans.value ;

import wzw.beans.Value;
/**
 * This class represents a long value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class LongValue extends Value {
    private long value;

	//从页面得到的String变量构造
    public LongValue(String value) {
        this.value = Long.parseLong(value);
    }
    
    public LongValue(long value) {
        this.value = value;
    }

    public long getLong() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
