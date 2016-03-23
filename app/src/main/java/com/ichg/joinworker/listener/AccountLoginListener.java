package com.ichg.joinworker.listener;

public interface AccountLoginListener {

	void onLoginSuccess();

	void onLoginFail(int errorType, String message);

	void onInputFormatCheckFinish(boolean isPass);

}
