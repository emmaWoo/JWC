package com.ichg.jwc.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

	public static String getNowTimeString() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.TAIWAN).format(System.currentTimeMillis());
	}

}
