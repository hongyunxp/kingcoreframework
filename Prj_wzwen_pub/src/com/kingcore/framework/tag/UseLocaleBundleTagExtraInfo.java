package com.kingcore.framework.tag ;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class provides information about the variable created by the
 * UseLocaleBundleTag to the JSP container at translation-time.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UseLocaleBundleTagExtraInfo extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]
        {
            new VariableInfo(data.getAttributeString("id"),
                "com.ora.jsp.beans.locale.LocaleBean",
                true,
                VariableInfo.AT_END)
        };
    }
}
