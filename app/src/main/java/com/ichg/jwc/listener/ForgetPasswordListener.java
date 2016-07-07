package com.ichg.jwc.listener;

public interface ForgetPasswordListener {

	void onFail(int errorType, String message);

	void onInputFormatCheckFinish(boolean isPass);

	void onSendPasswordSuccess();

	void onResendPasswordSuccess();

}
