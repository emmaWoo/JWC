package com.ichg.service.utils;

import android.util.Log;

public class Debug {
	private static final String TAG = "join worker";

	public static void i(Object msg) {
		Log.i(TAG, "" + msg);
	}

	public static void i(String tag, Object msg) {
		Log.i(tag, "" + msg);
	}

	public static void e(Object msg) {
		Log.e(TAG, "" + msg);
	}

	public static void e(String tag, Object msg) {
		Log.e(tag, "" + msg);
	}
}
