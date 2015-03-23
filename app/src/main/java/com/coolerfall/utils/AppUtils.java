package com.coolerfall.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Contains some app utils here.
 *
 * @author Vincent Cheung
 * @since  Jan. 14, 2015
 */
public class AppUtils {
	private static final String TAG = AppUtils.class.getSimpleName();

	private static final String APK_MANIFEST = "AndroidManifest.xml";

	/**
	 * Exit current app(clear memory). This util needs
	 * `android.permission.KILL_BACKGROUND_PROCESSES` permission.
	 *
	 * @param context context
	 */
	public static void exitApp(Context context) {
		if (context == null) {
			return;
		}

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		manager.killBackgroundProcesses(context.getPackageName());
		System.exit(0);
	}

	/**
	 * To check whether the apk file has installed.
	 * 
	 * @param  context the context
	 * @param  apkFile the apk file
	 * @return         true if installed, otherwise return false
	 */
	public static boolean isApkInstalled(Context context, File apkFile) {
		if (context == null || apkFile == null || !apkFile.exists()) {
			return false;
		}

		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPkgs = packageManager.getInstalledPackages(0);
		PackageInfo pkgInfo = getApkInfo(context, apkFile);
		if (pkgInfo != null) {
			String pkgName = pkgInfo.packageName;

			for (PackageInfo info : installedPkgs) {
				if (pkgName.equals(info.packageName)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * To check if the specified service is running.
	 * This util needs `android.permission.GET_TASKS` permission.
	 *
	 * @param  context   the context
	 * @param  className class name of service
	 * @return           true if the service is running
	 */
	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo info : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (info.service.getClassName().equals(className)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the package info of the specified apk file.
	 * 
	 * @param  context the context
	 * @param  apkFile the apk file
	 * @return
	 */
	public static PackageInfo getApkInfo(Context context, File apkFile) {
		if (context == null || apkFile == null || !apkFile.exists()) {
			return null;
		}

		PackageManager packageManager = context.getPackageManager();
		PackageInfo pkgInfo = packageManager.getPackageArchiveInfo(
				apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);

		return pkgInfo;
	}

	/**
	 * To check if the apk file is available to install.
	 * 
	 * @param apkFilePath the apk file path
	 * @return            true if available, otherwise return false
	 */
	public static boolean isApkAvailable(String apkFilePath) {
		File apkFile = new File(apkFilePath);
		if (!apkFile.exists()) {
			return false;
		}

		try {
			ZipFile zipFile = new ZipFile(apkFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				if (APK_MANIFEST.equals(zipEntry.getName())) {
					zipFile.close();
					return true;
				}
			}

			zipFile.close();
		} catch (Exception e) {
			return false;
		}

		return false;
	}
	
	/**
	 * To check if the app has opened ever(most time).
	 * 
	 * @param  context     context
	 * @param  packageName package name
	 * @return             true if the app has actived, otherwise return false
	 */
	public static boolean isAppActivated(Context context, String packageName) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			if (packageInfo == null) {
				return false;
			}
			
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			File file = new File(appInfo.dataDir);

			/* if the /data/data/<packagename> is not existed, return false */
			if (file == null || !file.exists()) {
				return false;
			}
			
			long lastUpdateTime = packageInfo.lastUpdateTime;
			long lastModifiedTime = file.lastModified();
			
			/* if the delta time is greater than 1.5s(most time), then the app has activated */
			if (Math.abs(lastModifiedTime - lastUpdateTime) >= 1500) {
				return true;
			}
			
			return false;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Get the installed app list.
	 * 
	 * @param  context context
	 * @return         app list
	 */
	public static List<Map<String, String>> getAppList(Context context) {
		List<Map<String, String>> appList = new ArrayList<>();
		
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i ++) {
			PackageInfo packageInfo = packages.get(i); 
			/* not system app */
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				Map<String, String> res = new HashMap<>();
				ApplicationInfo appInfo = packageInfo.applicationInfo;
				res.put(appInfo.packageName, (String) pm.getApplicationLabel(appInfo));
				appList.add(res);
			}
		}
		
		return appList;
	}

	/**
	 * Get the app name according to the apk file.
	 * 
	 * @param context the context
	 * @param apkFile the apk file
	 * @return the app name
	 */
	public static String getApkName(Context context, File apkFile) {
		String CLASS_PACKAGEPARSER = "android.content.pm.PackageParser";
		String CLASS_ASSERTMANAGER = "android.content.res.AssetManager";

		try {
			Class<?> pkgParserCls = Class.forName(CLASS_PACKAGEPARSER);
			Class<?>[] typeArgs = { String.class };
			Constructor<?> pkgParserConstruct = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = { apkFile.getAbsolutePath() };
			Object pkgParser = pkgParserConstruct.newInstance(valueArgs);

			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			typeArgs = new Class<?>[] { File.class, String.class, DisplayMetrics.class,
					int.class };
			Method parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
					typeArgs);
			valueArgs = new Object[] { apkFile, apkFile.getAbsolutePath(), metrics, 0 };
			Object pkgParserPkg = parsePackageMtd.invoke(pkgParser, valueArgs);

			Field appInfoFld = pkgParserPkg.getClass()
					.getDeclaredField("applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

			Class<?> assetMagCls = Class.forName(CLASS_ASSERTMANAGER);
			Object assetMag = assetMagCls.newInstance();
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
					typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkFile.getAbsolutePath();
			addAssetPathMtd.invoke(assetMag, valueArgs);

			Resources res = context.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();

			res = resCt.newInstance(valueArgs);
			String name = (String) res.getText(info.labelRes);
			return name;
		} catch (Exception e) {
			Log.e(TAG, "get apk name error: " + e.getMessage());
		}

		return null;
	}

	/**
	 * To check if the SD card is availbale.
	 *
	 * @return true if available, otherwise return false
	 */
	public static boolean isSDCardAvailable() {
		return Environment.MEDIA_MOUNTED
				.equals(Environment.getExternalStorageState());
	}

	/**
	 * Get the left size of the SDCard in Bytes.
	 * 
	 * @return the left size
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardLeftSize() {
		File sdcard = Environment.getExternalStorageDirectory();
		if (sdcard.exists()) {
			StatFs statFs = new StatFs(sdcard.getAbsolutePath());
			long blockSize = statFs.getBlockSize();
			long availableBlocks = statFs.getAvailableBlocks();

			return blockSize * availableBlocks;
		}

		return 0;
	}
}
