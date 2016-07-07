package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.ForgetPasswordListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.ForgetPasswordApi;

public class ForgetPasswordPresenter {

	private static final int ACCOUNT_MINI_SIZE = 10;
	private static final int ID_MINI_SIZE = 10;

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private ForgetPasswordListener forgetPasswordListener;

	public ForgetPasswordPresenter(ApiFacade apiFacade, AccountManager accountManager, ForgetPasswordListener forgetPasswordListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.forgetPasswordListener = forgetPasswordListener;
	}

	public void forgetPassword(String id, String phoneNo, boolean isFirstTime) {
		mApiFacade.request(new ForgetPasswordApi(id, phoneNo)
				.success(response -> {
                    if (isFirstTime) {
                        forgetPasswordListener.onSendPasswordSuccess();
                    } else {
                        forgetPasswordListener.onResendPasswordSuccess();
                    }
                })
				.fail(forgetPasswordListener::onFail), this);
	}

	public void checkDataAvailable(String id, String phoneNo) {
		boolean isPhoneNoPass = phoneNo.length() >= ACCOUNT_MINI_SIZE;
		boolean isIdPass = id.length() >= ID_MINI_SIZE;
		forgetPasswordListener.onInputFormatCheckFinish(isPhoneNoPass && isIdPass);
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}

}
