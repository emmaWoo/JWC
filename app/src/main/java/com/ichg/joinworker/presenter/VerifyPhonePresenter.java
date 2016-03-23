package com.ichg.joinworker.presenter;

import android.os.Bundle;

import com.ichg.service.object.RegisterPhoneInfo;

public interface VerifyPhonePresenter {

	RegisterPhoneInfo getCurrentRegisterPhoneInfo();

	void startVerifyPhoneFlow(String phoneNo);

	void checkVerifyCode(String verifyCode);

	void cancel();

	void requestVerifyCodeFromServer(boolean isFirstTime);

	void saveState(Bundle bundle);

	void restoreState(Bundle bundle);

	void resendVerifyCode();

	void logout();

	boolean isSignIn();
}
