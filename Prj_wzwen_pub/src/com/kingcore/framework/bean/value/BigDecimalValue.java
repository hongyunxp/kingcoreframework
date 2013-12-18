package com.kingcore.framework.bean.value ;

import java.math.BigDecimal;

import com.kingcore.framework.bean.Value;

/**
 * This class represents a BigDecimal value used by the SQL tags.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class BigDecimalValue extends Value {
    private BigDecimal value;

	//将页面得到的 String 变量直接转为BigDecimal对象。
    //public BigDecimalValue(String value) {
    //    this.value = new BigDecimal(value);
    //}

    public BigDecimalValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getBigDecimal() {
        return value;
    }

    public String getString() {
        return value.toString();
    }
}