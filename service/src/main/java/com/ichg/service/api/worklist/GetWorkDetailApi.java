package com.ichg.service.api.worklist;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.WorkDetailBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.WorkDetailInfo;

import org.json.JSONObject;

import java.util.Map;

public class GetWorkDetailApi extends JoinWorkerApi<WorkDetailInfo> {

	private int id;

	public GetWorkDetailApi(int id) {
		this.id = id;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/job/"+ id +"/detail";
	}

	@Override
	protected WorkDetailInfo parseResult(String result) throws Exception {
		JSONObject jsonObject = new JSONObject(result).optJSONObject("data");
		WorkDetailBaseEntity workDetailBaseEntity = new Gson().fromJson(jsonObject.toString(), WorkDetailBaseEntity.class);
		return workDetailBaseEntity.getWorkDetailInfo();
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		parameterMap.put("id", String.valueOf(id));
		super.getParameter(parameterMap);
	}

}
