package com.ichg.jwc.listener;

public interface PresenterListener {

	void onFail(int errorType, String message);

	void onSuccess();

	void onInputFormatCheckFinish(boolean isPass);

}
