package wzw.beans.value ;

import wzw.beans.Value;

/**
 * This class represents a byte value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class ByteValue extends Value {
    private byte value;

    public ByteValue(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
