package com.transn.util;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 
 * 类名:EHCacheUtil 缓存用户以及对应的EMail_UID,避免重复不断查询数据库
 * 功能描述: 
 * 作者   Administrator
 * 版本   ver 1.0 
 * 日期	 2013	2013-5-24		下午1:28:49
 */
public class EHCacheUtil {
	private Logger logger = Logger.getLogger(EHCacheUtil.class);
	private CacheManager cacheManager = null;
	private Cache paramCache = null;

	public EHCacheUtil(String cacheName) {
		init(cacheName);
	}

	private void init(String cacheName) {

		if (cacheManager == null) {
			logger.info("初始化CacheManager");
			cacheManager = CacheManager.create(this.getClass().getClassLoader().getResource("ehcache.xml"));
		}
		synchronized (this) {
			if (paramCache == null) {
				paramCache = cacheManager.getCache(cacheName);
			}
		}
	}

	public void putParam(String key, Serializable obj) {
		paramCache.put(new Element(key, obj));
	}

	public void putParam(String key, Serializable obj, int liveTime) {
		paramCache.put(new Element(key, obj, false, liveTime, liveTime));
	}

	public Object getParam(String key) {
		Element ele = paramCache.get(key);
		return ele == null ? null : ele.getObjectValue();
	}

	public boolean isExist(String key) {
		return paramCache.isKeyInCache(key);
	}

	public boolean removeParam(String key) {
		return paramCache.remove(key);
	}
	
	public List<String> getKeys() {
		return paramCache.getKeys();
	}
	
	public void flush() {
		 paramCache.flush();
	}
	
	public int getSize() {
		return paramCache.getSize();
	}
	
	public void shutdown() {
		try {
			if (paramCache != null) {
				paramCache.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cacheManager != null) {
			cacheManager.shutdown();
		}
	}

	public void destroy() {
		shutdown();
	}

//	 public static void main(String[] args) {
//		 String key = "2013";
//		 EHCacheUtil cache = new EHCacheUtil(Constants.EHCACHE_MAILUID);
//		 System.out.println(cache.getParam(key)+"<>"+cache.isExist(key));
//		 cache.putParam(key, "刘雷-中国");
//		 cache.removeParam(key);
//		 System.out.println(cache.getParam(key)+"<>"+cache.isExist(key));
//	 }

}