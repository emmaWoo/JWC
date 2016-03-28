package com.ichg.service.api.login;

import com.google.gson.JsonObject;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class ActivateAccountApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String password;

	public ActivateAccountApi(String phoneNo, String password){
		this.phoneNo = phoneNo;
		this.password = password;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("token");
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
		return getBaseUrl() + "/register/activate";
	}
}
