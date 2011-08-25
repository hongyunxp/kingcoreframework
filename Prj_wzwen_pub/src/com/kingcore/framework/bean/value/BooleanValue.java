package com.kingcore.framework.bean.value ;

import com.kingcore.framework.bean.Value;

/**
 * This class represents a boolean value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class BooleanValue extends Value {
    private boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public boolean getBoolean() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
