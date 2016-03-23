package com.ichg.service.api.login;

import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.framework.HttpMethod;
import com.ichg.service.utils.Debug;

import org.json.JSONObject;

import java.util.Map;

public abstract class LoginApiBase extends JoinWorkerApi<LoginApiBase.LoginResultInfo> {

	private static final String PROVIDER_CELLPHONE = "cellphone";
	private static final String PROVIDER_FACEBOOK = "facebook";

	protected static final String PARA_USERNAME = "username";
	protected static final String PARA_PASSWORD = "password";

	public static class ErrorCode {
		public static final int USER_NOT_FOUND = -11001;
		public static final int DATABASE_ERROR = -11002;
		public static final int PERMISSION_DENY = -11003;
	}

	@Override
	protected LoginResultInfo parseResult(String result) throws Exception {
		JSONObject jsonContent = new JSONObject(result);
//		JSONObject profileJsonObject = jsonContent.optJSONObject("profile");
		LoginResultInfo resultInfo = new LoginResultInfo();
		resultInfo.accessToken = jsonContent.optString("token");
//		resultInfo.userId = profileJsonObject.optInt("user_id");
//		resultInfo.username = profileJsonObject.optString("username");
//		resultInfo.nickname = profileJsonObject.optString("nickname");
//		resultInfo.avatarUrl = profileJsonObject.optString("image");
//		resultInfo.avatarImageId = profileJsonObject.optInt("image_id");
//		resultInfo.isFinishInterest = profileJsonObject.optBoolean("is_finish_interest");
//
//		JSONArray oauthData = new JSONArray(jsonContent.getString("oauth_list"));
//		for (int i = 0, size = oauthData.length(); i < size; i++) {
//			ProviderInfo providerInfo = new ProviderInfo(oauthData.optJSONObject(i));
//			if(PROVIDER_CELLPHONE.equals(providerInfo.name)){
				resultInfo.isBindCellphone = true;
//				resultInfo.cellphone = providerInfo.userId;
//			}else if(PROVIDER_FACEBOOK.equals(providerInfo.name)){
//				resultInfo.isBindFacebook = true;
//				resultInfo.facebookId = providerInfo.userId;
//				resultInfo.facebookToken = providerInfo.accessToken;
//			}
//		}

		return resultInfo;
	}

	@Override
	protected int onApiFail(int errorType, String result) {
		Debug.e("onApiFail: " + errorType + "  " + (this instanceof LoginApi));
		if ("USER_NOT_FOUND".equals(result)) {
			return ErrorCode.USER_NOT_FOUND;
		} else if ("PERMISSION_DENY".equals(result)) {
			return ErrorCode.PERMISSION_DENY;
		} else if ("DATABASE_ERROR".equals(result)) {
			return ErrorCode.DATABASE_ERROR;
		}
		return errorType;
	}

	@Override
	public void getHeaders(Map<String, String> headerMap) {
		super.getHeaders(headerMap);
		headerMap.put("Content-type", "application/x-www-form-urlencoded");
	}

	@Override
	public int getHttpMethod() {
		return HttpMethod.POST;
	}

	public static class LoginResultInfo{
		public boolean isBindCellphone;
		public boolean isBindFacebook;
		public boolean isFinishInterest;
		public int userId;
		public String username;
		public String nickname;
		public String avatarUrl;
		public String accessToken;

		public String cellphone;
		public String facebookId;
		public String facebookToken;
		public int avatarImageId;
	}

}
