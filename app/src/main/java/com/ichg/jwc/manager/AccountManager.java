package com.ichg.jwc.manager;

import android.text.TextUtils;

import com.ichg.jwc.utils.EWPreference;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.LoginApi;
import com.ichg.service.api.login.LoginApiBase;

public class AccountManager {

	private EWPreference preference;
	private ApiFacade apiFacade;
	public boolean isBindFacebook;
	public int userId;
	public int userAvatarImageId;
	public String userAccessToken;
	public String userAccount;
	public String userName;
	public String userAvatarUrl;
	public String facebookId;
	public String facebookToken;
	public boolean isSkipProfile;
	private boolean isLoginByAccount;



	public class LoginNavigationType {
		public static final int REGISTER_ACCOUNT = 0;
		public static final int MAIN_PAGE = 1;
		public static final int PROFILE_SETTING = 2;
	}

	public AccountManager(ApiFacade apiFacade, EWPreference preference) {
		this.preference = preference;
		this.apiFacade = apiFacade;

		userId = preference.getUserId();
		userAccount = preference.getAccount();
		facebookToken = preference.getFacebookAccessToken();
		facebookId = preference.getFacebookUserId();
		isBindFacebook = facebookToken != null && !facebookToken.isEmpty();
		userAccessToken = preference.getUserToken();
		userAvatarUrl = preference.getAvatarUrl();
		userName = preference.getUserName();
		isSkipProfile = preference.isSkipProfile();
	}

	public void facebookLogin(String userId, String userToken, LoginListener listener) {
		//FacebookLoginApi api = new FacebookLoginApi(userId, userToken);
		//startLogin(api, listener);
	}

	public void accountLogin(String phoneNo, String password, LoginListener listener) {
		isLoginByAccount = true;
		LoginApi api = new LoginApi(phoneNo, password);
		startLogin(api, listener);
	}

	public void cancelLogin() {
		apiFacade.cancel(this);
	}

	public void logout() {
		clear();
		saveLoginStatus();
	}

	public int checkLoginNavigation() {
		int navigationType;
		if (isSignIn()) {
			if (isLoginByAccount || isSignInFlowSuccess()) {
				navigationType = LoginNavigationType.MAIN_PAGE;
			} else {
				navigationType = LoginNavigationType.PROFILE_SETTING;
			}
		} else {
			navigationType = LoginNavigationType.REGISTER_ACCOUNT;
		}
		return navigationType;
	}

	public void updateAccount(String account) {
		userAccount = account;
		preference.setAccount(userAccount);
	}

	public void updateToken(String token) {
		userAccessToken = token;
		preference.setUserToken(userAccessToken);
	}

	private void startLogin(LoginApi api, LoginListener listener) {
		api.success((LoginApi.LoginResultInfo response) -> {
			updateLoginStatus(response);
			listener.onLoginSuccess();
		}).fail(listener::onLoginFail).start(this);
	}

	public void updateLoginStatus(LoginApiBase.LoginResultInfo response) {
		userId = response.userId;
		userAccount = response.account;
		userAccessToken = response.accessToken;
		userName = response.userName;
		facebookId = response.facebookId;
		facebookToken = response.facebookToken;
		userAvatarUrl = response.avatarUrl;
		userAvatarImageId = response.avatarImageId;
		saveLoginStatus();
	}

	public void updateUserAvatarInfo(String userAvatarUrl, int userAvatarImageId) {
		this.userAvatarUrl = userAvatarUrl;
		this.userAvatarImageId = userAvatarImageId;
		saveLoginStatus();
	}


	private void clear() {
		userId = 0;
		userAvatarImageId = 0;
		userAccount = null;
		userAvatarUrl = null;
		userName = null;
		isBindFacebook = false;
		userAccessToken = null;
		facebookId = null;
		facebookToken = null;
		isLoginByAccount = false;
		isSkipProfile = false;
	}

	private void saveLoginStatus() {
		apiFacade.updateUserToken(userAccessToken);

		preference.setUserToken(userAccessToken);
		preference.setAccount(userAccount);
		preference.setUserName(userName);
		preference.setUserId(userId);
		preference.setAvatarUrl(userAvatarUrl);
		preference.setFacebookUserId(facebookId);
		preference.setFacebookAccessToken(facebookToken);
		preference.setSkipProfile(isSkipProfile);
	}

	public boolean isSignInFlowSuccess() {
		return (!TextUtils.isEmpty(userName) || isSkipProfile) && isSignIn();
	}

	public boolean isSignIn() {
		return !TextUtils.isEmpty(userAccessToken);
	}


	public interface LoginListener {
		void onLoginSuccess();

		void onLoginFail(int errorType, String message);
	}

}
