/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.util;

import java.util.HashMap;

/**
 * <p>这是一个类似Hex的编码工具类，旧版中的Cookie操作用到。
 * 		建议不再使用该类。</p>
 * @deprecated 建议使用 Hex 类替换本类的功能。
 * @author Zeven on 2005-9-22
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class CodeUtils {


    private static final String hex[] = {
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", 
        "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", 
        "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", 
        "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", 
        "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", 
        "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", 
        "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", 
        "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", 
        "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", 
        "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", 
        "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", 
        "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", 
        "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", 
        "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", 
        "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", 
        "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", 
        "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", 
        "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", 
        "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", 
        "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", 
        "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", 
        "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", 
        "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", 
        "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", 
        "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", 
        "FA", "FB", "FC", "FD", "FE", "FF"
    };
    private static final byte val[] = {
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 0, 1, 
        2, 3, 4, 5, 6, 7, 8, 9, 63, 63, 
        63, 63, 63, 63, 63, 10, 11, 12, 13, 14, 
        15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 10, 11, 12, 
        13, 14, 15, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 
        63, 63, 63, 63, 63, 63
    };

    public CodeUtils()
    {
    }

    public static String escape(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            int k = s.charAt(j);
            if(65 <= k && k <= 90)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(97 <= k && k <= 122)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(48 <= k && k <= 57)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(k == 45 || k == 95 || k == 46 || k == 33 || k == 126 || k == 42 || k == 39 || k == 40 || k == 41)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(k <= 127)
            {
                stringbuffer.append('%');
                stringbuffer.append(hex[k]);
            } else
            {
                stringbuffer.append('%');
                stringbuffer.append('u');
                stringbuffer.append(hex[k >>> 8]);
                stringbuffer.append(hex[0xff & k]);
            }
        }

        return stringbuffer.toString();
    }

    public static String unescape(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        for(int j = s.length(); i < j; i++)
        {
            int k = s.charAt(i);
            if(65 <= k && k <= 90)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(97 <= k && k <= 122)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(48 <= k && k <= 57)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(k == 45 || k == 95 || k == 46 || k == 33 || k == 126 || k == 42 || k == 39 || k == 40 || k == 41)
            {
                stringbuffer.append((char)k);
                continue;
            }
            if(k == 37)
            {
                int l = 0;
                if('u' != s.charAt(i + 1))
                {
                    l = l << 4 | val[s.charAt(i + 1)];
                    l = l << 4 | val[s.charAt(i + 2)];
                    i += 2;
                } else
                {
                    l = l << 4 | val[s.charAt(i + 2)];
                    l = l << 4 | val[s.charAt(i + 3)];
                    l = l << 4 | val[s.charAt(i + 4)];
                    l = l << 4 | val[s.charAt(i + 5)];
                    i += 5;
                }
                stringbuffer.append((char)l);
            } else
            {
                stringbuffer.append((char)k);
            }
        }

        return stringbuffer.toString();
    }

    public static void main(String args[])
    {
        String s = "\u4E2D\u65871234 abcd[]()<+>,.~\\";
        System.out.println(s);
        System.out.println(escape(s));
        System.out.println(unescape(escape(s)));
    }

}
