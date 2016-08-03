package com.ichg.jwc;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.jwc.utils.JWCPreference;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.gcm.GCMDevicesAPI;
import com.ichg.service.api.gcm.GCMRemoveAPI;
import com.ichg.service.framework.Api;
import com.ichg.service.framework.VolleyRequestExecutor;
import com.ichg.service.utils.Debug;

import io.fabric.sdk.android.Fabric;

public class JoinWorkerApp extends Application {
	private final static String GCM_SENDERID = "914853625297";
	public static JWCPreference preference;
	public static JoinWorkerApp instance;
//	public static NotifyController notifyController;
//	private Tracker tracker;
	public static boolean isDebug;
	public static ApiFacade apiFacade;
	public static AccountManager accountManager;

	public static final String AUTHORITY = "com.ichg.joinworker.search";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/suggestions");

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		instance = this;
		preference = new JWCPreference(this);
		isDebug = getResources().getBoolean(R.bool.in_debug);
//		notifyController = new NotifyController();
//		FacebookSdk.sdkInitialize(getApplicationContext());
//		UpgradeUtils.upgradeCheck(getApplicationContext());
//		CacheUtils.initBitmapLruCache(getApplicationContext());
//		Debug.setDebugEnabled(isDebug);
		if (isDebug) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			Log.i("e-join work", "\nDevice Information:" +
					"\nBrand Name = " + Build.BRAND + ", Model Name = " + Build.MODEL +
					"\nAndroid Version: " + Build.VERSION.RELEASE +
					"\nDPI: width = " + (int) (metrics.widthPixels / metrics.density) + "dp, " +
					"height = " + (int) (metrics.heightPixels / metrics.density) + "dp" +
					"\nPixels: width = " + metrics.widthPixels + "px, height = " + metrics.heightPixels + "px" +
					"\ndensityDpi: " + metrics.densityDpi);
		}
		int connectType = isDebug ? 1 : 0;
		apiFacade = new ApiFacade(new VolleyRequestExecutor(this), connectType);
		apiFacade.updateUserToken(preference.getUserToken());
		accountManager = new AccountManager(apiFacade, preference);
//		BitmapUtils.init(this);
	}

	public boolean isDebug() {
		return isDebug;
	}

	public static void logout(LogoutListener logoutListener) {
		removeGCMDevice(logoutListener);
	}

	private static void removeGCMDevice(LogoutListener logoutListener) {
		apiFacade.request(new GCMRemoveAPI(instance, preference.getRegistrationID()).success(new Api.ApiListener<String>() {
			@Override
			public void onSuccess(String response) {
				logoutListener.onSuccess();
				//		instance.getContentResolver().delete(CONTENT_URI, null, null);
				apiFacade.updateUserToken("");
				//		new FacebookUtils().logout();
				accountManager.logout();
			}
		}), instance);
	}

	public static void registerGCM(GCMRegisterListener gcmRegisterListener) {
		try {
			if (instance.getPackageManager().getPackageInfo("com.google.android.gsf", 0) != null
					&& "".equals(preference.getRegistrationID()) && !TextUtils.isEmpty(preference.getUserToken())) {
				Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
				intent.setPackage("com.google.android.gsf");
				intent.putExtra("app", PendingIntent.getBroadcast(instance, 0, new Intent(), 0));
				intent.putExtra("sender", GCM_SENDERID);
				instance.startService(intent);
			} else if (!TextUtils.isEmpty(preference.getUserToken())) {
				JoinWorkerApp.apiFacade.request(new GCMDevicesAPI(instance, preference.getRegistrationID())
						.success(response -> {
							preference.setAccountStatus(response);
							gcmRegisterListener.onSuccess();
						}), instance);
			}
		} catch (Exception e) {
			Debug.e("register gcm exception " + Log.getStackTraceString(e));
		}
	}

	public interface LogoutListener {
		void onSuccess();
	}

	public interface GCMRegisterListener {
		void onSuccess();
	}

}
