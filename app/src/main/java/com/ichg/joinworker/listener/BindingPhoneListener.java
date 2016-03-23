package com.ichg.joinworker.listener;

public interface BindingPhoneListener {

	void onResendVerifyCodeSuccess();

	void startVerifyPhone();

	void onVerifyPhoneSuccess(int loginNavigationType);

	void onVerifyPhoneFail(int errorType, String message);

	void onRequestFail(int errorType, String message);

	boolean isModifyMode();

	void onBindPhoneFail(int errorCode, String errorMessage);

	void onSettingBindSuccess();
}
