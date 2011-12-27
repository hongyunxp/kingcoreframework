/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Zeven.woo
 */

package com.kingcore.framework.base.controller;

import org.apache.log4j.Logger;

import com.kingcore.framework.cache.CacheManager;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * ≤È—Øª∫¥Êπ‹¿Ì∆˜°£
 * 
 * @author	WUZEWEN on 2006-9-13
 *
 */
public class QueryCacheManager {
	private static Logger log = Logger.getLogger(QueryCacheManager.class);
	private static CacheManager cacheManager = CacheManager.getInstance();  //CacheManager.MIX
	
	public static Object getCache(String identifier) throws NeedsRefreshException {
		//if (log.isInfoEnabled()) {
			//Object[] o = {new Long(cacheManager.getHitCount()), new Long(cacheManager.getMissCount())};
		 	//log.info( " [DAOCacheManager] getCache: {0} hits, {1} misses" );
		//}
		return cacheManager.getCache(identifier);
	}
	
	public static void putCache(Object object, String id, int minutesToLive) {
		//if (log.isInfoEnabled()) {
		//	log.info(" [DAOCacheManager] putCache");
		//}
		
		cacheManager.putCache(id, object, minutesToLive);
	}
	
	public static void invalidate(String id) {
		cacheManager.invalidate(id);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
