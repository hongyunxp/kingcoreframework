package com.kingcore.framework.tag ;

import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This class is a custom action for making a bean property value
 * available to other actions and scripting code as a variable.
 * The property must be a multi-value property. The getter
 * method may take a String argument identifying the one value
 * to return.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UsePropertyTag extends TagSupport {
    private String id;
    private String name;
    private String property;
    private String arg;
    private String className;

    /**
     * Sets the id attribute, i.e. the name of the variable to hold the
     * reference to the selected property value.
     *
     * @param id the variable name
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name attribute, i.e. the name of the variable holding the
     * reference to the bean with the property to retrieve.
     *
     * @param name the bean name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the property attribute, i.e. the name of the property to
     * retrieve.
     *
     * @param property the property name
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Sets the arg attribute, i.e. the String argument value used
     * by the property getter method to select on of multiple property
     * values.
     *
     * @param arg the String argument value
     */
    public void setArg(String arg) {
        this.arg = arg;
    }

    /**
     * Sets the class attribute, i.e. the class name for the property
     * value.
     *
     * @param className the property class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Retrieves one value of a multi-valued property in the specified
     * bean using a getter method with a String argument. The value is
     * saved with the specified variable name in the page scope.
     */
    public int doEndTag() throws JspException {
    	// get the object from the name
        Object obj = pageContext.findAttribute(name);
        if (obj == null) {
            throw new JspException("Variable " + name + " not found");
        }
        Object propObj = getProperty(obj, property, className);
        pageContext.setAttribute(id, propObj);
    	return SKIP_BODY;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        id = null;
        name = null;
        property = null;
        arg = null;
        className = null;
        super.release();
    }

    /**
     * Returns the value of the specified property from the
     * specified bean.
     *
     * @param bean the bean
     * @param property the property name
     * @param propertyClassName the property class name (type)
     */
    private Object getProperty(Object bean, String property,
        String propClassName) throws JspException {
        Object propObj = null;
        Class beanClass = bean.getClass();
        Class[] params = null;
        if (arg != null) {
            // If an arg is specified, it must be a String arg
            params = new Class[1];
            params[0] = String.class;
        }
        Method method = null;
        try {
            /*
             * Since the method may have an arg, look for it explicitly
             * instead of using the standard property lookup method
             */
            method = beanClass.getMethod("get" +
                property.substring(0, 1).toUpperCase() + property.substring(1),
                params);
        }
        catch (NoSuchMethodException e) {
            throw new JspException("A property getter for " + property +
                (arg != null ? " with a String argument" : "") +
                " not found in " + name);
        }
        Class propClass = null;
        try {
          propClass = Class.forName(propClassName);
        }
        catch (ClassNotFoundException e) {
            throw new JspException("Property class " + propClassName + " not found");
        }

        Class returnType = method.getReturnType();
        if (!propClass.isAssignableFrom(returnType)) {
            throw new JspException("Property " + property + " is not a " + className);
        }
        Object[] args = null;
        if (arg != null) {
            args = new Object[1];
            args[0] = arg;
        }
        try {
            propObj = method.invoke(bean, args);
        }
        catch (Exception e) {
            throw new JspException("Failed to get property " + property + " from " + name);
        }
        return propObj;
    }
}
