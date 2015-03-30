package com.coolerfall.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Device util: used to get device information such as imei, cpu, hardware and so on.
 * 
 * @author Vincent
 * @since  Sept. 04, 2014
 */
public class DeviceUtils {
	private static final String TAG = DeviceUtils.class.getSimpleName();

	/**
	 * Get imei code of current device.
	 *
	 * @param  context context
	 * @return         imei code
	 */
	public static String getImei(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager == null ? "" : telephonyManager.getDeviceId();
	}

	/**
	 * Get mac address of current device. This util needs
	 * "android.permission.ACCESS_WIFI_STATE" permission.
	 *
	 * @param  context context
	 * @return         mac address
	 */
	public static String getMac(Context context) {
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (manager == null) {
			return "";
		}

		WifiInfo info = manager.getConnectionInfo();

		return info == null ? "" : info.getMacAddress();
	}

	/**
	 * Get the model of current mobile.
	 *
	 * @return mobile model
	 */
	public static String getModel() {
		Field[] fields = Build.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				if ("MODEL".equals(name)) {
					String model = field.get(null).toString();
					return model == null ? "" : model;
				}
			}
		} catch (Exception e) {
			/* ignore */
		}

		return "";
	}

	/**
	 * Get information of the mobile.
	 * 
	 * @return the information map
	 */
	public static Map<String, String> getMobileInfo() {
		Map<String, String> mobileInfo = new HashMap<>();
		
		Field[] fields = Build.class.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				
				String value = field.get(null).toString();
				mobileInfo.put(name, value);
			}
		} catch (Exception e) {
			Log.e(TAG, "get mobile info error: " + e.getMessage());
		}
		
		return mobileInfo;
	}
	
	/**
	 * Get the information of CPU.
	 * 
	 * @return the information string
	 */
	public static String getCpuInfo() {
		String result = "N/A";
		
		String args[] = {
			"/system/bin/cat",
			"/proc/cpuinfo"
		};
		
		ProcessBuilder cmdBuilder = new ProcessBuilder(args);
		try {
			Process process = cmdBuilder.start();
			InputStream is = process.getInputStream();
			byte[] buf = new byte[1024];
			result = "";
            while (is.read(buf) != -1) {
            	result = result + new String(buf);
            }
            is.close();
		} catch (IOException e) {
			Log.e(TAG, "get cpu max freq error: " + e.getMessage());
		}
		
		return result;
	}
}
