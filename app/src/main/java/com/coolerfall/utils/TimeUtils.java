package com.coolerfall.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Contains some time/data utils.
 *
 * @author Vincent Cheung
 * @since  Mar. 31, 2015
 */
public class TimeUtils {
	/**
	 * Format unix timestamp according to format.
	 *
	 * @param  unixTime unix timestamp
	 * @param  format   date format(such as: yyyy-MM-dd HH:mm:ss)
	 * @return          formatted date string
	 */
	public static String formatUnix(long unixTime, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date(unixTime * 1000);

		return dateFormat.format(date);
	}

	/**
	 * Format current timestamp according to date format.
	 *
	 * @param  format date format
	 * @return        formatted date string
	 */
	public static String formatNow(String format) {
		return formatUnix(System.currentTimeMillis()/1000, format);
	}
}
