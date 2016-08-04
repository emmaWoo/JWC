package com.ichg.jwc.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.service.api.gcm.GCMDevicesAPI;
import com.ichg.service.object.PushNotificationInfo;
import com.ichg.service.utils.Debug;

public class GCMReceiver extends BroadcastReceiver {


	private final static String GCM_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
	private final static String GCM_RECEIVE = "com.google.android.c2dm.intent.RECEIVE";

	private final static int ID_TYPE_NORMAL = 0;
	private int indexId = 0;

	@Override
	public void onReceive(Context context, Intent gcmIntent) {
		Debug.i("GCMReceiver gcmIntent=" + gcmIntent.getAction());
		PushNotificationInfo pushNotificationInfo = buildPushNotificationInfo(gcmIntent.getExtras());
		if (GCM_REGISTRATION.equals(gcmIntent.getAction())) {
			final String registrationId = gcmIntent.getStringExtra("registration_id");
			if (isRegisterFail(gcmIntent)) {
				JoinWorkerApp.preference.setRegistrationID("");
			} else {
				JoinWorkerApp.apiFacade.cancel(this);
				JoinWorkerApp.apiFacade.request(new GCMDevicesAPI(context, registrationId)
						.success(response -> {
							JoinWorkerApp.preference.setAccountStatus(response);
							JoinWorkerApp.preference.setRegistrationID(registrationId);
						}), this);
			}
		} else if (GCM_RECEIVE.equals(gcmIntent.getAction())) {
			sendPushNotification(context, pushNotificationInfo);
		}
	}

	private boolean isRegisterFail(Intent intent) {
		return intent.getStringExtra("unregistered") != null || intent.getStringExtra("error") != null || intent.getStringExtra("registration_id") == null;
	}

	private PushNotificationInfo buildPushNotificationInfo(Bundle extras) {
		Debug.i("extras: " + extras.toString());
		PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
		pushNotificationInfo.title = extras.getString("title");
		pushNotificationInfo.contentText = extras.getString("message");
		pushNotificationInfo.protocalUrl = extras.getString("deeplink");
		return pushNotificationInfo;
	}

	private void sendPushNotification(final Context context, final PushNotificationInfo pushNotificationInfo) {

		String gcmAction = pushNotificationInfo.protocalUrl;
		Debug.i("sendPushNotification: " + gcmAction);
		if (TextUtils.isEmpty(gcmAction) || gcmAction.equals("null") || TextUtils.isEmpty(pushNotificationInfo.title) || TextUtils.isEmpty(pushNotificationInfo.contentText)) {
			return;
		}

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(indexId, buildNotification(context, ID_TYPE_NORMAL, pushNotificationInfo));
		indexId++;
	}

	private Notification buildNotification(Context context, int notificationType, PushNotificationInfo pushNotificationInfo) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pushNotificationInfo.protocalUrl));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("protocol_title", pushNotificationInfo.title);
		Builder normalBuilder = createNormalNotificationBuilder(context, intent, pushNotificationInfo);
		return normalBuilder.build();
	}

	private android.support.v4.app.NotificationCompat.Builder createNormalNotificationBuilder(Context context, Intent contentIntent,
	                                                                                          PushNotificationInfo pushNotificationInfo) {
		return new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(pushNotificationInfo.contentText)
				.setContentTitle(pushNotificationInfo.title)
				.setContentText(pushNotificationInfo.contentText)
				.setAutoCancel(true)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setContentIntent(PendingIntent.getActivity(context, 0,
						contentIntent, PendingIntent.FLAG_UPDATE_CURRENT));
	}
}