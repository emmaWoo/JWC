package com.ichg.service.api.login;

import com.google.gson.JsonObject;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class ForgetPasswordApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String personalId;

	public ForgetPasswordApi(String phoneNo, String personalId){
		this.phoneNo = phoneNo;
		this.personalId = personalId;
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
	public String getRequestBody() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phoneNo", phoneNo);
		jsonObject.addProperty("personalId", personalId);
		return jsonObject.toString();
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/forget-password";
	}
}
