package com.kingcore.framework.tag ;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class provides information about the variable created by the
 * UsePropertyTag to the JSP container at translation-time.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UsePropertyTagExtraInfo extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]
        {
            new VariableInfo(data.getAttributeString("id"),
                data.getAttributeString("className"),
                true,
                VariableInfo.AT_END)
        };
    }
}


