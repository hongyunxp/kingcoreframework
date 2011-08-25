package wzw.beans.value ;

import java.sql.Date;

import wzw.beans.Value;

/**
 * This class represents a java.sql.Date value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class DateValue extends Value {
    private Date value;

    public DateValue(Date value) {
        this.value = value;
    }

    public Date getDate() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}
