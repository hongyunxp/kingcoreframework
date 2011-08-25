package com.kingcore.framework.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a number of static methods that can be used to
 * validate the format of Strings, typically received as input from
 * a user, and to format values as Strings that can be used in
 * HTML output without causing interpretation problems.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class StringUtils {

    /**
     * An empty immutable <code>String</code> array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Replaces one string with another throughout a source string.
     *
     * @param in the source String
     * @param from the sub String to replace
     * @param to the sub String to replace with
     * @return a new String with all occurences of from replaced by to
     */
    public static String replaceInString(String in, String from, String to)
    {
        if (in == null || from == null || to == null) {
            return in;
        }

        StringBuffer newValue = new StringBuffer();
        char[] inChars = in.toCharArray();
        int inLen = inChars.length;
        char[] fromChars = from.toCharArray();
        int fromLen = fromChars.length;

        for (int i = 0; i < inLen; i++) {
            if (inChars[i] == fromChars[0] && (i + fromLen) <= inLen) {
                boolean isEqual = true;
                for (int j = 1; j < fromLen; j++) {
                    if (inChars[i + j] != fromChars[j]) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    newValue.append(to);
                    i += fromLen - 1;
                }
                else {
                    newValue.append(inChars[i]);
                }
            }
            else {
                newValue.append(inChars[i]);
            }
        }
        return newValue.toString();
    }


    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }
    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.split(null, *, *)            = null
     * StringUtils.split("", *, *)              = []
     * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cdef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars, int max) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i =0, start = 0;
        boolean match = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
            	boolean equalAll = true ;
            	for(int j=0; j<separatorChars.length(); j++){
            		if(separatorChars.charAt(j)!=str.charAt(i+j)){
            			//System.out.print("\n!=") ;
            			//System.out.print(str.charAt(i+j)) ;
            			equalAll = false ;
            			break;
            		}
            	}
                if ( equalAll ) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    // if equals
                    i += separatorChars.length() ;
                    //start = ++i;
                    start = i ;
                    continue;
                }
                match = true;
                i++;
            }
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    //
	public static String toGB2312(String s) throws Exception{
		if (s==null || s.length()<1) return s;
		byte[] temp_b = s.getBytes("ISO-8859-1");
		String sTmpStr = new String(temp_b,"GB2312");
		return sTmpStr;
	}
}
