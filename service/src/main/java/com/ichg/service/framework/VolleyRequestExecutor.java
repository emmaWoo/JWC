package com.ichg.service.framework;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ichg.service.utils.Debug;
import com.ichg.service.volley.OkHttpStack;
import com.squareup.okhttp.OkHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class VolleyRequestExecutor implements RequestExecutor {

	private RequestQueue mRequestQueue;

	public VolleyRequestExecutor(Context context) {
		mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
	}

	@Override
	public void request(Api api, Object tag) {
		Request<String> request =
				isRequestByJson(api) ?
						createJsonStringRequest(api) :
						createStringRequest(api);
		request.setRetryPolicy(new DefaultRetryPolicy(api.getTimeout(), api.getRetryCount(), DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setTag(tag);
		mRequestQueue.add(request);
	}

	private Request<String> createJsonStringRequest(final Api api) {
		return new JsonRequest<String>(getVolleyMethodType(api.getHttpMethod()), api.getUrl(), api.getRequestBody(), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Debug.i(api.getClass().getCanonicalName() + " request success.");
				api.onRequestSuccess(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onResponseError(api, error);
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headerMap = new ArrayMap<>();
				api.getHeaders(headerMap);
				return headerMap;
			}

			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				try {
					String jsonString = new String(response.data,
							HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
					return Response.success(jsonString,
							HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new ParseError(e));
				}
			}
		};
	}

	private boolean isRequestByJson(Api api) {
		String requestBody = api.getRequestBody();
		return requestBody != null && !requestBody.isEmpty();
	}

	private Request<String> createStringRequest(final Api api) {
		int method = getVolleyMethodType(api.getHttpMethod());
		String url = method == Request.Method.GET ? getGetUrlString(api) : api.getUrl();
		Debug.i("Connect API url " + url);
		return new StringRequest(method, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Debug.i(api.getClass().getCanonicalName() + " request success.");
				api.onRequestSuccess(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onResponseError(api, error);
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headerMap = new ArrayMap<>();
				api.getHeaders(headerMap);
				return headerMap;
			}

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> paramMap = new ArrayMap<>();
				api.getParameter(paramMap);
				return paramMap;
			}

		};
	}

	private String getGetUrlString(Api api) {
		String url = api.getUrl() + "?";
		Map<String, String> paramsMap = new ArrayMap<>();
		api.getParameter(paramsMap);
		try {
			for (String key : paramsMap.keySet()) {
				url += key + "=" + URLEncoder.encode(paramsMap.get(key), "UTF-8") + "&";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return url;
	}

	private int getVolleyMethodType(int httpMethod) {
		int volleyMethod = Request.Method.GET;
		switch (httpMethod) {
			case HttpMethod.POST:
				volleyMethod = Request.Method.POST;
				break;
			case HttpMethod.PUT:
				volleyMethod = Request.Method.PUT;
				break;
			case HttpMethod.DELETE:
				volleyMethod = Request.Method.DELETE;
				break;
		}
		return volleyMethod;
	}

	private void onResponseError(Api api, VolleyError volleyError) {

		if (volleyError == null) {
			api.onRequestFail(RequestError.SERVER_ERROR, "");
			return;
		}
		int statusCode = 0;
		String responseBody = "";
		if (volleyError instanceof TimeoutError) {
			api.onRequestFail(RequestError.TIMEOUT_ERROR, "");
		} else if (volleyError instanceof NoConnectionError) {
			api.onRequestFail(RequestError.NETWORK_NOT_AVAILABLE, "");
		} else {
			if (volleyError.networkResponse == null) {
				api.onRequestFail(RequestError.SERVER_ERROR, "");
				return;
			}
			statusCode = volleyError.networkResponse.statusCode;
			try {
				responseBody = new String(volleyError.networkResponse.data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			api.onRequestFail(RequestError.SERVER_ERROR, responseBody);
		}
		Debug.e(api.getClass().getSimpleName() + " error with statusCode: " + statusCode + " " + responseBody);
	}

	@Override
	public void cancel(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	@Override
	public void cancel(final Api api) {
		mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return api.getUrl().equals(request.getUrl());
			}
		});
	}
}
