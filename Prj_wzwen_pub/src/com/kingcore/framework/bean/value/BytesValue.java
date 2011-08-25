package com.kingcore.framework.bean.value ;

import com.kingcore.framework.bean.Value;

/**
 * This class represents a byte[] value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class BytesValue extends Value {
    private byte[] value;

    public BytesValue(byte[] value) {
        this.value = value;
    }

    public byte[] getBytes() {
        return value;
    }

    public String getString() {
        return new String(value);
    }
}
