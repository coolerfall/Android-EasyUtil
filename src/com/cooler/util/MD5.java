package com.cooler.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

/**
 * MD5 util: this util is used to eccrypt string with MD5.
 *
 * @author Vincent Cheung
 * @since May. 11, 2014
 */
public class MD5 {
	private static final String TAG = MD5.class.getSimpleName();
	private static final String NULL = "";

	public static String Encrypt(String origin) {
		MessageDigest md5 = null;

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "[md5 encrypt error]: " + e.getMessage());
			return NULL;
		}

		byte[] byteArray = null;
		try {
			byteArray = origin.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "[md5 encrypt error]: " + e.getMessage());
			return NULL;
		}

		byte[] md5Bytes = md5.digest(byteArray);
		/* transfer the result to hex string */
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}

			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
}
