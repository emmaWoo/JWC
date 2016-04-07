package com.ichg.jwc.presenter;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.activity.VerifyPhoneActivity;
import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;

public class PresenterFactory {

	public static AccountLoginPresenter createAccountLoginPresenter(AccountManager accountManager, PresenterListener presenterListener) {
		return new AccountLoginPresenter(accountManager, presenterListener);
	}

	public static VerifyPhonePresenter createVerifyPhonePresenter(VerifyPhoneActivity activity) {
		VerifyPhonePresenter presenter = new RegisterPhonePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, activity);
		return presenter;
	}

}
