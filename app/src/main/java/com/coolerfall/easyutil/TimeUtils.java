package com.coolerfall.easyutil;

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

	/**
	 * Format millisecond duration with specified format string, the duration is
	 * not timestamp but only a duration of time.
	 *
	 * @param msec   millisecond
	 * @param format format(such as: dd:HH:mm:ss:SSSS),
	 *               the max unit is day and the min unit is millisecond
	 * @return       formatted string
	 */
	public static String formatDuration(long msec, String format) {
		int day = (int) (msec / 86400000);
		int hour = (int) (msec % 86400000 / 3600000);
		int minute = (int) (msec % 86400000 % 3600000 / 60000);
		int second = (int) (msec % 86400000 % 3600000 % 60000 / 1000);
		int millisec = (int) (msec % 86400000 % 3600000 % 60000 % 1000);

		String hourStr = Integer.toString(hour);
		String minuteStr = Integer.toString(minute);
		String secondStr = Integer.toString(second);
		String millisecondStr = Integer.toString(millisec);

		if (format.contains("dd") && hour < 10) {
			hourStr = "0" + hourStr;
		}

		if (format.contains("HH") && minute < 10) {
			minuteStr = "0" + minuteStr;
		}

		if (format.contains("mm") && second < 10) {
			secondStr = "0" + secondStr;
		}

		if (millisec < 10) {
			millisecondStr = "00" + millisecondStr;
		} else if (millisec < 100) {
			millisecondStr = "0" + millisecondStr;
		}

		String result = format.replace("dd", Integer.toString(day));
		result = result.replace("HH", hourStr);
		result = result.replace("mm", minuteStr);
		result = result.replace("ss", secondStr);
		result = result.replace("SSSS", millisecondStr);

		return result;
	}
}
