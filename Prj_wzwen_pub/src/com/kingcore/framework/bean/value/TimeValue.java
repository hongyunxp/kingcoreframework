package com.kingcore.framework.bean.value ;

import java.sql.Time;

import com.kingcore.framework.bean.Value;

/**
 * This class represents a Time value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class TimeValue extends Value {
    private Time value;

    public TimeValue(Time value) {
        this.value = value;
    }

    public Time getTime() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}
