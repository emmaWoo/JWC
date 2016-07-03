package com.ichg.service.api.worklist;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.WorkListBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.WorkListInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class GetWorkListApi extends JoinWorkerApi<ArrayList<WorkListInfo>> {

	private int id;
	private int time;
	private String city;

	public GetWorkListApi(int id, int time, String city) {
		this.id = id;
		this.time = time;
		this.city = city;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/job/publish";
	}

	@Override
	protected ArrayList<WorkListInfo> parseResult(String result) throws Exception {
		ArrayList<WorkListInfo> workListInfoList = new ArrayList<>();
		JSONArray jsonArray = new JSONObject(result).optJSONArray("data");
		for (int i = 0 ; i < jsonArray.length() ; i++) {
			JSONObject jsonObject = jsonArray.optJSONObject(i);
			WorkListBaseEntity workListBaseEntity = new Gson().fromJson(jsonObject.toString(), WorkListBaseEntity.class);
			workListInfoList.add(workListBaseEntity.getWorkListInfo());
		}
		return workListInfoList;
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		parameterMap.put("id", String.valueOf(id));
		parameterMap.put("timeOption", String.valueOf(time));
		parameterMap.put("cityOption", city);
		parameterMap.put("rows", String.valueOf(JoinWorkerApi.LIMIT_COUNT));
		super.getParameter(parameterMap);
	}

}
