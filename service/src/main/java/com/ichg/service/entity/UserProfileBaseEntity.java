package com.ichg.service.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.ichg.service.object.UserInfo;

import java.util.Calendar;

public class UserProfileBaseEntity {

	public UserProfileBaseEntity(UserInfo userInfo) {
		userName = userInfo.userName;
		birthday = String.valueOf(userInfo.birthday);
		sex = userInfo.gender;
		identityType = userInfo.isNationalsId ? "P" : "W";
		if (userInfo.isNationalsId) {
			personalId = userInfo.id;
		} else {
			workPermitNo = userInfo.id;
		}
		workCity = userInfo.idealWorkCity;
		workDistrict = userInfo.idealWorkArea;
		city = userInfo.city;
		district = userInfo.area;
		address = userInfo.address;
		email = userInfo.email;
		bankCode = userInfo.bankCode;
		bankAccount = userInfo.bankAccount;
	}

	public UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.userName = userName;
		userInfo.birthday = TextUtils.isEmpty(birthday) ? Calendar.getInstance().getTimeInMillis() : Long.parseLong(birthday);
		userInfo.gender = sex;
		if (identityType != null) {
			userInfo.isNationalsId = identityType.equals("P");
			userInfo.id = userInfo.isNationalsId ? personalId : workPermitNo;
		} else {
			userInfo.id = !TextUtils.isEmpty(personalId) ? personalId : workPermitNo;
		}
		userInfo.idealWorkCity = workCity;
		userInfo.idealWorkArea = workDistrict;
		userInfo.city = city;
		userInfo.area = district;
		userInfo.address = address;
		userInfo.email = email;
		userInfo.bankCode = bankCode;
		userInfo.bankAccount = bankAccount;
		return userInfo;
	}

	@SerializedName("name")
	public String userName;

	@SerializedName("birthday")
	public String birthday;

	@SerializedName("sex")
	public String sex;

	@SerializedName("identityType")
	public String identityType;

	@SerializedName("personalId")
	public String personalId;

	@SerializedName("workPermitNo")
	public String workPermitNo;

	@SerializedName("workCity")
	public String workCity;

	@SerializedName("workDistrict")
	public String workDistrict;

	@SerializedName("city")
	public String city;

	@SerializedName("district")
	public String district;

	@SerializedName("address")
	public String address;

	@SerializedName("email")
	public String email;

	@SerializedName("bankCode")
	public String bankCode;

	@SerializedName("bankAccount")
	public String bankAccount;
}
