package com.ichg.service.entity;

import com.google.gson.annotations.SerializedName;
import com.ichg.service.object.WorkDetailInfo;

public class WorkDetailBaseEntity {

	public WorkDetailBaseEntity(WorkDetailInfo workDetailInfo) {
		id = workDetailInfo.id;
		typeName = workDetailInfo.typeName;
		title = workDetailInfo.title;
		workingDate = workDetailInfo.workingDate;
		workingTimeFrom = workDetailInfo.workingTimeFrom;
		workingTimeTo = workDetailInfo.workingTimeTo;
		manpower = workDetailInfo.needPeople;
		matched = workDetailInfo.matchedPeople;
		city = workDetailInfo.city;
		district = workDetailInfo.district;
		address = workDetailInfo.address;
		content = workDetailInfo.content;
		serve = workDetailInfo.haveMeals;
		serveContent = workDetailInfo.mealsContent;
		payDate = workDetailInfo.payDate;
		payType = workDetailInfo.payType;
		payAmount = workDetailInfo.payAmount;
		companyName = workDetailInfo.companyName;
		companyContactName = workDetailInfo.companyContactName;
		companyContactPhone = workDetailInfo.companyContactPhone;
		follow = workDetailInfo.follow;
		status = workDetailInfo.status;
	}

	public WorkDetailInfo getWorkDetailInfo () {
		WorkDetailInfo workDetailInfo = new WorkDetailInfo();
		workDetailInfo.id = id;
		workDetailInfo.typeName = typeName;
		workDetailInfo.title = title;
		workDetailInfo.workingDate = workingDate;
		workDetailInfo.workingTimeFrom = workingTimeFrom;
		workDetailInfo.workingTimeTo = workingTimeTo;
		workDetailInfo.needPeople = manpower;
		workDetailInfo.matchedPeople = matched;
		workDetailInfo.city = city;
		workDetailInfo.district = district;
		workDetailInfo.address = address;
		workDetailInfo.content = content;
		workDetailInfo.haveMeals = serve;
		workDetailInfo.mealsContent = serveContent;
		workDetailInfo.payDate = payDate;
		workDetailInfo.payType = payType;
		workDetailInfo.payAmount = payAmount;
		workDetailInfo.companyName = companyName;
		workDetailInfo.companyContactName = companyContactName;
		workDetailInfo.companyContactPhone = companyContactPhone;
		workDetailInfo.follow = follow;
		workDetailInfo.status = status;
		return workDetailInfo;
	}

	@SerializedName("id")
	public String id;

	@SerializedName("typeName")
	public String typeName;

	@SerializedName("title")
	public String title;

	@SerializedName("workingDate")
	public Long workingDate;

	@SerializedName("workingTimeFrom")
	public String workingTimeFrom;

	@SerializedName("workingTimeTo")
	public String workingTimeTo;

	@SerializedName("manpower")
	public String manpower;

	@SerializedName("matched")
	public String matched;

	@SerializedName("payType")
	public String payType;

	@SerializedName("city")
	public String city;

	@SerializedName("district")
	public String district;

	@SerializedName("address")
	public String address;

	@SerializedName("content")
	public String content;

	@SerializedName("serve")
	public String serve;

	@SerializedName("serveContent")
	public String serveContent;

	@SerializedName("payDate")
	public String payDate;

	@SerializedName("payAmount")
	public int payAmount;

	@SerializedName("companyName")
	public String companyName;

	@SerializedName("companyContactName")
	public String companyContactName;

	@SerializedName("companyContactPhone")
	public String companyContactPhone;

	@SerializedName("follow")
	public String follow;

	@SerializedName("status")
	public String status;


}
