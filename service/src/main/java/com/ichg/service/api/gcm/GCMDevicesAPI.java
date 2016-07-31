package com.ichg.service.api.gcm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.utils.JoinWorkerId;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMDevicesAPI extends JoinWorkerApi<String> {

	private String token;
	private Context context;

	public GCMDevicesAPI(Context context, String token) {
		this.context = context;
		this.token = token;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/account-status";
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
			jsonContent.put("appname", context.getPackageName());
			jsonContent.put("os_type", "android");
			jsonContent.put("appver", getVersionName(context));
			jsonContent.put("brand", Build.BRAND);
			jsonContent.put("model", Build.MODEL);
			jsonContent.put("device_name", JoinWorkerId.isTabletDevice(context) ? "Pad" : "Phone");
			jsonContent.put("os_ver", Build.VERSION.RELEASE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonContent.toString();
	}


	public static String getVersionName(Context context) {
		String verName = "0.0.0";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			verName = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}
}
