package com.droiddev.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class PictureUtil
{
	private static final String TAG = PictureUtil.class.getSimpleName();
	
	/**
	 * Compress the pitcure.
	 * 
	 * @param  picturePath the absolute path of picture
	 * @throws FileNotFoundException 
	 */
	public static boolean compress(File file, int compressrRatio) throws FileNotFoundException {
		String filePath = file.getAbsolutePath();
		Bitmap bitmap = getSmallBitmap(filePath);
		FileOutputStream fos = new FileOutputStream(
				new File(file.getParent(), "tmp_" + file.getName()));

		return bitmap.compress(Bitmap.CompressFormat.JPEG, compressrRatio, fos);
	}
	
	/**
	 * Transfer bitmap to string.
	 * 
	 * @param  filePath
	 * @return the string
	 */
	public static String bitmapToString(String filePath) {
		Bitmap bm = getSmallBitmap(filePath);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		byte[] b = baos.toByteArray();
		
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * Caculate 
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		/*
		 *  Raw height and width of image
		 */
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			/* Calculate ratios of height and width to requested height and width */
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			/**
			 * Choose the smallest ratio as inSampleSize value, this will guarantee
			 * a final image with both dimensions larger than or equal to the
			 * requested height and width.
			 */
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Get small bitmap according to the path.
	 * 
	 * @param  filePath the path of file
	 * @return the bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		/* Calculate inSampleSize */
		options.inSampleSize = calculateInSampleSize(options, 720, 1080);

		/* Decode bitmap with inSampleSize set */
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * get bitmap
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmap(String filePath){
		return BitmapFactory.decodeFile(filePath);
	}
	
	/**
	 * Get small bitmap according to the path.
	 * 
	 * @param  filePath  the path of file
	 * @param  reqWidth  the width to compress
	 * @param  reqHeight the height to compress
	 * @return the compressed bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		/* Calculate inSampleSize */
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		/* Decode bitmap with inSampleSize set */
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * Transfer bitmap to bytes.
	 * 
	 * @param  bitmap the bitmap
	 * @return the byte array
	 */
	public static byte[] BitmapToBytes(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		
		return baos.toByteArray();
	}
	
	/**
	 * Transfer image byte array to bitmap.
	 * 
	 * @param  bytes the input image byte array
	 * @return the bitmap if the bytes is no null, otherwise return null
	 */
	public static Bitmap BytesToBitmap(byte[] bytes) {
		if (bytes.length != 0) {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		
		return null;
	}
	
	/**
	 * Save bitmap to path (.jpg)
	 * @param bitName the name to save
	 * @param bitmap  the bitmap
	 * @param path    the path to save
	 * @param suffix  the suffix of the bitmap to save(jpg, png or other)
	 */
	public static void saveBitmap(String bitName, Bitmap bitmap, String path, String suffix) {
		if (suffix == null || suffix.equals("")) {
			File f = new File(path + "/" + bitName + ".jpg");
		} else {
			File f = new File(path + "/" + bitName + "." + suffix);
		}

		try {
			f.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, "[save bitmap error]: " + e.getMessage());
		}

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}