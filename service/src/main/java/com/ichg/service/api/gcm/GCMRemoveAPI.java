package com.ichg.service.api.gcm;

import android.content.Context;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.utils.JoinWorkerId;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMRemoveAPI extends JoinWorkerApi<String> {

	private String token;
	private Context context;

	public GCMRemoveAPI(Context context, String token) {
		this.context = context;
		this.token = token;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/logout";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		JSONObject jsonContent = new JSONObject(result);
		return jsonContent.optString("status");
	}

	@Override
	public String getRequestBody() {
		JSONObject jsonContent = new JSONObject();
		try {
			jsonContent.put("hwid", JoinWorkerId.getJoinWorkerId(context));
			jsonContent.put("apptoken", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonContent.toString();
	}

}
