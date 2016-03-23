package com.ichg.service.object;

import org.json.JSONObject;

public class ProviderInfo {
	public String name = "";
	public String userId = "";
	public String accessToken = "";

	public ProviderInfo() {
	}

	public ProviderInfo(JSONObject jsonObject) {
		if (jsonObject != null) {
			name = jsonObject.optString("provider_name");
			userId = jsonObject.optString("provider_user_id");
			accessToken = jsonObject.optString("provider_access_token");
		}
	}
}
