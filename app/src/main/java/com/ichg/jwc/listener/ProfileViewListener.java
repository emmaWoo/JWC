package com.ichg.jwc.listener;

import android.graphics.Bitmap;

import com.ichg.service.object.UserInfo;

public interface ProfileViewListener {

	void onFail(int errorType, String message);

	void onSuccess(UserInfo userInfo);

}
