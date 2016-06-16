package com.ichg.service.entity;

import com.google.gson.annotations.SerializedName;
import com.ichg.service.object.WorkListInfo;

public class WorkListBaseEntity {


	public WorkListBaseEntity(WorkListInfo workListInfo) {
		id = workListInfo.id;
		title = workListInfo.title;
		city = workListInfo.city;
		workingDate = workListInfo.workingDate;
		workingTimeFrom = workListInfo.workingTimeFrom;
		workingTimeTo = workListInfo.workingTimeTo;
		payAmount = workListInfo.payAmount;
		payType = workListInfo.payType;
		createTime = workListInfo.createTime;
		follow = workListInfo.follow;
	}

	public WorkListInfo getWorkListInfo () {
		WorkListInfo workListInfo = new WorkListInfo();
		workListInfo.id = id;
		workListInfo.title = title;
		workListInfo.city = city;
		workListInfo.workingDate = workingDate;
		workListInfo.workingTimeFrom = workingTimeFrom;
		workListInfo.workingTimeTo = workingTimeTo;
		workListInfo.payAmount = payAmount;
		workListInfo.payType = payType;
		workListInfo.createTime = createTime;
		workListInfo.follow = follow;
		return workListInfo;
	}

	@SerializedName("id")
	public String id;

	@SerializedName("title")
	public String title;

	@SerializedName("city")
	public String city;

	@SerializedName("workingDate")
	public Long workingDate;

	@SerializedName("workingTimeFrom")
	public String workingTimeFrom;

	@SerializedName("workingTimeTo")
	public String workingTimeTo;

	@SerializedName("payAmount")
	public String payAmount;

	@SerializedName("payType")
	public String payType;

	@SerializedName("createTime")
	public Long createTime;

	@SerializedName("follow")
	public String follow;

}
