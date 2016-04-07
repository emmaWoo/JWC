package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.ForgetPasswordApi;

public class ForgetPasswordPresenter {

	private static final int ACCOUNT_MINI_SIZE = 10;
	private static final int ID_MINI_SIZE = 10;

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private PresenterListener presenterListener;

	public ForgetPasswordPresenter(ApiFacade apiFacade, AccountManager accountManager, PresenterListener presenterListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.presenterListener = presenterListener;
	}

	public void forgetPassword(String id, String phoneNo) {
		mApiFacade.request(new ForgetPasswordApi(phoneNo, id)
				.success(this::onSuccess)
				.fail(presenterListener::onFail), this);
	}

	private void onSuccess(String status) {if (status == "OK") {
			presenterListener.onSuccess();
		}
	}

	public void checkDataAvailable(String id, String phoneNo) {
		boolean isPhoneNoPass = phoneNo.length() >= ACCOUNT_MINI_SIZE;
		boolean isIdPass = id.length() >= ID_MINI_SIZE;
		presenterListener.onInputFormatCheckFinish(isPhoneNoPass && isIdPass);
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}

}
