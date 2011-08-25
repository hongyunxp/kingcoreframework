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


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.util.ConvertUtils;


/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 

public class MoneyFormatTag extends TagSupport
{
    /**
     * 表示金额的字符串
     */
    private String money = null;
    /**
     * 有效小数位
     * 缺省为2
     */
    private String point = "2";
    /**
     * 是否做舍入操作
     */
    private String round = "true";
    /**
     * 舍入的标准
     */
    private String std = null;
    //-------------------------------set attribute
    public void setMoney(String money)
    {
        this.money = money;
    }

    public void setPoint(String point)
    {
        this.point = point;
    }

    public void setStd(String std)
    {
        this.std = std;
    }

    public int doStartTag() throws JspException
    {
        String formatMoney = null;
        try
        {
            if (round.equals("false") && !round.equals(""))
            {
                if (std != null && !std.equals(""))
                {
                    formatMoney =
                        ConvertUtils.formatMoney(
                            money,
                            Integer.parseInt(point),
                            true,
                            Integer.parseInt(std));
                }
                else
                {
                    formatMoney =
                        ConvertUtils.formatMoney(
                            money,
                            Integer.parseInt(point),
                            true);
                }
            }
            else
            {
                formatMoney =
                    ConvertUtils.formatMoney(
                        money,
                        Integer.parseInt(point),
                        false);
            }
            JspWriter out = pageContext.getOut();
            out.write(formatMoney);
        }
        catch (IOException e)
        {
            ;
        }
        catch (Exception e)
        {
            ;
        }
        return this.SKIP_BODY;
    }
}
