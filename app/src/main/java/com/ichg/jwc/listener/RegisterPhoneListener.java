package com.ichg.jwc.listener;

public interface RegisterPhoneListener {

	void onPhoneDuplicate();

	void onPhoneNoUsed();

	void startVerifyPhone();

	void onResendVerifyCodeSuccess();

	void onRegisterLoginSuccess(int loginNavigationType);

	void onRequestFail(int errorType, String message);

}
