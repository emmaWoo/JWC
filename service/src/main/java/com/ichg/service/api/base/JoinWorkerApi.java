package com.ichg.service.api.base;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ichg.service.framework.Api;
import com.ichg.service.framework.Protocol;
import com.ichg.service.framework.RequestError;
import com.ichg.service.utils.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public abstract class JoinWorkerApi<T> implements Api<T> {

	private static ApiFacade mApiFacade;

	public static int LIMIT_COUNT = 25;
	public static final int MAX_COUNT_PER_REQUEST = 25;
	public static final int TIMEOUT = 30000;
	public static final int RETRY_COUNT = 2;
	public static final int API_VERSION = 1;
	//private static final String VERSION_TEXT = "/v%s/";
	private static final String DEVICE_OS = "android";

	public static class DomainName {
		static final String PRD = "";
		static final String DEV = "210.61.165.173:8080/jwc";
		static final String STAGING = "";
	}

	public static final String SECRET_KEY = "";
	public static final String USER_AGENT = "";

	private static String mDomainName = DomainName.DEV;
	private static String mApiProtocol = Protocol.HTTP;

	private static String DEVICE_ID;
	private static String USER_TOKEN;
	//protected static String AUTH_TOKEN;

	private ApiErrorListener mApiErrorListener;
	private ApiListener<T> mApiListener;

	private boolean isLoading;

	protected abstract T parseResult(String result) throws Exception;

	protected void onApiSuccess(T parsedResult, ApiListener<T> listener) {
		listener.onSuccess(parsedResult);
	}

	protected int onApiFail(int errorType, String result) {
		return errorType;
	}

	public static void initVars(String deviceId, int environmentType) {
		DEVICE_ID = deviceId;
		updateEnvironment(environmentType);
	}

	public static void updateEnvironment(int environmentType) {
		switch (environmentType) {
			case 0:
				mDomainName = DomainName.PRD;
				mApiProtocol = Protocol.HTTPS;
				break;
			case 1:
				mDomainName = DomainName.DEV;
				mApiProtocol = Protocol.HTTP;
				break;
			case 2:
				mDomainName = DomainName.STAGING;
				mApiProtocol = Protocol.HTTPS;
				break;
		}
		//mDomainName += String.format(VERSION_TEXT, API_VERSION);
//		String secretKey = environmentType != 0 ?
//				"1:client_team" : SECRET_KEY;
//		AUTH_TOKEN = "Basic " + Base64.encodeToString(secretKey.getBytes(), Base64.NO_WRAP);
	}

	protected String getBaseUrl() {
		return getProtocol() + "://" + getDomainName();
	}

	public static void updateUserToken(String userToken) {
		USER_TOKEN = TextUtils.isEmpty(userToken) ? null : userToken;
	}

	@Override
	public JoinWorkerApi<T> success(ApiListener<T> listener) {
		mApiListener = listener;
		return this;
	}

	@Override
	public JoinWorkerApi<T> fail(ApiErrorListener listener) {
		mApiErrorListener = listener;
		return this;
	}

	@Override
	final public void onRequestSuccess(final String result) {
		Debug.i(getClass().getName() + " Result: " + result);
		isLoading = false;
		try {
			JSONObject jsonObject = new JSONObject(result);
			String message = jsonObject.optString("message");
			if (!TextUtils.isEmpty(message) && !message.equals("null")) {
				onRequestFail(RequestError.SERVER_ERROR, result);
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (mApiListener != null) {
			new AsyncTask<Void, Void, T>() {
				@Override
				protected T doInBackground(Void... params) {
					T parsedResult = null;
					try {
						parsedResult = parseResult(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return parsedResult;
				}

				@Override
				protected void onPostExecute(T parsedResult) {
					if (parsedResult != null) {
						onApiSuccess(parsedResult, mApiListener);
					} else {
						onRequestFail(JoinWorkerApiError.PARSE_API_FAIL, result);
					}
				}
			}.execute();
		}
	}


	@Override
	final public void onRequestFail(int cause, String response) {
		Debug.i("onRequestFail: " + response);
		isLoading = false;
		if (mApiErrorListener == null) {
			return;
		}
		int apiErrorType = JoinWorkerApiError.UNKNOWN_SERVER_ERROR;
		String errorMessages = "";
		switch (cause) {
			case RequestError.NETWORK_NOT_AVAILABLE:
				apiErrorType = JoinWorkerApiError.NETWORK_NOT_AVAILABLE;
				break;
			case RequestError.TIMEOUT_ERROR:
				apiErrorType = JoinWorkerApiError.TIMEOUT_ERROR;
				break;
			case RequestError.SERVER_ERROR:
				try {
					JSONObject jsonObject = new JSONObject(response);
					String message = jsonObject.optString("message");
					if (!TextUtils.isEmpty(message)) {
						errorMessages += message;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					apiErrorType = JoinWorkerApiError.ERROR_MESSAGE_FORMAT_ERROR;
				}
				break;
		}
		mApiErrorListener.onFail(onApiFail(apiErrorType, errorMessages), errorMessages);
	}

	@Override
	public int getTimeout() {
		return TIMEOUT;
	}

	@Override
	public int getRetryCount() {
		return RETRY_COUNT;
	}

	@Override
	public String getProtocol() {
		return mApiProtocol;
	}

	@Override
	public String getDomainName() {
		return mDomainName;
	}

	@Override
	public String getRequestBody() {
		return "";
	}

	@Override
	public String getContentType() {
		if (getBitmap() != null) {
			return "multipart/form-data";
		} else if(TextUtils.isEmpty(getRequestBody())) {
			return "application/x-www-form-urlencoded";
		} else {
			return "application/json";
		}
	}

	@Override
	public void getHeaders(Map<String, String> headerMap) {
//		headerMap.put("Authorization", USER_TOKEN == null ? AUTH_TOKEN : USER_TOKEN);
		if (USER_TOKEN != null) {
			headerMap.put("jwc-token", USER_TOKEN);
		}
		headerMap.put("Content-Type", getContentType());
		//headerMap.put("User-Agent", USER_AGENT);
	}

	@Override
	public void getParameter(Map<String, String> parameterMap) {
		parameterMap.put("", "");
	}

	@Override
	public Bitmap getBitmap() {
		return null;
	}

	protected boolean isDebug() {
		return false;
	}

	public static void initApiFacade(ApiFacade apiFacade) {
		mApiFacade = apiFacade;
	}

	public JoinWorkerApi<T> start(Object user) {
		if (mApiFacade == null) {
			throw new RuntimeException("You have to init ApiFacade before using start method.");
		}
		isLoading = true;
		mApiFacade.request(this, user);
		return this;
	}

	public JoinWorkerApi<T> start() {
		return start(this);
	}

	public void cancel() {
		mApiFacade.cancel(this);
	}

	public void cancel(Object user) {
		mApiFacade.cancel(user);
	}

	public boolean isLoading(){
		return isLoading;
	}

}
