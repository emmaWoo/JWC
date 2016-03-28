package com.ichg.service.api.setting;

import com.google.gson.JsonObject;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class SettingPasswordApi extends JoinWorkerApi<String> {

	private String password;
	private String newPassword;

	public SettingPasswordApi(String password, String newPassword){
		this.password = password;
		this.newPassword = newPassword;
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
		jsonObject.addProperty("password", password);
		jsonObject.addProperty("newPassword", newPassword);
		return jsonObject.toString();
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/change-password";
	}
}
