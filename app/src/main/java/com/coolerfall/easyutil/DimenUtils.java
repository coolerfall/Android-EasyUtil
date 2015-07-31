package com.coolerfall.easyutil;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Dimen utils: constains some dimension utils.
 *
 * @author Vincent Cheung
 * @since  Jul. 31, 2015
 */
public class DimenUtils {
	/**
	 * Get the width of current screen.
	 *
	 * @param  contex the contex
	 * @return the width
	 */
	public static int getScreenWidth(Context contex) {
		DisplayMetrics dm = contex.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * Get the height of current screen.
	 *
	 * @param  contex the context
	 * @return the height
	 */
	public static int getScreenHeight(Context contex) {
		DisplayMetrics dm = contex.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * Convert dp to pixel.
	 *
	 * @param  context context to use
	 * @param  dp      dp
	 * @return         pixel
	 */
	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5);
	}

	/**
	 * Convert pixel to dp.
	 *
	 * @param  context the context to use
	 * @param  px      pixel
	 * @return         dp
	 */
	public static int pxToDp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5);
	}
}
