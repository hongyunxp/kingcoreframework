/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework.util;

import java.util.StringTokenizer;

/**
 * @author zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ConvertPassword
{
    /**
     * 密码加密
     * @param passwd:String 需要加密的字符串
     * @return String 返回加密以后字符串，如果返回为""，那么表示输入的密码包含了不合法字符
     */
    public static String expressPassword(String psPass) throws Exception
    {
        int i, viTemp, viAscSum, viSumMod, viDev, viMod, viMaxMod;
        char vcChar, vcBegin, vcEnd;
        String vsModStr, vsDevStr, vsTemp;

        /*检查口令是否合法，如不合法则返回*/
        // 口令长度要求大于3并且小于30
        psPass = psPass.trim();
        if (psPass.length() < 3)
        {
            return "";
        }
        if (psPass.length() > 30)
        {
            return "";
        }

        // 口令要求第一个为字母
        viTemp = psPass.charAt(0);
        if (viTemp < 'A' || viTemp > 'z' || (viTemp > 'Z' && viTemp < 'a'))
        {
            return "";
        }
        viAscSum = 0;
        for (i = 0; i < psPass.length(); i++)
        {
            vcChar = psPass.charAt(i);
            // 口令必须是字母和数字的组合
            if (vcChar > 'z'
                || (vcChar > 'Z' && vcChar < 'a')
                || vcChar < '0'
                || (vcChar > '9' && vcChar < 'A'))
            {
                return "";
            }
            viTemp = (int) vcChar;
            viAscSum = viAscSum + viTemp;
        }

        viSumMod = viAscSum % 26;
        viMaxMod = viSumMod;
        vsModStr = "";
        vsDevStr = "";
        for (i = 0; i < psPass.length(); i++)
        {
            vcChar = psPass.charAt(i);
            viTemp = (int) vcChar + viSumMod;
            viDev = viTemp / 26;
            viMod = viTemp - viDev * 26;
            if (viDev > viMaxMod)
                viMaxMod = viDev;
            vsModStr = vsModStr + "," + (char) (viMod + 97);
            vsDevStr = vsDevStr + "," + String.valueOf(viDev);
        }
        vsModStr = vsModStr.substring(1);
        vsDevStr = vsDevStr.substring(1);

        /*随机求出ASCII码差值能表示viMaxMod的任意两字母*/
        viTemp = (int) (Math.random() * (26 - viMaxMod));
        vcBegin = (char) (viTemp + 96);
        vcEnd = (char) (viTemp + viSumMod + 96);

        vsTemp = "";
        StringTokenizer st = new StringTokenizer(vsDevStr, ",");
        while (st.hasMoreElements())
        {
            vcChar = st.nextToken().charAt(0);
            viTemp = vcBegin + Integer.parseInt(String.valueOf(vcChar));
            vsTemp = vsTemp + "," + String.valueOf((char) viTemp);
        }
        vsDevStr = vsTemp.substring(1);

        /*形成加密口令*/
        psPass = "";
        i = 0;
        st = new StringTokenizer(vsDevStr, ",");
        StringTokenizer st2 = new StringTokenizer(vsModStr, ",");
        while (st.hasMoreTokens() && st2.hasMoreElements())
        {
            i++;
            if (i % 2 == 1)
            {
                psPass = st.nextToken() + st2.nextToken() + psPass;
            }
            else
            {
                psPass = st2.nextToken() + st.nextToken() + psPass;
            }
        }
        psPass = vcBegin + psPass + vcEnd;
        return psPass;
    }
    /**
     * 密码解密
     * @param passwd:String 需要解密的字符串
     * @return String 返回解密以后字符串 
     */
    public static String expandPassword(String passwd) throws Exception
    {
        int i, viTemp, viCz, viNum;
        char vcBegin, vcEnd, vcFirst, vcSecond;
        String vsTemp = "", vsPass = "";
        try
        {
            passwd = passwd.trim();

            /*求出变码差值*/
            vcBegin = passwd.charAt(0);
            vcEnd = passwd.charAt(passwd.length() - 1);
            viCz = vcEnd - vcBegin;
            passwd = passwd.substring(1);
            passwd = passwd.substring(0, passwd.length() - 1);
            viNum = 0;
            for (i = passwd.length() - 1; i >= 0; i -= 2)
            {
                viNum++;
                if ((viNum % 2) == 1)
                {
                    vcFirst = passwd.charAt(i - 1);
                    vcSecond = passwd.charAt(i);
                }
                else
                {
                    vcFirst = passwd.charAt(i);
                    vcSecond = passwd.charAt(i - 1);
                }
                viTemp =
                    (char) (vcFirst - vcBegin) * 26
                        + (char) (vcSecond - 97)
                        - (char) viCz;
                vsPass = vsPass + String.valueOf((char) viTemp);
            }
            passwd = vsPass;
        }
        catch (Exception e)
        {
        }
        return passwd;
    }
}
