package com.cooler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Caculate hash string of file or string.
 *
 * @author Vincent Cheung
 * @since  Dec. 23, 2014
 */
public class Hash {
	private static final String NULL = "";

	/** hash type */
	private enum HashType {
		MD5("MD5"),
		SHA1("SHA1");

		String type;

		HashType(String type) {
			this.type = type;
		}

		public String getValue() {
			return type;
		}
	}

	/** cacultate string to hash string */
	private static String strHash(HashType type, String origin) {
		try {
			MessageDigest md = MessageDigest.getInstance(type.getValue());
			md.update(origin.getBytes("UTF-8"));
			BigInteger bi = new BigInteger(1, md.digest());

			return bi.toString(16);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			return NULL;
		}
	}

	/** caculate file to hash string */
	private static String fileHash(HashType type, String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return NULL;
		}

		String result = NULL;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			MappedByteBuffer mbf = fis.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md = MessageDigest.getInstance(type.getValue());
			md.update(mbf);
			BigInteger bi = new BigInteger(1, md.digest());
			result = bi.toString(16);
		} catch (Exception e) {
			return NULL;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					/* ignore */
				}
			}
		}

		return result;
	}

	/**
	 * Caculate MD5 hash string.
	 */
	public static class MD5 {
		/**
		 * Used to encrypt string to md5 hash.
		 *
		 * @param  origin the string to be encrpted
		 * @return        hash string
		 */
		public static String str(String origin) {
			return strHash(HashType.MD5, origin);
		}

		/**
		 * Caculate md5 hash of specifed file. This may spend some time,
		 * use new Thread if necessary.
		 *
		 * @param  filePath the path of file
		 * @return          md5 hash string
		 */
		public static String file(String filePath) {
			return fileHash(HashType.MD5, filePath);
		}
	}

	/**
	 * Caculate SHA1 hash string.
	 */
	public static class SHA1 {
		/**
		 * Used to encrypt string to sha1 hash.
		 *
		 * @param  origin the string to be encrpted
		 * @return        sha1 hash string
		 */
		public static String str(String origin) {
			return strHash(HashType.SHA1, origin);
		}

		/**
		 * Caculate sha1 hash of specifed file. This may spend some time,
		 * use new Thread if necessary.
		 *
		 * @param  filePath the path of file
		 * @return          sha1 hash string
		 */
		public static String file(String filePath) {
			return fileHash(HashType.SHA1, filePath);
		}
	}
}
