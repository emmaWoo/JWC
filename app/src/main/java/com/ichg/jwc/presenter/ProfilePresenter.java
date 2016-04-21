package com.ichg.jwc.presenter;

import android.graphics.Bitmap;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.avatar.UploadAvatarAPI;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.setting.UpdateUserApi;
import com.ichg.service.object.UserInfo;

public class ProfilePresenter {

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private PresenterListener presenterListener;

	public ProfilePresenter(ApiFacade apiFacade, AccountManager accountManager, PresenterListener presenterListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.presenterListener = presenterListener;
	}

	public void saveProfile(UserInfo userInfo) {
		mApiFacade.request(new UpdateUserApi(userInfo)
				.success(response -> onSuccess(userInfo))
				.fail(presenterListener::onFail), this);
	}

	private void onSuccess(UserInfo userInfo) {
		mAccountManager.updateUserName(userInfo.userName);
		presenterListener.onSuccess();
	}

	public void uploadPhoto(Bitmap bitmap) {
		mApiFacade.request(new UploadAvatarAPI(bitmap)
				.success(response -> presenterListener.onSuccess())
				.fail(presenterListener::onFail), this);
	}

	public int checkPageNavigation(){
		return mAccountManager.checkLoginNavigation();
	}

	public void skipProfile() {
		mAccountManager.isSkipProfile = true;
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}

}
