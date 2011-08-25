package com.kingcore.framework.bean.value ;

import com.kingcore.framework.bean.Value;

/**
 * This class represents a short value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class ShortValue extends Value {
    private short value;

	//从页面得到的String变量构造
    public ShortValue(String value) {
        this.value = Short.parseShort(value );
    }
    
    public ShortValue(short value) {
        this.value = value;
    }

    public short getShort() {
        return value;
    }

    public String getString() {
        return String.valueOf(value);
    }
}
