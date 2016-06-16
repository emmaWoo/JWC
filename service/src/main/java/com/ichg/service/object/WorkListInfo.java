package com.ichg.service.object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WorkListInfo implements Serializable{

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
}
