package com.ichg.service.api;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class CheckVersionApi extends JoinWorkerApi<String> {

	private String version;
	private CheckAppVersionListener listener;

	public CheckVersionApi(String version, CheckAppVersionListener listener) {
		this.version = version;
		this.listener = listener;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/system/update";
	}

	@Override
	protected String parseResult(String result) throws Exception {
		JSONObject jsonContent = new JSONObject(result);
		String minVer = jsonContent.optString("optionalVersion");
		String nowVer = jsonContent.optString("requiredVersion");
		if (checkVersion(version, minVer)) {
			listener.onForcedUpdate(minVer);
		} else if (checkVersion(version, nowVer)) {
			listener.onVersionUpdate(nowVer);
		}
		return jsonContent.optString("status");
	}

	private boolean checkVersion(String versionName, String version) {
		String minVerName = version.substring(0, version.lastIndexOf("."));
		int minVerCode = Integer.valueOf(version.substring(version.lastIndexOf(".") + 1));
		String nowVerName = versionName.substring(0, versionName.lastIndexOf("."));
		int nowVerCode = Integer.valueOf(versionName.substring(versionName.lastIndexOf(".") + 1));
		if (Float.valueOf(nowVerName) < Float.valueOf(minVerName)) {
			return true;
		} else if (Float.valueOf(nowVerName).equals(Float.valueOf(minVerName))) {
			if (nowVerCode < minVerCode) {
				return true;
			}
		}
		return false;
	}

	public interface CheckAppVersionListener {
		void onForcedUpdate(String version);

		void onVersionUpdate(String version);
	}
}
