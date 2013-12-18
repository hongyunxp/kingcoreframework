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

package com.kingcore.framework.cache;

import org.apache.log4j.Logger;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;


/**
 * 缓存对象管理者。
 * 
 * @author	WUZEWEN on 2006.09.21
 *
 */
public class CacheManager {

	private static Logger log = Logger.getLogger(CacheManager.class);
	private static CacheManager instance = null;
	private static volatile GeneralCacheAdministrator cacheAdministrator = null;
	
	static {
		instance = new CacheManager();
		cacheAdministrator = new GeneralCacheAdministrator(); // 采用第三方 opensymphony.oscache
	}
	
	
	private CacheManager() {
		super();
	}
	
	public static CacheManager getInstance(){
		return instance;
	}
	
	public synchronized Object getCache(String key) throws NeedsRefreshException {
		return cacheAdministrator.getFromCache(key);
	}

	public synchronized Object getCache(String key, int refreshPeriod) throws NeedsRefreshException {
		return cacheAdministrator.getFromCache(key, refreshPeriod);
	}
	
	public synchronized void putCache(String key, Object content) {
		cacheAdministrator.putInCache(key, content);
	}
	public synchronized void putCache(String key, Object content, int minutesToLive) {
		cacheAdministrator.putInCache(key, content);
	}
	
	/**
	 * Cancels a pending cache update. This should only be called by a thread that received a NeedsRefreshException and was unable to generate some new cache content. 
	 * @param key
	 */
	public void cancelUpdate(String key) {
		cacheAdministrator.cancelUpdate(key);
	}
	
	
	public void clearCache() {
		cacheAdministrator.flushAll();
	}

	public void clearCache(String key) {
		cacheAdministrator.flushEntry(key);
	}
	
	public static String createKey(String[] keys) {
		StringBuffer newKey = new StringBuffer("");
		for(int i =0;i<keys.length;i++)
			newKey.append(keys[i]).append("/");
		return newKey.toString();
	}
	
	public synchronized void invalidate(String key) {

	}
	
	public long getHitCount() {
		return 1;
	}
	
	public long getMissCount() {
		return 1;
	}
	
	public long getCurrentCacheSize() {
		return 1;
	}
	
	public synchronized void sweep() {
		
	}

}




