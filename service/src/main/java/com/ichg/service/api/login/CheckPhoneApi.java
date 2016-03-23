package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class CheckPhoneApi extends JoinWorkerApi<String> {

	private String phoneNo;

	public CheckPhoneApi(String phoneNo){
		this.phoneNo = phoneNo;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("status");
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("phoneNo", phoneNo);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/validation/check/phone-status";
	}
}
