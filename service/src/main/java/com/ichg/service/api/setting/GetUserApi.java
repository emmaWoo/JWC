package com.ichg.service.api.setting;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.UserProfileBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.UserInfo;

import org.json.JSONObject;

public class GetUserApi extends JoinWorkerApi<UserInfo> {

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	protected UserInfo parseResult(String result) throws Exception {
		JSONObject dataObject = new JSONObject(result).optJSONObject("data");
		UserProfileBaseEntity userProfileBaseEntity = new Gson().fromJson(dataObject.toString(), UserProfileBaseEntity.class);
		return userProfileBaseEntity.getUserInfo();
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/account-info";
	}
}
