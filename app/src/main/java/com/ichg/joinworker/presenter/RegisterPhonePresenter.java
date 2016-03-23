package com.ichg.joinworker.presenter;

import android.os.Bundle;

import com.ichg.joinworker.listener.RegisterPhoneListener;
import com.ichg.joinworker.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.CheckPhoneApi;
import com.ichg.service.api.login.GetVerifyCodeAPI;
import com.ichg.service.api.login.VerifyPhoneLoginApi;
import com.ichg.service.entity.UserProfileBaseEntity;
import com.ichg.service.object.RegisterPhoneInfo;

public class RegisterPhonePresenter implements VerifyPhonePresenter {

	private ApiFacade mApiFacade;
	private AccountManager accountManager;
	private RegisterPhoneListener registerPhoneListener;

	private RegisterPhoneInfo currentRegisterPhoneInfo;

	public RegisterPhonePresenter(ApiFacade apiFacade, AccountManager accountManager, RegisterPhoneListener registerPhoneView) {
		mApiFacade = apiFacade;
		this.accountManager = accountManager;
		registerPhoneListener = registerPhoneView;
	}

	public RegisterPhoneInfo getCurrentRegisterPhoneInfo() {
		return currentRegisterPhoneInfo;
	}

	public void startVerifyPhoneFlow(String phoneNo) {
		currentRegisterPhoneInfo = new RegisterPhoneInfo(phoneNo);
		checkPhoneNumberDuplicate(phoneNo);
	}

	private void checkPhoneNumberDuplicate(String phoneNo) {
		mApiFacade.request(new CheckPhoneApi(phoneNo)
				.success(this::onCheckPhoneNumberDuplicateSuccess)
				.fail(registerPhoneListener::onRequestFail), this);
	}

	private void onCheckPhoneNumberDuplicateSuccess(String response) {
		if (response.equals("EXISTS")) {
//			response.userProfileBase
			registerPhoneListener.onPhoneDuplicate(new UserProfileBaseEntity());
		} else {
			registerPhoneListener.onPhoneNoUsed();
		}
	}

	public void checkVerifyCode(String verifyCode) {
		String phoneNo = currentRegisterPhoneInfo.phoneNo;
		mApiFacade.request(new VerifyPhoneLoginApi(phoneNo, verifyCode)
				.success(response -> registerLogin(phoneNo, verifyCode))
				.fail(registerPhoneListener::onVerifyPhoneFail), this);
		accountManager.updateCellphone(phoneNo);
		registerPhoneListener.onRegisterLoginSuccess(accountManager.checkLoginNavigation());
	}

	private void registerLogin(String phoneNo, String verifyCode) {
		accountManager.registerLogin(phoneNo, verifyCode, new AccountManager.LoginListener() {
			@Override
			public void onLoginSuccess() {
				accountManager.updateCellphone(phoneNo);
				registerPhoneListener.onRegisterLoginSuccess(accountManager.checkLoginNavigation());
			}

			@Override
			public void onLoginFail(int errorType, String message) {
				registerPhoneListener.onRegisterLoginFail(errorType, message);
			}
		});
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
				.fail((errorType, message) -> registerPhoneListener.onGetVerifyCodeFail(errorType, message)), this);
	}

	public void saveState(Bundle bundle) {

	}

	public void restoreState(Bundle bundle) {

	}

	public void resendVerifyCode() {
		requestVerifyCodeFromServer(false);
	}

	@Override
	public void logout() {
		accountManager.logout();
	}

	@Override
	public boolean isSignIn() {
		return accountManager.isSignIn();
	}
}
