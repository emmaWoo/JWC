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
	public static final int TIMEOUT = 30000;
	public static final int RETRY_COUNT = 2;

	public static class DomainName {
		static final String PRD = "app.joinworker.com.tw:8080";
		static final String DEV = "beta.joinworker.com.tw:8080";
	}

	private static String mDomainName = DomainName.DEV;
	private static String mApiProtocol = Protocol.HTTP;

	private static String USER_TOKEN;

	private ApiErrorListener mApiErrorListener;
	private ApiListener<T> mApiListener;

	protected abstract T parseResult(String result) throws Exception;

	protected void onApiSuccess(T parsedResult, ApiListener<T> listener) {
		listener.onSuccess(parsedResult);
	}

	protected int onApiFail(int errorType, String result) {
		return errorType;
	}

	public static void initVars(int environmentType) {
		updateEnvironment(environmentType);
	}

	public static void updateEnvironment(int environmentType) {
		switch (environmentType) {
			case 0:
				mDomainName = DomainName.PRD;
				mApiProtocol = Protocol.HTTP;
				break;
			case 1:
				mDomainName = DomainName.DEV;
				mApiProtocol = Protocol.HTTP;
				break;
		}
	}

	public static String getBaseUrl() {
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
						parsedResult = parseResult(result.replaceAll("null", "\"\""));
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

	public static  String getProtocol() {
		return mApiProtocol;
	}

	public static  String getDomainName() {
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
		if (USER_TOKEN != null) {
			headerMap.put("jwc-token", USER_TOKEN);
		}
		headerMap.put("Content-Type", getContentType());
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

}
