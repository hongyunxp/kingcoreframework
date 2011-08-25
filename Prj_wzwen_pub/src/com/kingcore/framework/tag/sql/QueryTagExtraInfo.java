package com.kingcore.framework.tag.sql;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class provides information about the variable created by the
 * QueryTag to the JSP container at translation-time.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class QueryTagExtraInfo extends DBTagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]
        {
            new VariableInfo(data.getAttributeString("id"),
                "java.util.Vector",
                true,
                VariableInfo.AT_END)
        };
    }
}


