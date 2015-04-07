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
	 * Format millisecond according to format.
	 *
	 * @param  millis milliseconds
	 * @param  format date format(such as: yyyy-MM-dd HH:mm:ss)
	 * @return        formatted date string
	 */
	public static String formatMillis(long millis, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date(millis);

		return dateFormat.format(date);
	}

	/**
	 * Format unix timestamp according to format.
	 *
	 * @param  unixTime unix timestamp
	 * @param  format   date format(such as: yyyy-MM-dd HH:mm:ss)
	 * @return          formatted date string
	 */
	public static String formatUnix(long unixTime, String format) {
		return formatMillis(unixTime * 1000, format);
	}

	/**
	 * Format current timestamp according to date format.
	 *
	 * @param  format date format(such as: yyyy-MM-dd HH:mm:ss)
	 * @return        formatted date string
	 */
	public static String formatNow(String format) {
		return formatMillis(System.currentTimeMillis(), format);
	}
}
