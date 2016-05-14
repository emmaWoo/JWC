package com.ichg.jwc.presenter;

import android.graphics.Bitmap;

import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.listener.ProfileListener;
import com.ichg.jwc.listener.ProfileViewListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.avatar.UploadAvatarAPI;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.setting.GetUserApi;
import com.ichg.service.api.setting.UpdateUserApi;
import com.ichg.service.framework.Api;
import com.ichg.service.object.UserInfo;

public class ProfileViewPresenter {

	private ApiFacade mApiFacade;
	private AccountManager mAccountManager;
	private ProfileViewListener profileViewListener;

	public ProfileViewPresenter(ApiFacade apiFacade, AccountManager accountManager, ProfileViewListener profileViewListener) {
		mApiFacade = apiFacade;
		mAccountManager = accountManager;
		this.profileViewListener = profileViewListener;
	}

	public void getProfile() {
		mApiFacade.request(new GetUserApi()
				.success(userInfo -> profileViewListener.onSuccess(userInfo))
				.fail(profileViewListener::onFail), this);
	}

	public void cancel() {
		mAccountManager.cancelLogin();
	}

}
