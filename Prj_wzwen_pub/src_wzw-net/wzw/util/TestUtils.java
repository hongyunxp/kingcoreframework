/**
 * Copyright (C) 2008 ChangSha WangWin Science & Technology CO,.LTD. All rights reserved.
 * WangWin PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

/**
 * <p>Wu Zewen java笔记系统专用类测试工具类，类似Junit里面的测试方式，不过更简单、快捷、灵活。</p>
 * @author Zeven on 2008-1-31
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TestUtils {

	
	/**
	 * 
	 * <p>列出类中所有的public类型的方法。根据用户的选择执行其中一个。
	 * 		放在每个类的main方法中即可。</p>
	 * @author Zeven on 2008-2-1
	 * @param clazz
	 */
	public static void testAllMethod( Class<?> clazz ){


		try {
			String[] defaultMethods = {"equals","wait","getClass","notify",
					"hashCode","toString","main","notifyAll"};

			SortedMap<String, String>  map = new TreeMap<String, String>();
			Method[] md = clazz.getMethods();
			for (int i = 0,j=1; i < md.length; i++) {
				if(ArrayUtils.contains(defaultMethods, md[i].getName())){
					continue;
				}
				map.put( ""+ j++, md[i].getName() );
			}
			map.put( "q", "quit from class test." );

			//get a br
			BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)) ;
			//read characters
			Iterator<Map.Entry<String, String>> it = null;
			Map.Entry<String, String> mapen = null;
			String val = null;
			String k;
			System.out.println("please chose ...");
			it = map.entrySet().iterator() ;
			do{
				while(it.hasNext() ){
					mapen = it.next() ;
					System.out.println(mapen.getKey() + "\t" + mapen.getValue() );
				}
				System.out.print("> ");
				k = br1.readLine();
				if( "q".equals(k)) { 
					break;
				}
				if( map.get(k)!=null){
					val = map.get(k).toString();
					System.out.println("**************** begin *"+val+"* ...");
					clazz.getMethod(val, null).invoke(null, null);
					System.out.println("**************** *"+val +"* test success.");
					continue;
				}

				System.out.println("invalid choose,please choose again!");
				System.out.println("please chose ...");
				it = map.entrySet().iterator() ;

			}while(true) ;


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void consolePrint(Object obj){
		if(obj instanceof List){
			outPutList((List<?>) obj);
		
		}else if(obj instanceof ResultSet){
			outPutResultSet((ResultSet) obj);
			
		}else if(obj instanceof Map){
			outPutMap((Map<?, ?>) obj);
			
		}
		
	}

	private static void outPutMap(Map<?, ?> map) {
		Iterator<?> it = map.entrySet().iterator() ;
		while(it.hasNext() ){
			Map.Entry<?, ?> mapen = (Map.Entry<?, ?>)it.next() ;
			System.out.println("map '"+mapen.getKey()+"','"+mapen.getValue()+"')") ; //(String)
		}
		
	}

	private static void outPutList(List<?> list) {
		Object obj = null;
		for (int i=0; i<list.size(); i++) {
			obj = list.get(i);
			if( obj instanceof Map){
				outPutMap((Map<?,?>) obj);
			}else if( obj instanceof String ){
				System.out.println("---rsw++++---"+obj);
			}
			
		}
		
	}

	private static void outPutResultSet(ResultSet rs) {
		try {
			while(rs.next()){
				System.out.println("---rs---"+rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <p>java类文件的说明...</p>
	 * @author Zeven on 2008-1-31
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
