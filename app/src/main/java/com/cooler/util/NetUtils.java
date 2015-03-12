package com.cooler.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Net utils: contains some network utils.
 * 
 * @author Vincent Cheung
 * @since  Nov. 05, 2014
 */
public class NetUtils {
	/**
	 * To check if the network is available.
	 * 
	 * @param  context context
	 * @return true if available, otherwise return false
	 */
	public static boolean isNetAvailable(Context context) {
		if (context == null) {
			return false;
		}
		
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		return info == null ? false : info.isAvailable();
	}

	/**
	 * Open default browser with url.
	 *
	 * @param context context
	 * @param url     url to open
	 */
	public static void openBrowser(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}
	
	/**
	 * Ping the specified IP.
	 * 
	 * @param ip IP address
	 */
	public static String ping(String ip) {
		try {
			/* ping 3 times */
			Process process = Runtime.getRuntime().exec("ping -c 2 -w 100 " + ip);
			int status = process.waitFor();
			if (status != 0) {
				return "N/A";
			}
			
			InputStream is = process.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String content;
			while ((content = bf.readLine()) != null) {
				buffer.append(content);
				buffer.append("\n");
			}
			
			return buffer.toString();
		} catch (IOException | InterruptedException e) {
			return "N/A";
		}
	}
}
