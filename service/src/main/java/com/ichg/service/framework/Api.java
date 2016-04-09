package com.ichg.service.framework;

import android.graphics.Bitmap;

import java.util.Map;

public interface Api<T> {

	String getProtocol();

	int getHttpMethod();

	String getDomainName();

	String getUrl();

	String getRequestBody();

	Bitmap getBitmap();

	String getContentType();

	void getHeaders(Map<String, String> headerMap);

	void getParameter(Map<String, String> parameterMap);

	Api success(ApiListener<T> listener);

	Api fail(ApiErrorListener listener);

	void onRequestSuccess(String result);

	void onRequestFail(int cause, String errorMessage);

	int getTimeout();

	int getRetryCount();

	interface ApiListener<T>{
		void onSuccess(T response);
	}

	interface ApiErrorListener {
		void onFail(int errorType, String message);
	}

}
