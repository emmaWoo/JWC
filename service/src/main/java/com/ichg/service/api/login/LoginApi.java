package com.ichg.service.api.login;

import java.util.Map;

public class LoginApi extends LoginApiBase {

	private String phoneNo;
	private String password;

	public LoginApi(String phoneNo, String password){
		this.phoneNo = phoneNo;
		this.password = password;
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("phoneNo", phoneNo);
		parameterMap.put("password", password);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/auth/login";
	}
}
