package com.ichg.jwc.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ichg.jwc.JoinWorkerApp;
import com.ichg.service.api.avatar.UploadAvatarAPI;
import com.ichg.service.api.setting.UpdateUserApi;
import com.ichg.service.object.UserInfo;
import com.ichg.service.utils.Debug;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

public class FacebookUtils {

	private static int facebookProfileUpdateFlag;

	public static CallbackManager callbackManager = CallbackManager.Factory.create();

	public void login(Fragment fragment, final FacebookLoginListener listener) {
		LoginManager.getInstance().registerCallback(callbackManager, new
				FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(final LoginResult loginResult) {
						FacebookLoginInfo loginInfo = new FacebookLoginInfo();
						loginInfo.userId = loginResult.getAccessToken().getUserId();
						loginInfo.accessToken = loginResult.getAccessToken().getToken();
						listener.onLoginSuccess(loginInfo);
					}

					@Override
					public void onCancel() {
						listener.onCancel();
					}

					@Override
					public void onError(FacebookException e) {
						listener.onError(e.getLocalizedMessage());
					}
				});
		LoginManager.getInstance().logInWithReadPermissions(fragment, Collections.singletonList("public_profile"));
	}

	public void login(Activity activity, final FacebookLoginListener listener){
		LoginManager.getInstance().registerCallback(callbackManager, new
				FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(final LoginResult loginResult) {
						FacebookLoginInfo loginInfo = new FacebookLoginInfo();
						loginInfo.userId = loginResult.getAccessToken().getUserId();
						loginInfo.accessToken = loginResult.getAccessToken().getToken();
						listener.onLoginSuccess(loginInfo);
					}

					@Override
					public void onCancel() {
						listener.onCancel();
					}

					@Override
					public void onError(FacebookException e) {
						listener.onError(e.getLocalizedMessage());
					}
				});
		LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
	}

	public void logout() {
		LoginManager.getInstance().logOut();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public static void downloadFacebookProfilePicture(final String userID, final FacebookProfilePictureListener listener) {
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... voids) {
				Bitmap bitmap = null;
				try {
					URL imageURL = new URL(generateFacebookUserPicUrl(userID, true));
					bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				super.onPostExecute(bitmap);
				if (bitmap == null) {
					listener.onDownloadFail();
				}
				listener.onDownloadFinished(bitmap);
			}
		}.execute();
	}

	public static String generateFacebookUserPicUrl(String userId, boolean isLarge) {
		return "https://graph.facebook.com/" + userId + "/picture" + ((isLarge) ? "?type=large" : "");
	}

	public static void requestFacebookUserProfile(String userId, final FacebookUserProfileListener listener) {
		new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + userId, null, HttpMethod.GET, new GraphRequest.Callback() {
			@Override
			public void onCompleted(GraphResponse graphResponse) {
				Debug.e("fb", "requestFacebookUserProfile: " + graphResponse.toString());
				JSONObject jsonObject = graphResponse.getJSONObject();
				String nickName = "Facebook";
				if (jsonObject != null) {
					nickName = jsonObject.optString("name");
				}
				listener.onSuccess(nickName);
			}
		}).executeAsync();
	}

	public static void updateAccountProfile(final UpdateProfileListener listener) {
		facebookProfileUpdateFlag = 0;
		updateFacebookPictureToServer(listener);
		updateFacebookProfileToServer(listener);
	}

	private static void updateFacebookProfileToServer(final UpdateProfileListener listener) {
		GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
			@Override
			public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
				if (jsonObject == null) {
					checkFacebookProfileUpdateFinish(listener);
				} else {
					Debug.e("updateFacebookProfileToServer: " + jsonObject.toString());
					final String nickName = jsonObject.optString("name");
					UserInfo userInfo = new UserInfo();
					userInfo.userName = nickName;
					JoinWorkerApp.apiFacade.request(new UpdateUserApi(userInfo)
							.success(response -> checkFacebookProfileUpdateFinish(listener))
							.fail((errorType, message) -> checkFacebookProfileUpdateFinish(listener)), this);
				}
			}

		}).executeAsync();
	}

	private static void updateFacebookPictureToServer(final UpdateProfileListener listener) {
		FacebookUtils.downloadFacebookProfilePicture(AccessToken.getCurrentAccessToken().getUserId(), new
				FacebookUtils.FacebookProfilePictureListener() {
					@Override
					public void onDownloadFinished(Bitmap userPicture) {
						JoinWorkerApp.apiFacade.request(new UploadAvatarAPI(userPicture)
								.success(response -> checkFacebookProfileUpdateFinish(listener))
								.fail((errorType, message) -> checkFacebookProfileUpdateFinish(listener)), this);
					}

					@Override
					public void onDownloadFail() {
						checkFacebookProfileUpdateFinish(listener);
					}
				});
	}

	private static void checkFacebookProfileUpdateFinish(UpdateProfileListener listener) {
		facebookProfileUpdateFlag++;
		if (facebookProfileUpdateFlag == 2) {
			listener.onUpdateProfileFinish();
		}
	}

	public static String getUserId() {
		return AccessToken.getCurrentAccessToken().getUserId();
	}

	public static String getAccessToken() {
		return AccessToken.getCurrentAccessToken().getToken();
	}

	public interface FacebookUserProfileListener {
		void onSuccess(String userName);
	}

	public interface UpdateProfileListener {
		void onUpdateProfileFinish();
	}

	public interface FacebookProfilePictureListener {
		void onDownloadFinished(Bitmap userPicture);

		void onDownloadFail();
	}

	public interface FacebookLoginListener {
		void onLoginSuccess(FacebookLoginInfo loginInfo);

		void onCancel();

		void onError(String message);
	}

	public static class FacebookLoginInfo {
		public String userId;
		public String accessToken;
	}
}
