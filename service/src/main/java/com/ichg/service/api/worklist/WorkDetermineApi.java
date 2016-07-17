package com.ichg.service.api.worklist;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

import java.util.Map;

public class WorkDetermineApi extends JoinWorkerApi<String> {

	private int id;

	public WorkDetermineApi(int id) {
		this.id = id;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.PUT;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/job/"+ id +"/accept";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("status");
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		parameterMap.put("id", String.valueOf(id));
		super.getParameter(parameterMap);
	}

}
