package com.kingcore.framework.tag ;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.bean.LocaleBean;

/**
 * This class implements a custom action that inserts a text
 * resource, matching the currently selected locale, in the
 * response body.
 * It uses the com.ora.jsp.beans.locale.LocaleBean.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class GetLocalTextTag extends TagSupport {
    private String name;
    private String key;

    /**
     * Sets the LocaleBean name property.
     *
     * @param name the name of the LocaleBean
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the resource key property.
     *
     * @param key the resource key in the current ResourceBundle
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Uses the LocaleBean, available in a scope as a variable
     * specified by the "name" property, to retrieve the text
     * resource specified by the "key" property. The result
     * is added to the response body.
     */
    public int doEndTag() throws JspException {
        Object o = pageContext.findAttribute(name);
        if (o == null) {
            throw new JspException("LocaleBean named " + name + " not found");
        }
        if (!(o instanceof LocaleBean)) {
            throw new JspException("The object named " + name +
                " is not a LocaleBean");
        }
        JspWriter out = pageContext.getOut();
        try {
            out.write(((LocaleBean) o).getText(key));
        }
        catch (IOException e) {} // Ignore
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        key = null;
        super.release();
    }
}
