package com.ichg.jwc.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

	public static int getDifferenceDay(long timeMillis) {
		long curTime = System.currentTimeMillis();
		long time = (timeMillis - curTime) / 1000;
		return (int) (time / 3600 / 24) + 1;
	}

	public static boolean isAfterNowTime(long timeMillis) {
		Calendar currentCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMillis);
		return cal.after(currentCal);
	}

	public static String getDay(float timeMillis) {
		return new SimpleDateFormat("dd", Locale.TAIWAN).format(timeMillis);
	}

	public static String getDay(long timeMillis) {
		return new SimpleDateFormat("dd", Locale.TAIWAN).format(timeMillis);
	}

	public static String getMonth(long timeMillis) {
		return new SimpleDateFormat("M", Locale.TAIWAN).format(timeMillis);
	}

	public static String getDateTimeString(long timestamp, String time) {
		String date = new SimpleDateFormat("MM/dd E", Locale.TAIWAN).format(new Timestamp(timestamp));
		if (!TextUtils.isEmpty(time) && !"null".equals(time)) {
			return date + ", " + time;
		}
		return date;
	}

	public static String getTimeString(long timestamp) {
		return new SimpleDateFormat("HH:mm", Locale.TAIWAN).format(new Timestamp(timestamp));
	}

	public static String getNowTimeString() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.TAIWAN).format(System.currentTimeMillis());
	}

	public static boolean isSameDay(long date1, long date2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date1);
		int year1 = calendar.get(Calendar.YEAR);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);

		calendar.setTimeInMillis(date2);
		int year2 = calendar.get(Calendar.YEAR);
		int day2 = calendar.get(Calendar.DAY_OF_YEAR);
		return (year1 == year2) && (day1 == day2);
	}

	public static boolean isSameYear(long date1, long date2) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date1);
		int year1 = calendar.get(Calendar.YEAR);

		calendar.setTimeInMillis(date2);
		int year2 = calendar.get(Calendar.YEAR);
		return (year1 == year2);
	}
}
