package com.ichg.service.api.worklist;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.WorkTypeInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetWorkTypeListApi extends JoinWorkerApi<ArrayList<WorkTypeInfo>> {

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/job/type";
	}

	@Override
	protected ArrayList<WorkTypeInfo> parseResult(String result) throws Exception {
		ArrayList<WorkTypeInfo> workTypeInfoList = new ArrayList<>();
		JSONArray jsonArray = new JSONObject(result).optJSONArray("data");
		for (int i = 0 ; i < jsonArray.length() ; i++) {
			JSONObject jsonObject = jsonArray.optJSONObject(i);
			WorkTypeInfo workTypeInfo = new Gson().fromJson(jsonObject.toString(), WorkTypeInfo.class);
			workTypeInfoList.add(workTypeInfo);
		}
		return workTypeInfoList;
	}
}
