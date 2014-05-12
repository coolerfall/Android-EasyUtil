package com.droiddev.utils

import android.util.Log;

public class LogUtil
{
	/** set in application, the default is true */
	public static boolean isDebug = true;
	/* the default tag for log */
	private static final String TAG = "DroidDev";

	/* the four default method */
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	/* uotput log with given class */
	public static void i(Class<?> _class, String msg){
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void d(Class<?> _class, String msg){
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void e(Class<?> _class, String msg){
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void v(Class<?> _class, String msg){
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	/* output log with custom tag */
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}
}