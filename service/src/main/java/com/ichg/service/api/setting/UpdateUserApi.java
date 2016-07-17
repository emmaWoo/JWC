package com.ichg.service.api.setting;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.UserProfileBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.UserInfo;

import org.json.JSONObject;

public class UpdateUserApi extends JoinWorkerApi<String> {

	private UserProfileBaseEntity userProfileBaseEntity;

	public UpdateUserApi(UserInfo userInfo){
		userProfileBaseEntity = new UserProfileBaseEntity(userInfo);
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
		return new Gson().toJson(userProfileBaseEntity);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/account-info";
	}
}
