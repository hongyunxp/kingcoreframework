package com.kingcore.framework.tag ;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.bean.LocaleBean;

/**
 * This class implements a custom action that creates a
 * com.ora.jsp.beans.locale.LocaleBean and saves it in
 * the session scope.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UseLocaleBundleTag extends TagSupport {
    private String id;
    private String bundleName;
    private String supportedLangs;

    /**
     * Sets the id property, i.e. the name to use for the
     * LocaleBean in the session scope.
     *
     * @param id the id for the LocaleBean
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the bundleName property, i.e. the base name for the
     * ResourceBundle used for localized text.
     *
     * @param bundleName the ResourceBundle base name
     */
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    /**
     * Sets the supportedLangs property, i.e. a comma separated
     * list of language/country codes.
     *
     * @param supportedLangs the list of language/country codes
     */
    public void setSupportedLangs(String supportedLangs) {
        this.supportedLangs = supportedLangs;
    }

    /**
     * Creates and initializes a LocaleBean and saves it in the
     * session scope using the name specified by the "id" property.
     */
    public int doEndTag() throws JspException {
        LocaleBean bean = (LocaleBean)
            pageContext.getAttribute(id, PageContext.SESSION_SCOPE);
        if (bean == null) {
            bean = new LocaleBean();
            pageContext.setAttribute(id, bean, PageContext.SESSION_SCOPE);
        }
        // Tomcat bug: Only looks for bean in page scope
        pageContext.setAttribute(id, bean);
        HttpServletRequest request =
            (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response =
            (HttpServletResponse) pageContext.getResponse();
        bean.setRequestLocales(toArray(request.getLocales()));
        bean.setSupportedLangs(supportedLangs);
        String language = request.getParameter("language");
        if (language != null) {
            bean.setLanguage(language);
        }
        String charset = request.getParameter("charset");
        if (charset != null) {
            bean.setCharset(charset);
            bean.setParameters(getParameters(request));
        }
        bean.setBundleName(bundleName);

        response.setHeader("Content-Language", bean.getLanguage());

        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        id = null;
        bundleName = null;
        supportedLangs = null;
        super.release();
    }

    /**
     * Converts an Enumeration of Locale objects to a
     * Locale array.
     *
     * @param locales Enumeration of Locale objects
     * @return a Locale[]
     */
    private Locale[] toArray(Enumeration locales) {
        Vector v = new Vector();
        while (locales.hasMoreElements()) {
            v.addElement(locales.nextElement());
        }
        Locale[] localeArr = new Locale[v.size()];
        v.copyInto(localeArr);
        return localeArr;
    }

    /**
     * Returns a Hashtable with all HTTP parameters from an
     * HttpServetRequest. The key in the Hashtable is the
     * parameter name and the value is a String[].
     *
     * @param request the HttpServletRequest
     * @return a Hashtable with all parameters
     */
    private Hashtable getParameters(HttpServletRequest request) {
        Hashtable parameters = new Hashtable();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            parameters.put(name, request.getParameterValues(name));
        }
        return parameters;
    }
}
