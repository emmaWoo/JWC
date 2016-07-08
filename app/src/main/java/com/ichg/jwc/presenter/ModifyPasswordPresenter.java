package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.ModifyPasswordApi;

public class ModifyPasswordPresenter {

	private static final int PASSWORD_MINI_SIZE = 6;

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private PresenterListener presenterListener;

	public ModifyPasswordPresenter(ApiFacade apiFacade, AccountManager accountManager, PresenterListener presenterListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.presenterListener = presenterListener;
	}

	public void updatePassword(String password, String newPassword) {
		mApiFacade.request(new ModifyPasswordApi(password, newPassword)
				.success(response -> presenterListener.onSuccess())
				.fail(presenterListener::onFail), this);
	}

	public void checkDataAvailable(String password, String newPassword, String checkPassword) {
		boolean isPasswordPass = !password.isEmpty() && password.length() >= PASSWORD_MINI_SIZE;
		boolean isNewPasswordPass = !newPassword.isEmpty() && newPassword.length() >= PASSWORD_MINI_SIZE;
		boolean isCheckPasswordPass = !checkPassword.isEmpty() && checkPassword.length() >= PASSWORD_MINI_SIZE;
		presenterListener.onInputFormatCheckFinish(isPasswordPass && isNewPasswordPass && isCheckPasswordPass);
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}
}
