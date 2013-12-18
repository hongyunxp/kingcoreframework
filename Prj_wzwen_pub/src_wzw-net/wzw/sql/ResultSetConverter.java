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

package wzw.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.ArrayListHandler;


/**
 * <p>对于经常使用到的结果集对象的转换类。 主要转为为Map,MapList,Bean,BeanList
 * 	参考列表：
 * 		org.apache.commons.dbutils.* 
 * 
 *   ----------------------------------------------------------
 *   org.apache.commons.dbutils.handlers Classes  
 *   	ArrayHandler 
 *   	ArrayListHandler 	-- Array and ArrayList
 *   	BeanHandler 
 *   	BeanListHandler 
 *   	ColumnListHandler 
 *   	KeyedHandler 
 *   	MapHandler 
 *   	MapListHandler 
 *   	ScalarHandler 
 * 
 * </p>
 * @author Zeven on 2008-5-10
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ResultSetConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Convert a ResultSet Object to Map.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> toMap(ResultSet rs) throws SQLException{

		if(rs==null){
			return null;
		}
		
		Map<String, Object> map=new Hashtable<String, Object>();   
		ResultSetMetaData   rsmd   =   rs.getMetaData(); 
		
		if   (rsmd.getColumnCount()>=0){     
			if(rs.next()){
				for(int   j=1;j<=rsmd.getColumnCount();j++){ 
					if( rs.getObject(j)!=null ) { // 如果为Null，无须放入
						// 说明，使用 columnName = (String) rsmd.getColumnName(i); 获取的是大写，
						//  而且有时候列获取不到,好像是MySql数据库，
						map.put( rsmd.getColumnLabel(j).toLowerCase() , rs.getObject(j) ) ;
					}
				}
			}   
		}  
			
        return map;	
		
	}
	
	/**
	 * Convert a ResultSet Object to List<Map>.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> toMapList(ResultSet rs) throws SQLException{
		
		if(rs==null){
			return null;
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData   rsmd   =   rs.getMetaData();
		
		if   (rsmd.getColumnCount()>=0){     
			while(rs.next()){
				Map<String, Object> temph=new   Hashtable<String, Object>();   
				for(int   j=1;j<=rsmd.getColumnCount();j++){ 
					if( rs.getObject(j)!=null ) { // 如果为Null，无须放入
						temph.put( rsmd.getColumnLabel(j).toLowerCase() , rs.getObject(j) ) ;
					}
				}   
				list.add( temph );   
			}   
		}  
			
        
        return list;		
	}


	/**
	 * 将结果转换为一个基本对象返回。
	 * @param rs
	 * @param type 返回List对象中的java.sql.Types 中的基本对象类型
	 * @return
	 * @throws SQLException
	 */
	public static Object toType(ResultSet rs, int type) throws SQLException{

		
		if( rs.next() ){		// run rs.next()
			switch (type) {
			case Types.INTEGER:
				return new Integer(rs.getInt(1));

			case Types.BIGINT:
				return new Long(rs.getLong(1));

			case Types.VARCHAR:
				return rs.getString(1);
				
			case Types.FLOAT:
				return new Float( rs.getFloat(1));
				
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
				return new Double( rs.getDouble(1) );

			case Types.TIMESTAMP:
				return rs.getTimestamp(1);
				
			case Types.DATE:
				return rs.getDate(1);

			case Types.TIME:
				return rs.getTime(1);
				
			default:
				return null;
			}
		}
		return null;
		
	}
	
	/**
	 * 将结果转换为一个基本对象的列表返回。
	 * @param rs
	 * @param type 返回List对象中的java.sql.Types 中的基本对象类型
	 * @return
	 * @throws SQLException
	 */
	public static List<Object> toTypeList(ResultSet rs, int type) throws SQLException{

		List<Object> list = new ArrayList<Object>();
		
		switch (type) {
		case Types.INTEGER:
			while(rs.next()){
				list.add(new Integer(rs.getInt(1)));
			}

		case Types.BIGINT:
			while(rs.next()){
				list.add(new Long(rs.getLong(1)));
			}

		case Types.VARCHAR:
			while(rs.next()){
				list.add( rs.getString(1) );
			}
			
		case Types.FLOAT:
			while(rs.next()){
				list.add( new Float( rs.getFloat(1)) );
			}
			
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.NUMERIC:
			while(rs.next()){
				list.add(new Double( rs.getDouble(1) ));
			}

		case Types.TIMESTAMP:
			while(rs.next()){
				list.add( rs.getTimestamp(1) );
			}
			
		case Types.DATE:
			while(rs.next()){
				list.add( rs.getDate(1) );
			}

		case Types.TIME:
			while(rs.next()){
				list.add( rs.getTime(1) );
			}

		//default:
		}
		return list;
	}

	
	/**
	 * Convert a ResultSet Object to Bean.
	 * @param rs
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static Object toBean(ResultSet rs, Class<?> type) throws SQLException{
		BeanProcessor bp = new BeanProcessor();
		return bp.toBean( rs, type );
		
	}
	
	/**
	 * <p>
	 * Convert a ResultSet Object to List. 
	 * 	不会改变ResultSet的指针位置，建议调用之前使用 rs.beforeFirst()方法。
	 * </p>.
	 * @param rs
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> toBeanList(ResultSet rs, Class<?> type) throws SQLException{
		BeanProcessor bp = new BeanProcessor();
		return bp.toBeanList( rs, type );
		
	}
	
	/**
	 * Z：convert a ResultSet to Array.
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static java.lang.Object[] toArray(java.sql.ResultSet rs)
    				throws java.sql.SQLException{
		BasicRowProcessor brp = new BasicRowProcessor();
		return brp.toArray( rs );
	
	}

	/**
	 * Z：convert a ResultSet to ArrayList.
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	public static List<?> toArrayList(java.sql.ResultSet rs)
					throws java.sql.SQLException{
		ArrayListHandler alh = new ArrayListHandler();
		Object obj = alh.handle( rs );
		return obj==null?null:(List<?>)obj; 

	}
	
}
