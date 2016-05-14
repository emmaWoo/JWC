package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.RegisterPhoneListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.CheckPhoneStatusApi;
import com.ichg.service.api.login.GetVerifyCodeAPI;
import com.ichg.service.api.login.VerifyCodeLoginApi;
import com.ichg.service.entity.UserProfileBaseEntity;
import com.ichg.service.object.RegisterPhoneInfo;

public class RegisterPhonePresenter {

	private ApiFacade mApiFacade;
	private AccountManager accountManager;
	private RegisterPhoneListener registerPhoneListener;

	private RegisterPhoneInfo currentRegisterPhoneInfo;

	public RegisterPhonePresenter(ApiFacade apiFacade, AccountManager accountManager, RegisterPhoneListener registerPhoneView) {
		mApiFacade = apiFacade;
		this.accountManager = accountManager;
		registerPhoneListener = registerPhoneView;
	}

	public void startVerifyPhoneFlow(String phoneNo) {
		currentRegisterPhoneInfo = new RegisterPhoneInfo(phoneNo);
		checkPhoneNumberDuplicate(phoneNo);
	}

	private void checkPhoneNumberDuplicate(String phoneNo) {
		mApiFacade.request(new CheckPhoneStatusApi(phoneNo)
				.success(this::onCheckPhoneNumberDuplicateSuccess)
				.fail(registerPhoneListener::onRequestFail), this);
	}

	private void onCheckPhoneNumberDuplicateSuccess(String response) {
		if (response.equals("EXISTS")) {
			registerPhoneListener.onPhoneDuplicate();
		} else {
			registerPhoneListener.onPhoneNoUsed();
		}
	}

	public void checkVerifyCode(String verifyCode) {
		String phoneNo = currentRegisterPhoneInfo.phoneNo;
		mApiFacade.request(new VerifyCodeLoginApi(phoneNo, verifyCode)
				.success(response -> registerLogin(phoneNo))
				.fail(registerPhoneListener::onRequestFail), this);
	}

	private void registerLogin(String phoneNo) {
		accountManager.updateAccount(phoneNo);
		registerPhoneListener.onRegisterLoginSuccess(accountManager.checkLoginNavigation());
	}

	public void cancel() {
		mApiFacade.cancel(this);
	}

	public void requestVerifyCodeFromServer(final boolean isFirstTime) {
		String phoneNo = currentRegisterPhoneInfo.phoneNo;
		mApiFacade.request(new GetVerifyCodeAPI(phoneNo)
				.success(response -> {
					if (isFirstTime) {
						registerPhoneListener.startVerifyPhone();
					} else {
						registerPhoneListener.onResendVerifyCodeSuccess();
					}
				})
				.fail(registerPhoneListener::onRequestFail), this);
	}

	public void resendVerifyCode() {
		requestVerifyCodeFromServer(false);
	}
}
