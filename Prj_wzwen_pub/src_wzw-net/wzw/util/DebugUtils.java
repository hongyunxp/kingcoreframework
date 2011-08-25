/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p>控制台输出类，用于调试各种输出信息。</p>
 * @author Zeven on 2008-1-29
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class DebugUtils {

	/**
	 * 
	 */
	protected static Logger log = Logger.getLogger( DebugUtils.class );
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		showParents(java.util.Hashtable.class);
	}
	
	public static void consolePrint(Object obj){
		if(obj instanceof List<?>){
			outPutList((List<?>) obj);
		
		}else if(obj instanceof ResultSet){
			outPutResultSet((ResultSet) obj);
			
		}else if(obj instanceof Map<?, ?>){
			outPutMap((Map<?, ?>) obj);
			
		}
		
	}

	private static void outPutMap(Map<?, ?> map) {
		Iterator<?> it = map.entrySet().iterator() ;
		while(it.hasNext() ){
			Map.Entry mapen = (Map.Entry)it.next() ;
			log.debug("map '"+(String)mapen.getKey()+"','"+(String)mapen.getValue()+"')") ;
		}
		
	}

	private static void outPutList(List<?> list) {
		Object obj = null;
		for (int i=0; i<list.size(); i++) {
			obj = list.get(i);
			if( obj instanceof Map<?, ?>){
				outPutMap((Map<?, ?>) obj);
			}else if( obj instanceof String ){
				log.debug("---rsw++++---"+obj);
			}
			
		}
		
	}

	private static void outPutResultSet(ResultSet rs) {
		try {
			while(rs.next()){
				log.debug("---rs---"+rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showParents(Object obj){
		showParents( obj.getClass() );
		
	}
	private static String getSuperclasses(Class<?> clazz){
		StringBuffer sb = new StringBuffer();
		getSuperclass(clazz, sb);
		return sb.toString();
	}
	
	private static void getSuperclass(Class<?> clazz, StringBuffer sb){
		sb.append( clazz.getName()).append("\n\t");
		
		Class<?> cl2 = clazz.getSuperclass();
		if( cl2==null){
			return ;
		}
		if(cl2.getSuperclass()!=null){
			getSuperclass( cl2, sb);
		}
	}
	public static void showParents(Class<?> clazz){
		String pstr = getSuperclasses(clazz);
		System.out.println("--parent is:"+pstr);

		System.out.println("--interfaces is:\n");
		Class[] ifs = clazz.getInterfaces();
		for (int i = 0; i < ifs.length; i++) {
			System.out.println( getSuperclasses(ifs[i]));
			
		}
		
	}
}
