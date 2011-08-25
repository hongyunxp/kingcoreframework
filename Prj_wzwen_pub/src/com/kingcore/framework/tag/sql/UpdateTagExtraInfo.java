package com.kingcore.framework.tag.sql;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class provides information about the variable created by the
 * UpdateTag to the JSP container at translation-time.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UpdateTagExtraInfo extends DBTagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        if (data.getAttributeString("id") == null) {
            return new VariableInfo[0];
        }
        else {
            return new VariableInfo[]
            {
                new VariableInfo(data.getAttributeString("id"),
                    "java.lang.Integer",
                    true,
                    VariableInfo.AT_END)
            };
        }
    }
}


