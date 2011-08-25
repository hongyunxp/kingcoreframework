package wzw.beans.value ;

import wzw.beans.Value;

/**
 * This class represents an Object value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class ObjectValue extends Value {
    private Object value;

    public ObjectValue(Object value) {
        this.value = value;
    }

    public Object getObject() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}
