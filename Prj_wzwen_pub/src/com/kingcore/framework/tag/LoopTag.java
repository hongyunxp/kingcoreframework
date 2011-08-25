package com.kingcore.framework.tag;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * This class is a custom action for looping through the elements
 * of a multi-valued bean or bean property. The bean or bean property
 * must be an array, a Vector, a Dictionary or an Enumeration. The
 * action body is evaluated once for each element.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class LoopTag extends BodyTagSupport {
    // Property variables
    private String name;
    private String property;
    private String loopId;
    private String className;

    // Enumeration used for iteration
    private Enumeration enum_1;

    /**
     * Sets the name attribute, i.e. the name of the variable holding
     * a reference to the bean with the multi-value property to loop
     * through.
     *
     * @param name the bean variable name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the property attribute, i.e. the name of the multi-value
     * property to loop through.
     *
     * @param property the property name
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Sets the loopId attribute, i.e. the name of the variable to
     * hold the element reference in the body.
     *
     * @param property the property name
     */
    public void setLoopId(String loopId) {
        this.loopId = loopId;
    }

    /**
     * Sets the class attribute, i.e. the name of the class for the
     * multi-value property elements.
     *
     * @param class the element class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Creates an Enumeration of all loop values, either using the object
     * specified by the name attribute directly, or using the specified
     * property. The object or the property must be one of Enumeration,
     * Vector, Dictionary or Object[] (no primitive type arrays), or a
     * subclass/subinterface of one of them.
     *
     * Makes the first element available to the body in a variable
     * with the name specified by the loopId attribute.
     */
    public int doStartTag() throws JspException {
        Object obj = pageContext.findAttribute(name);
        if (obj == null) {
            throw new JspException("Variable " + name + " not found");
        }

        Object mvObj = obj;
        try {
            // Get the multi-value object using the specified property getter
            // method, if any
            if (property != null) {
                mvObj = getProperty(obj, property);
            }

            enum_1 = getEnumeration(mvObj);
        }
        catch (JspException e) {
            throw new JspException("Error getting loop data from " + name + ": " +
                e.getMessage());
        }

        // Set the first loop value, if any
        if (enum_1 != null && enum_1.hasMoreElements()) {
            Object currValue = enum_1.nextElement();
            pageContext.setAttribute(loopId, currValue);
        	return EVAL_BODY_TAG;
        }
        else {
        	return SKIP_BODY;
        }
    }

    /**
     * Makes the next element available to the body in a variable
     * with the name specified by the loopId attribute, or returns
     * SKIP_BODY if all elements have been processed.
     */
    public int doAfterBody() throws JspException {
        if (enum_1.hasMoreElements()) {
            Object currValue = enum_1.nextElement();
            pageContext.setAttribute(loopId, currValue);
        	return EVAL_BODY_TAG;
        }
        else {
        	return SKIP_BODY;
        }
    }

    /**
     * Writes the accumulated body contents to the JspWriter.
     */
    public int doEndTag() throws JspException {
        // Test if bodyContent is set, since it will be null if the
        // body was never evaluated (doStartTag returned SKIP_BODY)
        if (getBodyContent() != null) {
            try {
                getPreviousOut().print(getBodyContent().getString());
            }
            catch (IOException e) {}
        }
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        property = null;
        loopId = null;
        className = null;
        enum_1 = null;
        super.release();
    }

    /**
     * Returns an Object representing an Enumeration, a Vector,
     * a Dictionary or an array of objects (no primitive types),
     * using the specified property getter method on the specified
     * object.
     *
     * @param obj the Object with the multi-value property
     * @param property the property name
     * @return the multi-value Object
     */
    private Object getProperty(Object obj, String property)
        throws JspException {
        Object mvObj = null;

        Method method = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (int i = 0; pds != null && i < pds.length; i++) {
                if (pds[i].getName().equals(property)) {
                    method = pds[i].getReadMethod();
                    break;
                }
            }
        }
        catch (IntrospectionException e) {
            throw new JspException("Error analyzing the bean class: " +
                e.getMessage());
        }

        if (method == null) {
            throw new JspException("Property " + property + " not found");
        }

        // Make sure the property is not of a primitive type
        if (method.getReturnType().isPrimitive()) {
            throw new JspException("Invalid property data type");
        }

        // Invoke the method to get the multi-value Object
        Object[] args = {};
        try {
            mvObj = method.invoke(obj, args);
        }
        catch (Exception e) {
            throw new JspException("Failed to get property " + property + ": " +
                e.getMessage());
        }
        return mvObj;
    }

    /**
     * Returns an Enumeration of the values in the specified multi-value
     * object, which can be an Enumeration, Vector, Dictionary or array
     * of Objects (not primitive types).
     *
     * @param obj the multi-value Object
     * @return an Enumeration
     * @exception JspException if invalid type
     */
    private Enumeration getEnumeration(Object obj) throws JspException {
        if (obj == null) {
            return null;
        }

        Enumeration enum_1 = null;
        if (Enumeration.class.isAssignableFrom(obj.getClass())) {
            enum_1 = (Enumeration) obj;
        }
        else if (Vector.class.isAssignableFrom(obj.getClass())) {
            enum_1 = ((Vector) obj).elements();
        }
        else if (Dictionary.class.isAssignableFrom(obj.getClass())) {
            enum_1 = ((Dictionary) obj).elements();
        }
        else if (obj.getClass().isArray()) {
            Object[] oa = (Object[]) obj;
            Vector v = new Vector(oa.length);
            for (int i = 0; i < oa.length; i++) {
                v.addElement(oa[i]);
            }
            enum_1 = v.elements();
        }
        else {
            throw new JspException("Invalid data type");
        }
        return enum_1;
    }
}
