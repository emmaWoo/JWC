package com.ichg.service.api.login;

import com.google.gson.JsonObject;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class VerifyCodeLoginApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String verifyCode;

	public VerifyCodeLoginApi(String phoneNo, String verifyCode) {
		this.phoneNo = phoneNo;
		this.verifyCode = verifyCode;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/register/verification";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("status");
	}

	@Override
	public String getRequestBody() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phoneNo", phoneNo);
		jsonObject.addProperty("verificationCode", verifyCode);
		return jsonObject.toString();
	}
}
