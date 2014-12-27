package com.cooler.util;

import android.util.Log;

/**
 * Log custom utils, we can close or open log with debug flag.
 * 
 * @author Vincent Cheung
 * @since  Nov. 20, 2014
 */
public class Logx {
	/* set in application, the default is true */
	private static boolean sDebug = false;
	/* the default tag for log */
	private static String sTag = Logx.class.getSimpleName();

	/**
	 * Set debug, output log or not.
	 * 
	 * @param debug debug flag
	 */
	public static void setDebug(boolean debug) {
		sDebug = debug;
	}
	
	/**
	 * Set log tag.
	 * 
	 * @param tag log tag
	 */
	public static void setTag(String tag) {
		sTag = tag;
	}
	
	/**
	 * Output verbose logs.
	 * 
	 * @see {@link android.util.Log#v}
	 */
	public static void v(String tag, String msg) {
		if (sDebug)
			Log.v(tag, msg);
	}
	
	/**
	 * Output debug logs.
	 * 
	 * @see {@link android.util.Log#d}
	 */
	public static void d(String tag, String msg) {
		if (sDebug)
			Log.d(tag, msg);
	}
	
	/**
	 * Output information logs.
	 * 
	 * @see {@link android.util.Log#i}
	 */
	public static void i(String tag, String msg) {
		if (sDebug)
			Log.i(tag, msg);
	}
	
	/**
	 * Output warning logs.
	 * 
	 * @see {@link android.util.Log#w}
	 */
	public static void w(String tag, String msg) {
		if (sDebug)
			Log.w(tag, msg);
	}

	/**
	 * Output error logs.
	 * 
	 * @see {@link android.util.Log#e}
	 */
	public static void e(String tag, String msg) {
		if (sDebug)
			Log.e(tag, msg);
	}
	
	/**
	 * Output verbose logs.
	 * 
	 * @see {@link #v}
	 */
	public static void v(String msg) {
		v(sTag, msg);
	}
	
	/**
	 * Output debug logs.
	 * 
	 * @see {@link #d}
	 */
	public static void d(String msg) {
		d(sTag, msg);
	}
	
	/**
	 * Output information logs.
	 * 
	 * @see {@link #i}
	 */
	public static void i(String msg) {
		i(sTag, msg);
	}
	
	/**
	 * Output warning logs.
	 * 
	 * @see {@link #w}
	 */
	public static void w(String msg) {
		w(sTag, msg);
	}

	/**
	 * Output error logs.
	 * 
	 * @see {@link #e}
	 */
	public static void e(String msg) {
		e(sTag, msg);
	}

	/**
	 * Output verbose logs.
	 * 
	 * @see {@link #v}
	 */
	public static void v(Class<?> clazz, String msg) {
		v(clazz.getName(), msg);
	}
	
	/**
	 * Output debug logs.
	 * 
	 * @see {@link #d}
	 */
	public static void d(Class<?> clazz, String msg) {
		d(clazz.getName(), msg);
	}
	
	/**
	 * Output information logs.
	 * 
	 * @see {@link #i}
	 */
	public static void i(Class<?> clazz, String msg) {
		i(clazz.getName(), msg);
	}
	
	/**
	 * Output warning logs.
	 * 
	 * @see {@link #w}
	 */
	public static void w(Class<?> clazz, String msg) {
		w(clazz.getName(), msg);
	}
	
	/**
	 * Output error logs.
	 * 
	 * @see {@link #e}
	 */
	public static void e(Class<?> clazz, String msg) {
		e(clazz.getName(), msg);
	}
}