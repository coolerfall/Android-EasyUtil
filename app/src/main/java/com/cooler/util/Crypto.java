package com.cooler.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

/**
 * Contains some crypto method and hash method.
 * 
 * @author Vincent Cheung
 * @since  Dec. 09, 2014
 */
public class Crypto {
	private static final String TAG = Crypto.class.getSimpleName();

	/**
	 * Aes crypto method.
	 */
	public static class AES {
		private static final String SECRECT_KEY = "@rehpic*&^%$#@easnilsisiht#$%^&*";
		private static IvParameterSpec ivspec = null;
		private static SecretKeySpec keyspec = null;
		private static Cipher cipher = null;

		/**
		 * Encrypt string.
		 * 
		 * @param text the string to be encrypted
		 * @return the encrypted an encoded string
		 * @throws Exception
		 */
		@SuppressLint("TrulyRandom")
		public static String encrypt(String text) throws Exception {
			/* get iv and key */
			byte[] keyBytes = SECRECT_KEY.getBytes();
			byte[] iv = new byte[16];

			for (int i = 0; i < keyBytes.length && i < iv.length; i++) {
				iv[i] = keyBytes[i];
			}

			ivspec = new IvParameterSpec(iv);
			keyspec = new SecretKeySpec(SECRECT_KEY.getBytes(), "AES");

			try {
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				Log.d(TAG, "AES encrypt error: " + e.getMessage());
			}

			if (text == null || text.length() == 0) {
				throw new Exception("Empty string");
			}

			byte[] encrypted;

			try {
				cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
				encrypted = cipher.doFinal(text.getBytes());
			} catch (Exception e) {
				throw new Exception("[encrypt] " + e.getMessage());
			}

			/* encode with base64 */
			byte[] encode = Base64.encode(encrypted, Base64.DEFAULT);

			return byteToHex(encode);
		}

		/**
		 * Decrypt string.
		 * 
		 * @param  text the string to be decrypted
		 * @return the decrypted string
		 * @throws Exception
		 */
		public static String decrypt(String text) throws Exception {
			/* get iv and key */
			byte[] keyBytes = SECRECT_KEY.getBytes();
			byte[] iv = new byte[16];

			for (int i = 0; i < keyBytes.length && i < iv.length; i++) {
				iv[i] = keyBytes[i];
			}

			ivspec = new IvParameterSpec(iv);
			keyspec = new SecretKeySpec(SECRECT_KEY.getBytes(), "AES");

			try {
				cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				Log.d(TAG, "AES encrypt error: " + e.getMessage());
			}

			if (text == null || text.length() == 0) {
				throw new Exception("Empty string");
			}

			byte[] decrypted;

			try {
				byte[] decode = Base64.decode(hexToByte(text), Base64.DEFAULT);

				cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
				decrypted = cipher.doFinal(decode);
			} catch (Exception e) {
				throw new Exception("[decrypt] " + e.getMessage());
			}

			return new String(decrypted);
		}

		/**
		 * Convert byte to hex.
		 * 
		 * @param  data the data in byte
		 * @return the hex string
		 */
		public static String byteToHex(byte[] data) {
			if (data == null) {
				return null;
			}

			int len = data.length;
			String str = "";
			for (int i = 0; i < len; i++) {
				if ((data[i] & 0xff) < 16)
					str = str + "0" + Integer.toHexString(data[i] & 0xff);
				else
					str = str + Integer.toHexString(data[i] & 0xff);
			}

			return str;
		}

		/**
		 * Convert hex to byte.
		 * 
		 * @param  str the data in byte
		 * @return the byte
		 */
		public static byte[] hexToByte(String str) {
			if (str == null) {
				return null;
			} else if (str.length() < 2) {
				return null;
			} else {
				int len = str.length() / 2;
				byte[] buffer = new byte[len];
				for (int i = 0; i < len; i++) {
					buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
				}

				return buffer;
			}
		}
	}
}
