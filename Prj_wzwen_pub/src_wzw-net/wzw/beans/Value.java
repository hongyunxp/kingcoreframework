package wzw.beans ;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


/**
 * This class represents a value used by the SQL tags. It contains
 * default implementations of get methods for all supported types,
 * to avoid casting when using a concrete implementation. All default
 * implementations throw an UnsupportedConversionException.
 *
 * Each subclass must override the getString() method, returning the
 * value as a String, plus the get method for the appropriate data
 * type, and provide a constructor to set the value.
 *  
 *  Zeven  UnsupportedConversionException to Exception
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public abstract class Value {

    public BigDecimal getBigDecimal() throws Exception {
        throw new Exception("No conversion to BigDecimal");
    }

    public boolean getBoolean() throws Exception {
        throw new Exception("No conversion to boolean");
    }

    public byte getByte() throws Exception {
        throw new Exception("No conversion to byte");
    }

    public byte[] getBytes() throws Exception {
        throw new Exception("No conversion to byte[]");
    }

    public Date getDate() throws Exception {
        throw new Exception("No conversion to Date");
    }

    public double getDouble() throws Exception {
        throw new Exception("No conversion to double");
    }

    public float getFloat() throws Exception {
        throw new Exception("No conversion to float");
    }

    public int getInt() throws Exception {
        throw new Exception("No conversion to int");
    }

    public long getLong() throws Exception {
        throw new Exception("No conversion to long");
    }

    public short getShort() throws Exception {
        throw new Exception("No conversion to short");
    }

    /**
     * This method must be implemented by all subclasses.
     * All data types can be converted to a String.
     */
    public abstract String getString();

    public Time getTime() throws Exception {
        throw new Exception("No conversion to Time");
    }

    public Timestamp getTimestamp() throws Exception {
        throw new Exception("No conversion to Timestamp");
    }

    public Object getObject() throws Exception {
        throw new Exception("No conversion to Object");
    }
}