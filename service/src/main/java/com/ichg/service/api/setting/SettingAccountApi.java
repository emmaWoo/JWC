package com.ichg.service.api.setting;

import com.google.gson.JsonObject;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class SettingAccountApi extends JoinWorkerApi<String> {

	private String password;
	private String phoneNo;

	public SettingAccountApi(String phoneNo, String password){
		this.password = password;
		this.phoneNo = phoneNo;
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
		jsonObject.addProperty("password", password);
		return jsonObject.toString();
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/change-account";
	}
}
