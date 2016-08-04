package com.ichg.jwc;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.jwc.utils.JWCPreference;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.gcm.GCMDevicesAPI;
import com.ichg.service.api.gcm.GCMRemoveAPI;
import com.ichg.service.framework.VolleyRequestExecutor;
import com.ichg.service.utils.Debug;

import io.fabric.sdk.android.Fabric;

public class JoinWorkerApp extends Application {
	private final static String DEBUG_GCM_SENDERID = "914853625297";
	private final static String RELEASE_GCM_SENDERID = "126537140721";
	public static JWCPreference preference;
	public static JoinWorkerApp instance;
	public static boolean isDebug;
	public static ApiFacade apiFacade;
	public static AccountManager accountManager;


	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		instance = this;
		preference = new JWCPreference(this);
		isDebug = getResources().getBoolean(R.bool.in_debug);
		int connectType = isDebug ? 1 : 0;
		apiFacade = new ApiFacade(new VolleyRequestExecutor(this), connectType);
		apiFacade.updateUserToken(preference.getUserToken());
		accountManager = new AccountManager(apiFacade, preference);
	}

	public boolean isDebug() {
		return isDebug;
	}

	public static void logout(LogoutListener logoutListener) {
		removeGCMDevice(logoutListener);
	}

	private static void removeGCMDevice(LogoutListener logoutListener) {
		apiFacade.request(new GCMRemoveAPI(instance, preference.getRegistrationID()).success(response -> {
			logoutListener.onSuccess();
			apiFacade.updateUserToken("");
			accountManager.logout();
		}), instance);
	}

	public static void registerGCM(GCMRegisterListener gcmRegisterListener) {
		try {
			if (instance.getPackageManager().getPackageInfo("com.google.android.gsf", 0) != null
					&& "".equals(preference.getRegistrationID()) && !TextUtils.isEmpty(preference.getUserToken())) {
				Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
				intent.setPackage("com.google.android.gsf");
				intent.putExtra("app", PendingIntent.getBroadcast(instance, 0, new Intent(), 0));
				intent.putExtra("sender", isDebug ? DEBUG_GCM_SENDERID : RELEASE_GCM_SENDERID);
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
