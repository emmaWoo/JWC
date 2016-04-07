package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;

public class AccountLoginPresenter {

	private static final int ACCOUNT_MINI_SIZE = 10;
	private static final int PASSWORD_MINI_SIZE = 6;

	private AccountManager mAccountManager;
	private PresenterListener presenterListener;

	public AccountLoginPresenter(AccountManager accountManager, PresenterListener presenterListener) {
		mAccountManager = accountManager;
		this.presenterListener = presenterListener;
	}

	public void onAccountLogin(String phoneNo, String password) {
		mAccountManager.accountLogin(phoneNo, password, mAccountLoginCallback);
	}

	AccountManager.LoginListener mAccountLoginCallback = new AccountManager.LoginListener() {
		@Override
		public void onLoginSuccess() {
			presenterListener.onSuccess();
		}

		@Override
		public void onLoginFail(int errorType, String message) {
			presenterListener.onFail(errorType, message);
		}
	};

	public void checkDataAvailable(String account, String password) {
		presenterListener.onInputFormatCheckFinish(account.length() >= ACCOUNT_MINI_SIZE && password.length() >= PASSWORD_MINI_SIZE);
	}

	public int checkPageNavigation(){
		return mAccountManager.checkLoginNavigation();
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}
}
