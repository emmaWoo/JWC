package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class ForgetPasswordApi extends JoinWorkerApi<String> {

	private String phoneNo;
	private String personalId;

	public ForgetPasswordApi(String phoneNo, String personalId){
		this.phoneNo = phoneNo;
		this.personalId = personalId;
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
		parameterMap.put("phoneNo", phoneNo);
		parameterMap.put("personalId", personalId);
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/account/forget-password\t\n";
	}
}
