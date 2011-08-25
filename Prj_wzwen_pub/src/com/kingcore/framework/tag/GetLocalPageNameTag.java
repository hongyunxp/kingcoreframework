package com.kingcore.framework.tag ;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.bean.LocaleBean;

/**
 * This class implements a custom action that inserts a page
 * name, matching the currently selected locale, in the
 * response body.
 * It uses the com.ora.jsp.beans.locale.LocaleBean.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class GetLocalPageNameTag extends TagSupport {
    private String name;
    private String pageName;

    /**
     * Sets the LocaleBean name property.
     *
     * @param name the name of the LocaleBean
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the base page name property.
     *
     * @param pageName the base name for the page
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /**
     * Uses the LocaleBean, available in a scope as a variable
     * specified by the "name" property, to retrieve the page
     * name specified by the "pageName" property, matching the
     * current locale. The result is added to the response body.
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
            out.write(((LocaleBean) o).getPageName(pageName));
        }
        catch (IOException e) {} // Ignore
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        pageName = null;
        super.release();
    }
}
