package com.ichg.jwc.presenter;

public interface VerifyPhonePresenter {

	void startVerifyPhoneFlow(String phoneNo);

	void checkVerifyCode(String verifyCode);

	void cancel();

	void requestVerifyCodeFromServer(boolean isFirstTime);

	void resendVerifyCode();
}
