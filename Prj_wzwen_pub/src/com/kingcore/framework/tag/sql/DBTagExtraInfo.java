package com.kingcore.framework.tag.sql;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * This class provides additional attribute validation code for
 * DBTag subclasses.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class DBTagExtraInfo extends TagExtraInfo {
    /**
     * Returns true only if a valid scope value is specified:
     * page, request, session or application.
     */
    public boolean isValid(TagData data) {
        boolean isValid = false;
        String scope = data.getAttributeString("scope");

        if (scope == null || scope.equals("page") || scope.equals("request") ||
            scope.equals("session") || scope.equals("application")) {
            isValid = true;
        }
        return isValid;
    }
}
