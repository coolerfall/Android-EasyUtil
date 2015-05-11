package com.coolerfall.easyutil;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Shared preference util, it's easy to use like Prefers.load(...).read(...).
 * 
 * @author Vincent Cheung
 * @since  Oct. 29, 2014
 */
public class Prefers {
	private static Prefers sInstance;
	private Context mContext;
	
	public Prefers(Context context) {
		mContext = context;
	}
	
	/**
	 * Prefer file: shared preference file, save, read 
	 * or remove value via this class.
	 */
	public class PreferFile {
		SharedPreferences sp;
		
		public PreferFile(SharedPreferences sp) {
			this.sp = sp;
		}
		
		public void save(String key, int value) {
			sp.edit().putInt(key, value).commit();
		}
		
		public void save(String key, long value) {
			sp.edit().putLong(key, value).commit();
		}
		
		public void save(String key, float value) {
			sp.edit().putFloat(key, value).commit();
		}
		
		public void save(String key, boolean value) {
			sp.edit().putBoolean(key, value).commit();
		}
		
		public void save(String key, String value) {
			sp.edit().putString(key, value).commit();
		}
		
		public int getInt(String key, int defValue) {
			return sp.getInt(key, defValue);
		}
		
		public long getLong(String key, long defValue) {
			return sp.getLong(key, defValue);
		}
		
		public float getFloat(String key, float defValue) {
			return sp.getFloat(key, defValue);
		}
		
		public boolean getBoolean(String key, boolean defValue) {
			return sp.getBoolean(key, defValue);
		}
		
		public String getString(String key, String defValue) {
			return sp.getString(key, defValue);
		}
		
		public boolean contains(String key) {
			return sp.contains(key);
		}
		
		public void remove(String key) {
			sp.edit().remove(key).commit();
		}
		
		public void clear() {
			sp.edit().clear().commit();
		}
	}
	
	/**
	 * The global default shared preference util instance.
	 * This instance is automatically created with this method.
	 * 
	 * @param  context context
	 * @return         the single instance
	 */
	public static Prefers with(Context context) {
		synchronized (Prefers.class) {
			if (sInstance == null) {
				sInstance = new Prefers(context);
			}
		}
		
		return sInstance;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private SharedPreferences getPreferHoney(Context context, String preferName) {
		return context.getSharedPreferences(preferName, Context.MODE_MULTI_PROCESS);
	}

	/** get shared preferences according to api */
	private SharedPreferences getPrefer(Context context, String preferName) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return getPreferHoney(context, preferName);
		}

		return context.getSharedPreferences(preferName, Context.MODE_PRIVATE);
	}
	
	/**
	 * Load default shared preferences(packagename_preferences).
	 * 
	 * @return {@link PreferFile}
	 */
	public PreferFile load() {
		if (sInstance == null) {
			throw new IllegalStateException("call with(context) first");
		}

		if (mContext == null) {
			throw new IllegalArgumentException("context cannot be null");
		}

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);

		return new PreferFile(sp);
	}

	/**
	 * Load shared preferences according to prefer name.
	 *
	 * @param  preferName the name of shared preference
	 * @return            {@link PreferFile}
	 */
	public PreferFile load(String preferName) {
		if (sInstance == null) {
			throw new IllegalStateException("call with(context) first");
		}
		
		if (mContext == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		SharedPreferences sp = getPrefer(mContext, preferName);
		
		return new PreferFile(sp);
	}
}
