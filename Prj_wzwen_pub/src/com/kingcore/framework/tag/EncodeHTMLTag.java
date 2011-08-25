package com.kingcore.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kingcore.framework.util.ConvertUtils;

/**
 * This class is a tag handler for custom action that replaces
 * HTML special characters in its body with the corresponding
 * HTML character entities.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class EncodeHTMLTag extends BodyTagSupport {

    /**
     * Reads the body content, converts all HTML special characters
     * with the corresponding HTML character entities, and adds the
     * result to the response body.
     */
    public int doAfterBody() throws JspException {
    	//获取行为体内容
        BodyContent bc = getBodyContent();
        //获取out 对象   注意：不使用 getout()函数
        //，因为是向外层输出，外层可能是另一个值BodyContent对象。
        JspWriter out = getPreviousOut();
        try {
            out.write(ConvertUtils.toHTMLString(bc.getString()));
        }
        catch (IOException e) {} // Ignore
        return SKIP_BODY;
    }
}
