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
import com.ichg.service.object.NotifyDataEntity;
import com.ichg.service.object.PushNotificationInfo;
import com.ichg.service.utils.Debug;

public class GCMReceiver extends BroadcastReceiver {

	//public static String currentChatRoomId = "";

	private final static String GCM_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
	private final static String GCM_RECEIVE = "com.google.android.c2dm.intent.RECEIVE";

	private final static int ID_TYPE_NORMAL = 0;
	private final static int ID_TYPE_CHAT = 1;
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

		int notificationType = ID_TYPE_NORMAL;
//		String protocolUrl = pushNotificationInfo.notifyDataEntity.protocalUrl;
//		if (protocolUrl.contains(ProtocolUtils.ACTION_CHATS)) {
//			if (!currentChatRoomId.isEmpty() && protocolUrl.contains(currentChatRoomId)) {
//				return;
//			}
//			notificationType = ID_TYPE_CHAT;
//		}
		final int finalNotificationType = notificationType;
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... params) {
//
//				return null;
//			}
//
//		}.execute();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(indexId, buildNotification(context, finalNotificationType, pushNotificationInfo));
		indexId++;
	}

	private Notification buildNotification(Context context, int notificationType, PushNotificationInfo pushNotificationInfo) {
//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(buildProtocolActionString(pushNotificationInfo.notifyDataEntity)));
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra("protocol_title", pushNotificationInfo.title);
//		Builder normalBuilder = createNormalNotificationBuilder(context, intent, pushNotificationInfo);
//		Notification notification;
//		switch (notificationType) {
//			case ID_TYPE_CHAT:
//				SharedPreferences preferences = context.getSharedPreferences(NotificationSettingActivity.SHARE_PREFERENCES_NOTIFICATION, Context.MODE_PRIVATE);
//				Builder chatBuilder = createChatNotificationBuilder(context, pushNotificationInfo, normalBuilder);
//				notification = chatBuilder.build();
//				if (preferences.getBoolean(NotificationSettingActivity.SP_KEY_SHAKE, true)) {
//					notification.defaults |= Notification.DEFAULT_VIBRATE;
//				}
//				if (preferences.getBoolean(NotificationSettingActivity.SP_KEY_SOUND, true)) {
//					notification.defaults |= Notification.DEFAULT_SOUND;
//				}
//				notification.deleteIntent = PendingIntent.getService(context, 0, new Intent(context, CleanUpHistoryNotificationService.class), 0);
//				break;
//			default:
//				notification = normalBuilder.build();
//				break;
//		}
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pushNotificationInfo.protocalUrl));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("protocol_title", pushNotificationInfo.title);
		Builder normalBuilder = createNormalNotificationBuilder(context, intent, pushNotificationInfo);
		return normalBuilder.build();
	}

//	private Bitmap getUserIcon(Context context, SenderEntity senderInfo) {
//		Bitmap userIcon = null;
//		try {
//			userIcon = Picasso.with(context).load(senderInfo.userIconUrl)
//					.transform(new CircleTransform()).placeholder(R.drawable.bg_avatar_user_default).get();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return userIcon;
//	}

//	private NotificationCompat.InboxStyle createMultiMessageStyle(ArrayList<PushNotificationInfo> historyEntities, PushNotificationInfo
//			newEntity) {
//		NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
//		style = style.addLine(newEntity.contentText);
//		boolean hasMoreMessage = historyEntities.size() > 2;
//		int displayCount = hasMoreMessage ? 2 : historyEntities.size();
//		for (int i = 0; i < displayCount; i++) {
//			style.addLine(historyEntities.get(i).contentText);
//		}
//		if (hasMoreMessage) {
//			style.setSummaryText("+" + (historyEntities.size() - 2) + " more");
//		}
//		return style;
//	}

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

//	private android.support.v4.app.NotificationCompat.Builder createChatNotificationBuilder(Context context, PushNotificationInfo pushNotificationInfo, Builder chatBuilder) {
//		Bitmap userIcon = getUserIcon(context, pushNotificationInfo.notifyDataEntity.senderInfo);
//		try {
//			userIcon = Picasso.with(context).load(pushNotificationInfo.notifyDataEntity.senderInfo.userIconUrl)
//					.transform(new CircleTransform()).placeholder(R.drawable.bg_avatar_user_default).get();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		CitytalkPreference preference = new CitytalkPreference(context);
//		ArrayList<PushNotificationInfo> historyEntities = generateHistoryEntities(preference.getHistoryNotifyMessagies());
//		Collections.sort(historyEntities, new Comparator<PushNotificationInfo>() {
//			@Override
//			public int compare(PushNotificationInfo lhs, PushNotificationInfo rhs) {
//				return lhs.notifyDataEntity.messageCreateTime > rhs.notifyDataEntity.messageCreateTime ? -1 : 1;
//			}
//		});
//		if (historyEntities.size() > 0) {
//			chatBuilder.setStyle(createMultiMessageStyle(historyEntities, pushNotificationInfo));
//			for (int i = 0; i < historyEntities.size(); i++) {
//				if (historyEntities.get(i).notifyDataEntity.senderInfo.userId != pushNotificationInfo.notifyDataEntity.senderInfo.userId) {
//					break;
//				} else if (i == historyEntities.size() - 1) {
//					chatBuilder.setLargeIcon(userIcon);
//				}
//			}
//		} else {
//			chatBuilder.setLargeIcon(userIcon);
//		}
//
//		historyEntities.add(pushNotificationInfo);
//		preference.setHistoryNotifyMessagies(new Gson().toJson(historyEntities));
//		return chatBuilder;
//	}

//	@SuppressWarnings("unchecked")
//	private ArrayList<PushNotificationInfo> generateHistoryEntities(String historyJson) {
//		Type type = new TypeToken<ArrayList<PushNotificationInfo>>() {
//		}.getType();
//		return historyJson == null ?
//				new ArrayList<PushNotificationInfo>() :
//				(ArrayList<PushNotificationInfo>) new Gson().fromJson(historyJson, type);
//	}

	private String buildProtocolActionString(NotifyDataEntity entity) {
		String url = entity.protocalUrl;
//		if (url.contains(ProtocolUtils.ACTION_CHATS)) {
//			int userId = entity.senderInfo.userId;
//			url = url + "/" + userId;
//		}
		Debug.i("GCMReceiver action=" + url);
		return url;
	}
}