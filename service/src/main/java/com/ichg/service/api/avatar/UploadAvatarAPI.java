package com.ichg.service.api.avatar;

import android.graphics.Bitmap;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;

import org.json.JSONObject;

public class UploadAvatarAPI extends JoinWorkerApi<String> {

	private Bitmap bitmap;

	public UploadAvatarAPI(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	@Override
	protected String parseResult(String result) throws Exception {
		return new JSONObject(result).optString("avatarUrl");
	}

	@Override
	public Bitmap getBitmap() {
		return bitmap;
	}

	@Override
	public String getUrl() {
		return getBaseUrl() + "/jwc/account/avatar";
	}
}