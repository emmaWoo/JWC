package com.ichg.joinworker.presenter;

import com.ichg.joinworker.listener.AccountLoginListener;
import com.ichg.joinworker.manager.AccountManager;

public class AccountLoginPresenter {

	private static final int PASSWORD_MINI_SIZE = 8;

	private AccountManager mAccountManager;
	private AccountLoginListener mAccountLoginListener;

	public AccountLoginPresenter(AccountManager accountManager, AccountLoginListener accountLoginListener) {
		mAccountManager = accountManager;
		mAccountLoginListener = accountLoginListener;
	}

	public void onAccountLogin(String phoneNo, String password) {
		mAccountManager.accountLogin(phoneNo, password, mAccountLoginCallback);
	}

	AccountManager.LoginListener mAccountLoginCallback = new AccountManager.LoginListener() {
		@Override
		public void onLoginSuccess() {
			mAccountLoginListener.onLoginSuccess();
		}

		@Override
		public void onLoginFail(int errorType, String message) {
			mAccountLoginListener.onLoginFail(errorType, message);
		}
	};

	public void checkDataAvailable(String account, String password) {
		mAccountLoginListener.onInputFormatCheckFinish(!account.isEmpty() && password.length() >= PASSWORD_MINI_SIZE);
	}

	public int checkPageNavigation(){
		return mAccountManager.checkLoginNavigation();
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}
}
