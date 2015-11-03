package com.coolerfall.easyutil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Screenshot util: used to create a bitmap from current screen.
 *
 * @author Vincent Cheung
 * @since Nov. 03, 2015
 */
public class Scrshot {
	/**
	 * Take a screenshot with given activity.
	 *
	 * @param activity        the activity
	 * @return the bitmap of screenshot
	 */
	public static Bitmap takeScreenshot(Activity activity) {
		if (activity == null || activity.isFinishing()) {
			return null;
		}

		/* get the view to create screenshot */
		View scrView = activity.getWindow().getDecorView();
		scrView.setDrawingCacheEnabled(true);
		scrView.buildDrawingCache();
		Bitmap bmp = scrView.getDrawingCache();

		Rect rect = new Rect();
		scrView.getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		Bitmap scrBmp = null;
		try {
			scrBmp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
		} catch (IllegalArgumentException e) {
			if ((bmp != null) && (!bmp.isRecycled())) {
				scrBmp = Bitmap.createBitmap(bmp);
			}
		}

		/* clear view cache */
		scrView.setDrawingCacheEnabled(false);
		scrView.destroyDrawingCache();

		return scrBmp;
	}

	/**
	 * Take a screenshot and save into the specified filepath.
	 *
	 * @param activity        the activity to take screenshot
	 * @param filepath        the filepath to save screenshot
	 * @return true if save successfully, otherwise return false
	 */
	@SuppressWarnings("ConstantConditions")
	public static boolean saveScreenshot(Activity activity, String filepath) {
		File file = new File(filepath);
		if (file.isDirectory()) {
			return false;
		}

		Bitmap bitmap = takeScreenshot(activity);
		try {
			if (bitmap != null) {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				bitmap.recycle();
			}
		} catch (IOException e) {
			if (bitmap != null) {
				bitmap.recycle();
			}

			return false;
		}

		return true;
	}
}
