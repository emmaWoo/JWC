package com.ichg.jwc;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.jwc.utils.EWPreference;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.framework.VolleyRequestExecutor;

import io.fabric.sdk.android.Fabric;

public class JoinWorkerApp extends Application {
	public static EWPreference preference;
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
		preference = new EWPreference(this);
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

	public static void logout() {
//		removeGCMDevice();
//		preference.clearPrefs();
//		instance.getContentResolver().delete(CONTENT_URI, null, null);
//		apiFacade.updateUserToken("");
//		new FacebookUtils().logout();
		accountManager.logout();
	}

//	private static void removeGCMDevice() {
//		GCMDeviceRemoveAPI gcmDeviceRemoveAPI = new GCMDeviceRemoveAPI();
//		gcmDeviceRemoveAPI.setAPIListener(new APIListener() {
//			@Override
//			public void onAPIError(int errorCode, String errorMessage) {
//				Debug.e("device", "Unregister device error.");
//			}
//		});
//		gcmDeviceRemoveAPI.start();
//		preference.setRegistrationID("");
//	}

//	public static void registerGCM() {
//		try {
//			if (instance.getPackageManager().getPackageInfo("com.google.android.gsf", 0) != null
//					&& "".equals(preference.getRegistrationID())) {
//				Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
//				intent.setPackage("com.google.android.gsf");
//				intent.putExtra("app", PendingIntent.getBroadcast(instance, 0, new Intent(), 0));
//				intent.putExtra("sender", GCM_SENDERID);
//				instance.startService(intent);
//			}
//		} catch (Exception e) {
//			Debug.e("register gcm exception " + Log.getStackTraceString(e));
//		}
//	}

}
