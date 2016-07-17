package com.ichg.service.volley;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MultiPartRequest extends Request<String> {

	private HttpEntity mHttpEntity;

	private Response.Listener<String> mListener;

	public MultiPartRequest(String url, Bitmap bitmap, Response.Listener<String> listener,
	                        Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		mListener = listener;

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
		builder.addPart("upload", new ByteArrayBody(outputStream.toByteArray(), "UTF-8"));
//		for(String key : fields.keySet()){
//			builder.addPart(key, new StringBody(fields.get(key), ContentType.MULTIPART_FORM_DATA));
//		}
		mHttpEntity = builder.build();
	}

	@Override
	public String getBodyContentType() {
		return mHttpEntity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			mHttpEntity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}