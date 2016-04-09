package com.ichg.jwc.listener;

import com.ichg.service.entity.UserProfileBaseEntity;

public interface RegisterPhoneListener {

	void onPhoneDuplicate();

	void onPhoneNoUsed();

	void startVerifyPhone();

	void onVerifyPhoneFail(int errorType, String message);

	void onResendVerifyCodeSuccess();

	void onRegisterLoginSuccess(int loginNavigationType);

	void onRequestFail(int errorType, String message);

	void onGetVerifyCodeFail(int errorType, String message);

}
