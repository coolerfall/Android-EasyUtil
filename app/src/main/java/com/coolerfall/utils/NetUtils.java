package com.coolerfall.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
	 * Get the ip of current mobile device. This util needs
	 * "android.permission.ACCESS_WIFI_STATE" and "android.permission.INTERNET" permission.
	 */
	public static String getIp(Context context) {
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String ipAddress = "0.0.0.0";

		if (manager == null) {
			return ipAddress;
		}

		if (manager.isWifiEnabled()) {
			WifiInfo info = manager.getConnectionInfo();
			int ip = info.getIpAddress();
			ipAddress = (ip & 0xff) + "." + ((ip >> 8) & 0xff) + "." +
					((ip >> 16) & 0xff) + "." + ((ip >> 24) & 0xff);
		} else {
			try {
				Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
				for (; en.hasMoreElements();) {
					NetworkInterface nitf = en.nextElement();
					Enumeration<InetAddress> inetAddrs = nitf.getInetAddresses();
					for (;inetAddrs.hasMoreElements();) {
						InetAddress inetAddr = inetAddrs.nextElement();
						if (!inetAddr.isLoopbackAddress()) {
							ipAddress = inetAddr.getHostAddress();
							break;
						}
					}
				}
			} catch (SocketException e) {
				/* ignore */
			}
		}

		return ipAddress;
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
