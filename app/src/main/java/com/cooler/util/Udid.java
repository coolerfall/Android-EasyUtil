package com.cooler.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings.Secure;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * UDID util: generate udid.
 * 
 * @author Vincent Cheung
 * @since  Dec. 11, 2014
 */
public class Udid {
	private static Udid sInstance;
	private static String sUdid = null;
	
	private Context mContext;
	
	/** keep single instance */
	private Udid(Context context) {
		mContext = context;
	}
	
	/**
	 * Get the global instance.
	 * 
	 * @param context you current context
	 */
	public static Udid with(Context context) {
		synchronized (Udid.class) {
			if (sInstance == null) {
				sInstance = new Udid(context);
			}
		}
		
		return sInstance;
	}
	
	/**
	 * Get udid, this may spend some time, use thread if necessary.
	 * 
	 * @return udid
	 */
	public String fetch() {
		generateOpenUdid();
		return sUdid;
	}

	/** generate udid */
	@SuppressLint("TrulyRandom")
	private void generateOpenUdid() {
		/* Try to get the ANDROID_ID */
		sUdid = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID); 
		if (sUdid == null || sUdid.equals("9774d56d682e549c") || sUdid.length() < 15 ) {
			/**
			 * if ANDROID_ID is null, or it's equals to the GalaxyTab 
			 * generic ANDROID_ID or bad, generates a new one
			 */
			final SecureRandom random = new SecureRandom();
			sUdid = new BigInteger(64, random).toString(16);
		}
    }
}
