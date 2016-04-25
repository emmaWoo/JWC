package com.ichg.jwc.listener;

import android.graphics.Bitmap;

public interface ProfileListener {

	void onFail(int errorType, String message);

	void onSuccess();

	void onAvatarUpdateSuccess(Bitmap bitmap);

}
