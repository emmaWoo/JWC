package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class SignInApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String password;

	public SignInApi(String phoneNo, String password){
		this.phoneNo = phoneNo;
		this.password = password;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("status");
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("phoneNo", phoneNo);
		parameterMap.put("password", password);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/sign-up";
	}
}
