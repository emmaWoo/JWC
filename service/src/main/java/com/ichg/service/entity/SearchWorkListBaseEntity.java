package com.ichg.service.entity;

import com.google.gson.annotations.SerializedName;
import com.ichg.service.object.BaseSearchInfo;
import com.ichg.service.object.SearchInfo;

public class SearchWorkListBaseEntity {

	public SearchWorkListBaseEntity(BaseSearchInfo baseSearchInfo) {
		keyword = baseSearchInfo.keyword;
		timeOption = String.valueOf(baseSearchInfo.timeOption);
		cityOption = baseSearchInfo.cityOption;
	}

	public BaseSearchInfo getBaseSearchInfo() {
		BaseSearchInfo baseSearchInfo = new BaseSearchInfo();
		baseSearchInfo.keyword = keyword;
		baseSearchInfo.timeOption = Integer.parseInt(timeOption);
		baseSearchInfo.cityOption = cityOption;
		return baseSearchInfo;
	}

	public SearchWorkListBaseEntity(SearchInfo searchInfo) {
		title = searchInfo.title;
		payType = searchInfo.payType;
		payAmountFrom = searchInfo.payAmountFrom;
		payAmountTo = searchInfo.payAmountTo;
		city = searchInfo.city;
		district = searchInfo.district;
		workingDate = searchInfo.workingDate;
		workingTimeFrom = searchInfo.workingTimeFrom;
		workingTimeTo = searchInfo.workingTimeTo;
		typeId = searchInfo.typeId;
	}

	public SearchInfo getSearchInfo () {
		SearchInfo searchInfo = new SearchInfo();
		searchInfo.title = title;
		searchInfo.payType = payType;
		searchInfo.payAmountFrom = payAmountFrom;
		searchInfo.payAmountTo = payAmountTo;
		searchInfo.city = city;
		searchInfo.district = district;
		searchInfo.workingDate = workingDate;
		searchInfo.workingTimeFrom = workingTimeFrom;
		searchInfo.workingTimeTo = workingTimeTo;
		searchInfo.typeId = typeId;
		return searchInfo;
	}

	@SerializedName("keyword")
	public String keyword;

	@SerializedName("timeOption")
	public String timeOption;

	@SerializedName("cityOption")
	public String cityOption;

	@SerializedName("title")
	public String title;

	@SerializedName("payType")
	public String payType;

	@SerializedName("payAmountFrom")
	public String payAmountFrom;

	@SerializedName("payAmountTo")
	public String payAmountTo;

	@SerializedName("city")
	public String city;

	@SerializedName("district")
	public String district;

	@SerializedName("workingDate")
	public Long workingDate;

	@SerializedName("workingTimeFrom")
	public String workingTimeFrom;

	@SerializedName("workingTimeTo")
	public String workingTimeTo;

	@SerializedName("typeId")
	public String typeId;
}
