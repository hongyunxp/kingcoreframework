/*
 * @(#)DataBean.java        1.00 2004/04/18
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.wu.
 */


package com.kingcore.framework.tag ;

import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @version		1.00 2004.04.18
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 * This class is a custom action intended to be used in the body of
 * an EncodeURLTag or a RedirectTag. It adds the specified parameter
 * name and value to it's parent's list of parameters. It can be
 * used in the body of any tag handler that implements the ParamParent
 * interface.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class ParamTag extends TagSupport {
    private String name;
    private String value;

    /**
     * Sets the name attribute.
     *
     * @param name the parameter name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value attribute from a String.
     *
     * @param value the parameter String value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Adds the parameter name and the URL encoded value to the
     * parent's parameter list.
     */
    public int doEndTag() throws JspException {
        Tag parent = findAncestorWithClass(this, ParamParent.class);
        if (parent == null) {
            throw new JspException("The param action is not " +
                "enclosed by a supported action type");
        }
        ParamParent paramParent = (ParamParent) parent;
        paramParent.setParam(name, URLEncoder.encode(value));
        return EVAL_PAGE;
    }

    /**
     * Releases all instance variables.
     */
    public void release() {
        name = null;
        value = null;
        super.release();
    }
}
