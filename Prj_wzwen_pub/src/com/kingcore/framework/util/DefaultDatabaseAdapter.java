/*
 * Created on 2003-6-20
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.kingcore.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
/**
 * @author WUZEWEN
 */
public class DefaultDatabaseAdapter implements DatabaseInterface {

	/* (non-Javadoc)
	 * @see com.kingcore.framework.util.DatabaseInterface#getSql(java.lang.String)
	 */
	public String getSql(String method, Hashtable parameters)
		throws Throwable {
		Method runMethod = null;
		Object[] o = null;
		Class[] c = null;
		if( parameters != null){
			o = new Object[1];
			c = new Class[1];
			o[0] = parameters;
			c[0] = parameters.getClass();
		}
		try {
			runMethod = getClass().getMethod(method, c);
		} catch (NoSuchMethodException e) {
			throw e;
		}
		try {
			return (String)runMethod.invoke(this, o);
		} catch (InvocationTargetException e) {
			log.fatal(e);
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			log.fatal(e);
			throw e;
		}
	}

}
