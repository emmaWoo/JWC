package com.ichg.joinworker.manager;

import android.text.TextUtils;

import com.ichg.joinworker.utils.EWPreference;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.login.LoginApi;
import com.ichg.service.api.login.LoginApiBase;

public class AccountManager {

	private EWPreference preference;
	private ApiFacade apiFacade;
	public boolean isBindCellPhone;
	public boolean isBindFacebook;
	public int userId;
	public int userAvatarImageId;
	public String userAccessToken;
	public String userAccount;
	public String userNickname;
	public String userAvatarUrl;
	public String facebookId;
	public String facebookToken;
	public String cellphone;
	private boolean isLoginByAccount;
	private boolean isFirstBindCellphone;



	public class LoginNavigationType {
		public static final int UNREGISTER = 0;
		public static final int REGISTER_CELLPHONE = 1;
		public static final int REGISTER_ACCOUNT = 2;
		public static final int SETUP_INTERESTING = 3;
		public static final int MAIN_PAGE = 4;
		public static final int PROFILE_SETTING = 5;
	}

	public AccountManager(ApiFacade apiFacade, EWPreference preference) {
		this.preference = preference;
		this.apiFacade = apiFacade;

		userId = preference.getUserId();
		userNickname = preference.getNickname();
		userAccount = preference.getUserName();
		isBindCellPhone = preference.isBindingCellphone();
		facebookToken = preference.getFacebookAccessToken();
		facebookId = preference.getFacebookUserId();
		isBindFacebook = facebookToken != null && !facebookToken.isEmpty();
		userAccessToken = preference.getUserToken();
		cellphone = preference.getCellPhone();
		userAvatarUrl = preference.getAvatarUrl();
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

	public void registerLogin(String phoneNo, String verifyCode, LoginListener listener) {
		//RegisterLoginApi api = new RegisterLoginApi(phoneNo, verifyCode, countryCode);
		//startLogin(api, listener);
	}

	public void cancelLogin() {
		apiFacade.cancel(this);
	}

	public void logout() {
		clear();
		saveLoginStatus();
	}

	public int checkLoginNavigation() {
		if (userAccessToken == null || userAccessToken.isEmpty()) {
			return LoginNavigationType.UNREGISTER;
		}
		int navigationType;
		if (isBindCellPhone) {
			if (!isLoginByAccount && isFirstBindCellphone) {
				isFirstBindCellphone = false;
				navigationType = LoginNavigationType.REGISTER_ACCOUNT;
			} else {
				navigationType = LoginNavigationType.MAIN_PAGE;
			}
		} else {
			navigationType = LoginNavigationType.REGISTER_CELLPHONE;
		}
		return navigationType;
	}

	public void updateUserName(String userName) {
		userAccount = userName;
		preference.setUserName(userAccount);
	}

	public void updateNickname(String nickname) {
		userNickname = nickname;
		preference.setNickname(userNickname);
	}

	public void updateCellphone(String phoneNo) {
		cellphone = phoneNo;
		isBindCellPhone = true;
		isFirstBindCellphone = true;
		preference.setCellPhone(cellphone);
		preference.setBindingCellphone(isBindCellPhone);
		preference.setIsSignInSuccess(isBindCellPhone);
	}


	private void startLogin(LoginApi api, LoginListener listener) {
		api.success((LoginApi.LoginResultInfo response) -> {
			updateLoginStatus(response);
			listener.onLoginSuccess();
		}).fail(listener::onLoginFail).start(this);
	}

	public void updateLoginStatus(LoginApiBase.LoginResultInfo response) {
		userId = response.userId;
		userNickname = response.nickname;
		userAccount = response.username;
		isBindCellPhone = response.isBindCellphone;
		isBindFacebook = response.isBindFacebook;
		userAccessToken = response.accessToken;
		facebookId = response.facebookId;
		facebookToken = response.facebookToken;
		cellphone = response.cellphone;
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
		userNickname = null;
		userAccount = null;
		userAvatarUrl = null;
		isBindCellPhone = false;
		isBindFacebook = false;
		userAccessToken = null;
		facebookId = null;
		facebookToken = null;
		cellphone = null;
		isFirstBindCellphone = false;
		isLoginByAccount = false;
	}

	private void saveLoginStatus() {
		apiFacade.updateUserToken(userAccessToken);

		preference.setUserToken(userAccessToken);
		preference.setUserName(userAccount);
		preference.setNickname(userNickname);
		preference.setUserId(userId);
		preference.setAvatarUrl(userAvatarUrl);
		preference.setFacebookUserId(facebookId);
		preference.setFacebookAccessToken(facebookToken);
		preference.setBindingCellphone(isBindCellPhone);
		preference.setCellPhone(cellphone);

		preference.setIsSignInSuccess(isBindCellPhone);
	}

	public boolean isSignInFlowSuccess() {
		return isBindCellPhone && isSignIn();
	}

	public boolean isSignIn() {
		return !TextUtils.isEmpty(userAccessToken);
	}


	public interface LoginListener {
		void onLoginSuccess();

		void onLoginFail(int errorType, String message);
	}

}
