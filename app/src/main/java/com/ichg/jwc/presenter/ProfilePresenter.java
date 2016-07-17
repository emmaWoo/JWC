package com.ichg.jwc.presenter;

import android.graphics.Bitmap;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.listener.ProfileListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.avatar.UploadAvatarAPI;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.setting.UpdateUserApi;
import com.ichg.service.object.UserInfo;

public class ProfilePresenter {

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private ProfileListener profileListener;

	public ProfilePresenter(ApiFacade apiFacade, AccountManager accountManager, ProfileListener profileListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.profileListener = profileListener;
	}

	public void saveProfile(UserInfo userInfo) {
		mApiFacade.request(new UpdateUserApi(userInfo)
				.success(response -> onSuccess(userInfo))
				.fail(profileListener::onFail), this);
	}

	private void onSuccess(UserInfo userInfo) {
		mAccountManager.updateUserName(userInfo.userName);
		profileListener.onSuccess();
	}

	public void uploadPhoto(Bitmap bitmap) {
		mApiFacade.request(new UploadAvatarAPI(bitmap)
				.success(response -> uploadPhotoSuccess(bitmap, response))
				.fail(profileListener::onFail), this);
	}

	public void uploadPhotoSuccess(Bitmap bitmap, String url) {
		JoinWorkerApp.preference.setAvatarUrl(url);
		profileListener.onAvatarUpdateSuccess(bitmap);
	}

	public int checkPageNavigation(){
		return mAccountManager.checkLoginNavigation();
	}

	public void skipProfile() {
		mAccountManager.skipProfile();
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}

}
