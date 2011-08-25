package com.kingcore.framework.bean ;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * This class contains a number of methods for dealing with
 * localized content. It's used by the com.ora.jsp.tags.generic
 * localization tag handler classes, but can also be used
 * stand-alone.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class LocaleBean implements Serializable {
    private Locale locale;
    private Locale[] requestLocales;
    private ResourceBundle bundle;
    private String supportedLangs;
    private String bundleName;
    private String language;
    private NumberFormat numberFormat;
    private DateFormat dateFormat;
    private String charset;
    private Hashtable parameters;
    
    /**
     * Returns a Locale. The Locale is constructed based on the
     * language property, if set. If not, the Locale determined
     * based on the Accept-Language header (the requestLocales
     * property) is returned, if set. If the Locale found this way
     * matches one of the supported languages, it's returned.
     * Otherwise the Locale for first language in the list of supported
     * languages is returned.
     */
    public Locale getLocale() {
        if (locale == null) {
            if (language != null && isSupportedLang(language)) {
                locale = toLocale(language);
            }
            else if (requestLocales != null) {
                locale = getSupportedLocale(requestLocales);
            }
            if (locale == null) {
                locale = getDefaultLocale();
            }
        }
        return locale;
    }

    /**
     * Sets the set of locales received with the request, and
     * resets the currently selected locale if the new set
     * is different from the current set. This is done so that
     * all possible locale sources will be evaluated again the
     * next time the locale property is retrieved.
     */
    public void setRequestLocales(Locale[] locales) {
        if (requestLocales != null && !isEqual(requestLocales, locales)) {
            resetLocale();
        }
        requestLocales = locales;
    }
    
    /**
     * Sets the set of supported languages, provided as a comma
     * separated list of country/language codes. This is a
     * mandatory property.
     * <p>
     * Resets the currently selected locale if the new set
     * is different from the current set. This is done so that
     * all possible locale sources will be evaluated again the
     * next time the locale property is retrieved.
     */
    public void setSupportedLangs(String supportedLangs) {
        if (this.supportedLangs != null && 
            !this.supportedLangs.equals(supportedLangs)) {
            resetLocale();
        }
        this.supportedLangs = supportedLangs;
    }
    
    /**
     * Returns the language code for the currently selected
     * locale.
     */
    public String getLanguage() {
        if (language == null) {
            language = toLanguage(getLocale());
        }
        return language;
    }
    
    /**
     * Sets the user selected language/country code.
     * <p>
     * Resets the currently selected locale if the new language
     * is different from the current. This is done so that
     * all possible locale sources will be evaluated again the
     * next time the locale property is retrieved.
     */
    public void setLanguage(String language) {
        if (this.language != null && !language.equals(this.language)) {
            resetLocale();
        }
        this.language = language;
    }
    
    /**
     * Sets the bundle name.
     * <p>
     * Resets the current ResourceBundle so that a new will
     * be created with the new bundle name the
     * next time the ResourceBundle is retrieved.
     */
    public void setBundleName(String bundleName) {
        if (this.bundleName != null && !bundleName.equals(this.bundleName)) {
            resetBundle();
        }
        this.bundleName = bundleName;
    }
    
    /**
     * Sets the charset used to parse request parameters.
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    /**
     * Returns the specified number String converted to a
     * double, parsed as defined by the currently selected locale.
     */
    public double getDouble(String number) throws ParseException {
        return getNumber(number).doubleValue();
    }
        
    /**
     * Returns the specified number String converted to a
     * float, parsed as defined by the currently selected locale.
     */
    public float getFloat(String number) throws ParseException {
        return getNumber(number).floatValue();
    }
        
    /**
     * Returns the specified number String converted to a
     * int, parsed as defined by the currently selected locale.
     */
    public int getInt(String number) throws ParseException {
        return getNumber(number).intValue();
    }
        
    /**
     * Returns the specified number String converted to a
     * long, parsed as defined by the currently selected locale.
     */
    public long getLong(String number) throws ParseException {
        return getNumber(number).longValue();
    }
        
    /**
     * Returns the specified date String converted to a
     * Date, parsed as defined by the currently selected locale.
     */
    public Date getDate(String date) throws ParseException {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateInstance(dateFormat.FULL, getLocale());
        }
        return dateFormat.parse(date);
    }
    
    /**
     * Returns the specified number converted to a
     * String, formatted as defined by the currently selected locale.
     */
    public String getNumberString(double number) {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getNumberInstance(getLocale());
        }
        return numberFormat.format(number);
    }
        
    /**
     * Returns the specified Date converted to a
     * String, formatted as defined by the currently selected locale.
     */
    public String getDateString(Date date) {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateInstance(dateFormat.FULL, getLocale());
        }
        return dateFormat.format(date);
    }
    
    /**
     * Returns the text resource for the specified key,
     * with the best match for the currently selected locale.
     */
    public String getText(String resourceName) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(bundleName, getLocale());
        }
        return bundle.getString(resourceName);
    }
    
    /**
     * Returns a version of the specified page name with 
     * a language/country suffix for the currently selected locale.
     */
    public String getPageName(String basePageName) {
        StringBuffer pageName = new StringBuffer();
        Locale locale = getLocale();
        if (locale.equals(getDefaultLocale())) {
            pageName.append(basePageName);
        }
        else {
            String suffix = getLocale().toString();
            int extPos = basePageName.indexOf(".");
            if (extPos != -1) {
                pageName.append(basePageName.substring(0, extPos)).
                    append("_").append(suffix).
                    append(basePageName.substring(extPos));
            }
            else {
                pageName.append(basePageName).
                    append("_").append(suffix);
            }
        }
        return pageName.toString();
    }
    
    /**
     * Sets the parameter list.
     */
    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }
    
    /**
     * Returns an array of all values for the specified parameter,
     * parsed using the currently specified charset.
     */
    public String[] getParameterValues(String parameter) 
        throws UnsupportedEncodingException {
        String[] values = null;
        if (parameters != null) {
            String[] encodedValues = (String[]) parameters.get(parameter);
            if (encodedValues != null) {
                values = new String[encodedValues.length];
                for (int i = 0; i < encodedValues.length; i++) {
                    values[i] = getDecodedValue(encodedValues[i]);
                }
            }
        }
        return values;
    }
    
    /**
     * Returns an Enumeration of all parameter names.
     */
    public Enumeration getParameterNames() {
        if (parameters == null) {
            parameters = new Hashtable();
        }
        return parameters.keys();
    }
    
    /**
     * Returns the first all value for the specified parameter,
     * parsed using the currently specified charset.
     */
    public String getParameter(String parameter) 
        throws UnsupportedEncodingException {
        String firstValue = null;
        String[] values = getParameterValues(parameter);
        if (values != null && values.length > 0) {
            firstValue = values[0];
        }
        return firstValue;
    }
    
    /**
     * Resets the ResourceBundle.
     */
    private void resetBundle() {
        bundle = null;
    }
    
    /**
     * Resets all locale dependent variables.
     */
    private void resetLocale() {
        locale = null;
        numberFormat = null;
        dateFormat = null;
        bundle = null;
    }
    
    /**
     * Returns true if the the specifed Locale arrays contains
     * exactly the same locale information.
     */
    private boolean isEqual(Locale[] arr1, Locale[] arr2) {
        boolean isEqual = true;
        if (arr1.length != arr2.length) {
            isEqual = false;
        }
        else {
            for (int i = 1; i < arr1.length; i++) {
                if (!arr1[i].equals(arr2[i])) {
                    isEqual = false;
                    break;
                }
            }
        }
        return isEqual;
    }

    /**
     * Returns true if the specified languge is a supported language.
     */
    private boolean isSupportedLang(String language) {
        return supportedLangs.indexOf(language) != -1;
    }
    
    /**
     * Returns a Locale for the default language.
     */
    private Locale getDefaultLocale() {
        StringTokenizer st = new StringTokenizer(supportedLangs, ",");
        String defaultLang = st.nextToken().trim();
        return toLocale(defaultLang);
    }
    
    /**
     * Returns a Locale for the specifed language/country code.
     */
    private Locale toLocale(String language) {
        Locale locale = null;
        int countrySeparator = language.indexOf("-");
        if (countrySeparator != -1) {
            locale = new Locale(language.substring(0, countrySeparator),
                language.substring(countrySeparator + 1));
        }
        else {
            locale = new Locale(language, "");
        }
        return locale;
    }
    
    /**
     * Returns the language/country code for the specified Locale.
     */
    private String toLanguage(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (country != null && country.length() > 0) {
            language = language + "-" + country;
        }
        return language;
    }

    /**
     * Returns the first Locale that matches a supported
     * language, or the default Locale if no match.
     */
    private Locale getSupportedLocale(Locale[] locales) {
        Locale locale = getDefaultLocale();
        for (int i = 0; i < locales.length; i++) {
            if (isSupportedLang(toLanguage(locales[i]))) {
                locale = locales[i];
                break;
            }
        }
        return locale;
    }
    
    /**
     * Returns the specified String as a Number, parsed based on
     * the currently selected locale.
     */
    private Number getNumber(String number) throws ParseException {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getNumberInstance(getLocale());
        }
        return numberFormat.parse(number);
    }
    
    /**
     * This method returns a new String created from the specified String, 
     * encoded as 8859_1 converted to the currently set charset.
     */
    private String getDecodedValue(String value) 
        throws UnsupportedEncodingException {
        if (charset == null) {
            return value;
        }
        return new String(value.getBytes("8859_1"), charset);
    }
}