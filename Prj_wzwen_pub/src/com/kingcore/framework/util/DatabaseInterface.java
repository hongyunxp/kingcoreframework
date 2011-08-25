/*
 * Created on 2003-6-19
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.kingcore.framework.util;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author WUZEWEN
 */
public interface DatabaseInterface {
	/**
	 * 日志变量
	 */
	public static final Log log = LogFactory.getLog(DatabaseInterface.class);
	/**
	 * 得到相应的sql语句
	 * Create on 2003-6-20
	 * @param method
	 * @param parameteras
	 * @return
	 * @throws Throwable
	 */
	public String getSql(String method, Hashtable parameteras) throws Throwable;
}
