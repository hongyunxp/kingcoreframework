package wzw.beans.value ;

import java.sql.Timestamp;

import wzw.beans.Value;

/**
 * This class represents a Timestamp value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class TimestampValue extends Value {
    private Timestamp value;

    public TimestampValue(Timestamp value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}
