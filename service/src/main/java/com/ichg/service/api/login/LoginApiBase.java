package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public abstract class LoginApiBase extends JoinWorkerApi<LoginApiBase.LoginResultInfo> {

	@Override
	protected LoginResultInfo parseResult(String result) throws Exception {
		JSONObject jsonContent = new JSONObject(result);
		LoginResultInfo resultInfo = new LoginResultInfo();
		resultInfo.accessToken = jsonContent.optString("token");
		resultInfo.userName = jsonContent.optString("name");
		resultInfo.avatarUrl = jsonContent.optString("avatarUrl");
		return resultInfo;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	public static class LoginResultInfo {
		public int userId;
		public String account;
		public String userName;
		public String avatarUrl;
		public String accessToken;

		public String facebookId;
		public String facebookToken;
		public int avatarImageId;
	}

}
