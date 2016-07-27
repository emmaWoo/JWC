package com.ichg.service.object;

import com.google.gson.annotations.SerializedName;

public class PushNotificationInfo {

	@SerializedName("title")
	public String title;

	@SerializedName("contentText")
	public String contentText;

	@SerializedName("notifyDataEntity")
	public NotifyDataEntity notifyDataEntity;

}
