package com.ichg.jwc.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class EWPreference {

	private SharedPreferences loginPrefs;
	private SharedPreferences systemPrefs;

	public EWPreference(Context context) {
		loginPrefs = context.getSharedPreferences("pref_login", Context.MODE_PRIVATE);
		systemPrefs = context.getSharedPreferences("pref_system", Context.MODE_PRIVATE);
	}

	public void setIsSignInSuccess(boolean isSignInSuccess) {
		loginPrefs.edit().putBoolean("is_sign_in_success", isSignInSuccess).apply();
	}

	public String getUserToken() {
		return loginPrefs.getString("access_token", "");
	}

	public void setUserToken(String accessToken) {
		loginPrefs.edit().putString("access_token", accessToken).apply();
	}

	public String getCellPhone() {
		return loginPrefs.getString("cell_phone", "");
	}

	public void setCellPhone(String cellPhone) {
		loginPrefs.edit().putString("cell_phone", cellPhone).apply();
	}

	public int getUserId() {
		return loginPrefs.getInt("user_id", 0);
	}

	public void setUserId(int userId) {
		loginPrefs.edit().putInt("user_id", userId).apply();
	}

	public String getAccount() {
		return loginPrefs.getString("account", "");
	}

	public void setAccount(String account) {
		loginPrefs.edit().putString("account", account).apply();
	}

	public String getUserName() {
		return loginPrefs.getString("user_name", "");
	}

	public void setUserName(String userName) {
		loginPrefs.edit().putString("user_name", userName).apply();
	}

	public String getAvatarUrl() {
		return loginPrefs.getString("avatar_url", "http://");
	}

	public void setAvatarUrl(String avatarUrl) {
		loginPrefs.edit().putString("avatar_url", avatarUrl).apply();
	}

	public void setFacebookUserId(String userId) {
		loginPrefs.edit().putString("fb_id", userId).apply();
	}

	public String getFacebookUserId() {
		return loginPrefs.getString("fb_id", "");
	}

	public void setFacebookAccessToken(String accessToken) {
		loginPrefs.edit().putString("fb_access_token", accessToken).apply();
	}

	public String getFacebookAccessToken() {
		return loginPrefs.getString("fb_access_token", "");
	}

	public boolean isBindingCellphone() {
		return loginPrefs.getBoolean("is_binding_cellphone", false);
	}

	public void setBindingCellphone(boolean isBindingCellphone) {
		loginPrefs.edit().putBoolean("is_binding_cellphone", isBindingCellphone).apply();
	}

	public String getRegistrationID() {
		return loginPrefs.getString("key_gcm_registration_id", "");
	}

	public void setRegistrationID(String id) {
		loginPrefs.edit().putString("key_gcm_registration_id", id).apply();
	}

	public String getVersionNow() {
		return systemPrefs.getString("version_now", "");
	}

	public void setVersionNow(String versionNow) {
		systemPrefs.edit().putString("version_now", versionNow).apply();
	}

}
