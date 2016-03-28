package com.ichg.service.api.login;

import com.google.gson.JsonObject;

public class LoginApi extends LoginApiBase {

	private String phoneNo;
	private String password;

	public LoginApi(String phoneNo, String password) {
		this.phoneNo = phoneNo;
		this.password = password;
	}

	@Override
	public String getRequestBody() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phoneNo", phoneNo);
		jsonObject.addProperty("password", password);
		return jsonObject.toString();
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/auth/login";
	}
}
