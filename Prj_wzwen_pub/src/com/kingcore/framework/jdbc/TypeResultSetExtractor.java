package com.kingcore.framework.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import wzw.sql.ResultSetConverter;

/**
 * <p>将ResuletSet转为相应的java基本类型返回，针对一行一列，内部会运行rs.next()方法。</p>
 * @author Zeven on 2008-11-6
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class TypeResultSetExtractor implements ResultSetExtractor {

	private int type ;
	private boolean isMultiRow ;

	public TypeResultSetExtractor(int type, boolean isMultiRow){
		this.type = type;
		this.isMultiRow = isMultiRow;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
	 */
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {

		if(this.isMultiRow){		// multiRow
			return ResultSetConverter.toTypeList(rs, this.type);
			
		}else{			// single row
			return ResultSetConverter.toType(rs, this.type);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
