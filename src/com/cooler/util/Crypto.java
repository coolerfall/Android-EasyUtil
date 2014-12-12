package com.cooler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
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
	 * Md5 hash.
	 */
	public static class MD5 {
		private static final String NULL = "";

		/**
		 * Used to encrypt string to md5 hash.
		 * 
		 * @param  origin the string to be encrpted
		 * @return        hash string
		 */
		public static String encrypt(String origin) {
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
		
		/**
		 * Caculate md5 hash of specifed file. This may spend some time, 
		 * use new Thread if necessary.
		 * 
		 * @param  filepath the path of file
		 * @return          md5 hash string
		 */
		public static String file(String filepath) {
			File file = new File(filepath);
			if (file == null || !file.exists()) {
				return NULL;
			}
			
			String result = NULL;
			FileInputStream fis = null;
			
			try {
				fis = new FileInputStream(file);
				MappedByteBuffer mbf = fis.getChannel().map(MapMode.READ_ONLY, 0, file.length());
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(mbf);
				BigInteger bi = new BigInteger(1, md5.digest());
				result = bi.toString(16);
			} catch (Exception e) {
				Log.e(TAG, "[md5 encrypt error]: " + e.getMessage());
				return NULL;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						Log.e(TAG, "[md5 encrypt error]: " + e.getMessage());
					}
				}
			}
			
			return result;
		}
	}
	
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
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}

			if (text == null || text.length() == 0) {
				throw new Exception("Empty string");
			}

			byte[] encrypted = null;

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
		 * @param text the string to be decrypted
		 * @return the decrypted string
		 * @throws Exception
		 */
		public static String decrypt(String code) throws Exception {
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
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}

			if (code == null || code.length() == 0) {
				throw new Exception("Empty string");
			}

			byte[] decrypted = null;

			try {
				byte[] decode = Base64.decode(hexToByte(code), Base64.DEFAULT);

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
		 * @param data the data in byte
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
		 * @param data the data in byte
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
