package com.ichg.service.api.worklist;

import com.google.gson.Gson;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.entity.SearchWorkListBaseEntity;
import com.ichg.service.entity.WorkListBaseEntity;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.object.BaseSearchInfo;
import com.ichg.service.object.SearchInfo;
import com.ichg.service.object.WorkListInfo;
import com.ichg.service.utils.Debug;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class GetWorkListApi extends JoinWorkerApi<ArrayList<WorkListInfo>> {

	private int id;
	private SearchWorkListBaseEntity searchWorkListBaseEntity;

	public GetWorkListApi(int id, BaseSearchInfo baseSearchInfo) {
		this.id = id;
		searchWorkListBaseEntity = new SearchWorkListBaseEntity(baseSearchInfo);
	}

	public GetWorkListApi(int id, SearchInfo searchInfo) {
		this.id = id;
		searchWorkListBaseEntity = new SearchWorkListBaseEntity(searchInfo);
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
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
	public String getRequestBody() {
		Debug.i(new Gson().toJson(searchWorkListBaseEntity));
		return new Gson().toJson(searchWorkListBaseEntity);
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		parameterMap.put("id", String.valueOf(id));
		parameterMap.put("rows", String.valueOf(JoinWorkerApi.LIMIT_COUNT));
		super.getParameter(parameterMap);
	}

}
