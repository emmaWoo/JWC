package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.ActivateAccountApi;

public class RegisterPasswordPresenter {

	private static final int PASSWORD_MINI_SIZE = 6;

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private PresenterListener presenterListener;

	public RegisterPasswordPresenter(ApiFacade apiFacade, AccountManager accountManager, PresenterListener presenterListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.presenterListener = presenterListener;
	}

	public void activateAccount(String password) {
		mApiFacade.request(new ActivateAccountApi(mAccountManager.userAccount, password)
				.success(this::onSuccess)
				.fail(presenterListener::onFail), this);
	}

	private void onSuccess(String token) {
		mAccountManager.updateToken(token);
		presenterListener.onSuccess();
	}

	public void checkDataAvailable(String password, String newPassword) {
		boolean isPasswordPass = !password.isEmpty() && password.length() >= PASSWORD_MINI_SIZE;
		boolean isNewPasswordPass = !newPassword.isEmpty() && newPassword.length() >= PASSWORD_MINI_SIZE;
		boolean isPasswordSame = password.equals(newPassword);
		presenterListener.onInputFormatCheckFinish(isPasswordPass && isNewPasswordPass && isPasswordSame);
	}

	public int checkPageNavigation(){
		return mAccountManager.checkLoginNavigation();
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}
}
