package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class VerifyPhoneLoginApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String verifyCode;

	public VerifyPhoneLoginApi(String phoneNo, String verifyCode) {
		this.phoneNo = phoneNo;
		this.verifyCode = verifyCode;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/validation/check/verification-code";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("status");
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("phoneNo", phoneNo);
		parameterMap.put("verificationCode", verifyCode);
	}

	@Override
	public void getHeaders(Map<String, String> headerMap) {
		super.getHeaders(headerMap);
		headerMap.put("Authorization", AUTH_TOKEN);
	}
}
