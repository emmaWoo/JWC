package com.ichg.service.api.gcm;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class GCMDevicesAPI extends JoinWorkerApi<String> {

	private String token;

	public GCMDevicesAPI(String token) {
		this.token = token;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/chkAccStatus";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		JSONObject jsonContent = new JSONObject(result);
		return jsonContent.optString("status");
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		super.getParameter(parameterMap);
		parameterMap.put("os", "android");
		parameterMap.put("token", token);
	}

}
