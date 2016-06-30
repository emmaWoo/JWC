package com.ichg.service.object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WorkListInfo implements Serializable{

	public static String CREATE_WORK = "P";
	public static String RESPONSE_WORK = "A";
	public static String DETERMINE_WORK = "I";
	public static String DONE_WORK = "W";

	public String id;
	public String title;
	public String city;
	public long workingDate;
	public String workingTimeFrom;
	public String workingTimeTo;
	public String payAmount;
	public String payType; // H|D
	public long createTime;
	public String follow; // Y\N
	public String status;

	public boolean isDailyWage() {
		return "D".equals(payType);
	}

	public String getWorkDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(workingDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		return simpleDateFormat.format(calendar.getTime());
	}

	public String getCreateTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(createTime);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		return simpleDateFormat.format(calendar.getTime());
	}


	public boolean isFollow() {
		return "Y".equals(follow);
	}

	public void updateFollow() {
		if ("Y".equals(follow)) {
			follow = "N";
		} else {
			follow = "Y";
		}
	}
}
