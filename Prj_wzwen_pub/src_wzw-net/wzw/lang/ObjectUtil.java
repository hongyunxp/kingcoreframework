/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.lang;

import java.util.HashMap;

/**
 * <p>java类文件的说明...</p>
 * @author Zeven on Aug 6, 2011
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ObjectUtil {


	public static Object createObjectByName(String className) {

		Object obj=null;
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			//建立被载入类类的实例并强制类型转换， 
			obj=  clazz.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
