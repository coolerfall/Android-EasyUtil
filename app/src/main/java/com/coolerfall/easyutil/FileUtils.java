package com.coolerfall.easyutil;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {
	private static final String TAG = FileUtils.class.getSimpleName();
	
	/**
	 * Save content into the specified file.
	 * 
	 * @param filename absolute file path
	 * @param content  content
	 * @param mode     the file access mode, either "r", "rw", "rws" or "rwd".
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void write(String filename, String content, String mode) {
		try {
			File file = new File(filename);
			if (file.isDirectory()) {
				return;
			}
			
			File parentFile = file.getParentFile();
			parentFile.mkdirs();
			
			RandomAccessFile raf = new RandomAccessFile(filename, mode);
			byte[] buffer = content.getBytes("UTF-8");
			raf.write(buffer);
			raf.close();
		} catch (IOException e) {
			Log.e(TAG, "save file error: " + e.getMessage());
		}
	}

	/**
	 * Save content into the specified file.
	 *
	 * @param filename    absolute path
	 * @param content     the content to save
	 * @param shouldClear should clear the file first or not
	 */
	public static void write(String filename, String content, boolean shouldClear) {
		if (shouldClear) {
			delete(filename);
		}

		write(filename, content, "rw");
	}
	
	/**
	 * Save content into the specified file.
	 * 
	 * @param filename absolute path
	 * @param content  the content to save
	 */
	public static void write(String filename, String content) {
		write(filename, content, true);
	}

	/**
	 Save content into the specified file.
	 *
	 * @param file    file
	 * @param content the content to save
	 */
	public static void write(File file, String content) {
		write(file.getAbsolutePath(), content, true);
	}
	
	/**
	 * Read content from specified file.
	 * 
	 * @param  filename absolute file path
	 * @return content content
	 */
	public static String read(String filename) {
		try {
			RandomAccessFile raf = new RandomAccessFile(filename, "r");
			int fileLen = (int) raf.length();
			byte[] buffer = new byte[fileLen];
			
			int len = raf.read(buffer);
			raf.close();
			if (len != fileLen) {
				return null;
			}
			
			return new String(buffer);
		} catch (IOException e) {
			Log.e(TAG, "read file error: " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Delete file if existed.
	 * 
	 * @param filename absolute path
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void delete(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			return;
		}
		
		file.delete();
	}

	/**
	 * Get the extension for the specified file.
	 *
	 * @param  filePath file path
	 * @return          extension of file or empty if file has no extension
	 */
	public static String getExtention(String filePath) {
		return getExtention(new File(filePath));
	}

	/**
	 * Get the extension for the specified file.
	 *
	 * @param  file file to get extension
	 * @return      extension of file or empty if file has no extension
	 */
	public static String getExtention(File file) {
		String ext = "";

		if (!file.exists()) {
			return ext;
		}

		String filename = file.getName();
		int index = filename.lastIndexOf(".");
		if (index != -1 && index != 0) {
			ext = filename.substring(index+1);
		}

		return ext;
	}
}
