package com.cooler.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Meta data util: used to read some information in manifest.
 * 
 * @author Vincent Chueng
 * @since  Dec. 03, 2014
 */
public class MetaData {
	private static MetaData sInstance;
	private Context mContext;
	
	/** default construcotr, keep single instance */
	private MetaData(Context context) {
		mContext = context;
	}
	
	/**
	 * Create a global instance of MetaDatas.
	 * 
	 * @param context context
	 */
	public static MetaData with(Context context) {
		synchronized (MetaData.class) {
			if (sInstance == null) {
				sInstance = new MetaData(context);
			}
		}
		
		return sInstance;
	}
	
	/* get meta data from manifest */
	private ApplicationInfo getMetaData(Context context) {
		if (sInstance == null) {
			throw new IllegalStateException("call with(context) first");
		}
		
		if (mContext == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		PackageManager manager = context.getPackageManager();
		try {
			return manager.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	
	/* get package info from manifest */
	private PackageInfo getPackageInfo(Context context) {
		if (sInstance == null) {
			throw new IllegalStateException("call with(context) first");
		}
		
		if (mContext == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		try {
			return mContext.getPackageManager()
					.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Read version name from manifest.
	 * 
	 * @return version name if existed, otherwise return default version.
	 */
	public String getVersionName() {
		PackageInfo info = getPackageInfo(mContext);
		return info != null ? info.versionName : "";
	}
	
	/**
	 * Read version code from manifest.
	 * 
	 * @return version code if existed, otherwise return 0.
	 */
	public int getVersionCode() {
		PackageInfo info = getPackageInfo(mContext);
		return info != null ? info.versionCode : 0;
	}
	
	/**
	 * Read string from manifest.
	 * 
	 * @param  key          key in manifest
	 * @param  defaultValue default value
	 * @return              value if exists, otherwise return default value
	 */
	public String getString(String key, String defaultValue) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return defaultValue;
		}
		
		return appInfo.metaData.getString(key).trim();
	}
	
	/**
	 * Read int from manifest.
	 * 
	 * @param  key key in manifest
	 * @return     value if exists, otherwise return 0
	 */
	public int getInt(String key) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return 0;
		}
		
		return appInfo.metaData.getInt(key);
	}
	
	/**
	 * Read long from manifest.
	 * 
	 * @param  key key in manifest
	 * @return     value if exists, otherwise return 0
	 */
	public long getLong(String key) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return 0;
		}
		
		return appInfo.metaData.getLong(key);
	}
	
	/**
	 * Read float from manifest.
	 * 
	 * @param  key key in manifest
	 * @return     value if exists, otherwise return 0f
	 */
	public float getFloat(String key) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return 0f;
		}
		
		return appInfo.metaData.getFloat(key);
	}
	
	/**
	 * Read boolean from manifest.
	 * 
	 * @param  key key in manifest
	 * @return     value if exists, otherwise return false
	 */
	public boolean getBoolean(String key) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return false;
		}
		
		return appInfo.metaData.getBoolean(key);
	}
	
	/**
	 * To check if the key contains in manifest
	 * 
	 * @param  key key in manifest
	 * @return true if contains, otherwise return false
	 */
	public boolean contains(String key) {
		ApplicationInfo appInfo = getMetaData(mContext);
		if (appInfo == null) {
			return false;
		}
		
		return appInfo.metaData.containsKey(key);
	}
}
