package com.kingcore.framework.tag.sql;

import com.kingcore.framework.bean.Value;

/**
 * This interface must be implemented by all Tag's that can
 * have ValueTag's in the body.
 */
public interface ValueTagParent {
    /**
     * Adds a to the value list
     */
    void addValue(Value value);
}
