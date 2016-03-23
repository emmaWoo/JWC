package com.ichg.joinworker.listener;

import com.ichg.service.entity.UserProfileBaseEntity;

public interface RegisterPhoneListener {

	void onPhoneDuplicate(UserProfileBaseEntity userProfileBase);

	void onPhoneNoUsed();

	void startVerifyPhone();

	void onVerifyPhoneFail(int errorType, String message);

	void onRegisterLoginFail(int errorType, String message);

	void onResendVerifyCodeSuccess();

	void onRegisterLoginSuccess(int loginNavigationType);

	void onRequestFail(int errorType, String message);

	void onGetVerifyCodeFail(int errorType, String message);

}
