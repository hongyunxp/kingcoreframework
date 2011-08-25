/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.lang ;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;


/**
 * <p>对于数组的操作类，包括数组对象和简单类型的数组。
 * 			基于第三方的类和方法；
 * 		封装的目的是为增加一层，尽可能减弱第三方变动引起的对系统的影响。
 * 	参考列表：
 * 		org.apache.commons.lang.* 
 * 
 * </p>
 * @author Zeven on 2008-5-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */
public class ArraySupport {

    /**
     * Returns true if the specified value matches one of the elements
     * in the specified array.
     *
     * @param array the array to test.
     * @param value the value to look for.
     * @return true if valid, false otherwise
     */
    public static boolean isContains(String[] array, String value) {
        boolean isIncluded = false;

        if (array == null || value == null) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (value.equals(array[i])) {
                isIncluded = true;
                break;
            }
        }
        //return ArrayUtils.contains(array, value);
        return isIncluded;
    }
    
    public static int indexOf( Object[] array, Object obj ){
    	return ArrayUtils.indexOf(array, obj);
    }
    

    public static int lastIndexOf( Object[] array, Object obj ){
    	return ArrayUtils.lastIndexOf(array, obj);
    }

    public static Object[] subarray( Object[] array, int startIndexInclusive, int endIndexExclusive ){
    	return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive);
    }
    
    public static Map<?, ?> toMap(Object[] array){
    	return ArrayUtils.toMap( array );
    }
    
    public static Object[] add(Object[] array,
            Object element){
    	return ArrayUtils.add( array, element);
    }
    
    public static Object[] add(Object[] array,
            int index,
            Object element){
    	return ArrayUtils.add( array, index, element);
    	
    }
    
    public static Object[] addAll(Object[] array1,
            Object[] array2){
    	return ArrayUtils.addAll( array1, array2);
    	
    }
    
    public static Object[] clone(Object[] array){
    	return ArrayUtils.clone( array );
    	
    }
    
    public static Object[] removeElement(Object[] array,
            Object element){
    	return ArrayUtils.removeElement( array, element );
    	
    }
    
}
