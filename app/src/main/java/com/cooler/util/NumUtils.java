package com.cooler.util;

import java.util.Random;

import android.text.TextUtils;

/**
 * Number utils: contains some number utils.
 * 
 * @author Vincent Cheung
 * @since  Nov. 12, 2014
 */
public class NumUtils {
	/**
	 * Get the random number according to the cardinal number.
	 * 
	 * @param  cardinal the cardinal number, the max random number will be (cardinal-1)
	 * @return          the random number
	 */
	public static int random(int cardinal) {
		Random random = new Random();
		return cardinal == 0 ? 0 : Math.abs(random.nextInt()) % cardinal;
	}
	
	/**
	 * Parse number string to integer.
	 * 
	 * @param  num the integer number string
	 * @return     int number
	 */
	public static int parseInt(String num) {
		if (TextUtils.isEmpty(num) || TextUtils.isEmpty(num.trim())) {
			return 0;
		}
		
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException e) {
			/* if the num is float, get the integer */
			float value = parseFloat(num);
			
			return value == 0f ? 0 : Float.valueOf(value).intValue();
		}
	}
	
	/**
	 * Parse number string to long.
	 * 
	 * @param  num the long number string
	 * @return     long number
	 */
	public static long parseLong(String num) {
		if (TextUtils.isEmpty(num) || TextUtils.isEmpty(num.trim())) {
			return 0L;
		}
		
		try {
			return Long.parseLong(num);
		} catch (NumberFormatException e) {
			/* if the num is float, get the long integer */
			float value = parseFloat(num);
			
			return value == 0f ? 0L : Float.valueOf(value).longValue();
		}
	}
	
	/**
	 * Parse number string to float.
	 * 
	 * @param  num the float number string
	 * @return     float number
	 */
	public static float parseFloat(String num) {
		if (TextUtils.isEmpty(num) || TextUtils.isEmpty(num.trim())) {
			return 0f;
		}
		
		try {
			return Float.parseFloat(num);
		} catch(NumberFormatException e) {
			return 0f;
		}
	}
	
	/**
	 * Parse number string to boolean.
	 * 
	 * @param  num the boolean number string
	 * @return     boolean
	 */
	public static boolean parseBoolean(String num) {
		if (TextUtils.isEmpty(num) || TextUtils.isEmpty(num.trim())) {
			return false;
		}
		
		boolean result = Boolean.parseBoolean(num);
		if (!result) {
			/* the num may be '0' or '1' */
			int value = parseInt(num);
			return value == 1 ? true : false;
		}
		
		return result;
	}
}
