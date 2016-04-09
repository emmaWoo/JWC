package com.ichg.service.api.setting;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.UserProfileBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.UserInfo;

import org.json.JSONObject;

public class GetUserApi extends JoinWorkerApi<UserProfileBaseEntity> {

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	protected UserProfileBaseEntity parseResult(String result) throws Exception {
		JSONObject dataObject = new JSONObject(result).optJSONObject("data");
		return new Gson().fromJson(dataObject.toString(), UserProfileBaseEntity.class);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/account-info";
	}
}
