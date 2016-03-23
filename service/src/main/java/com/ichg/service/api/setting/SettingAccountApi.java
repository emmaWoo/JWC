package com.ichg.service.api.setting;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

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
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("password", password);
		parameterMap.put("phoneNo", phoneNo);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/change-account";
	}
}
