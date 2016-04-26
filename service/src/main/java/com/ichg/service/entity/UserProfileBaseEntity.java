package com.ichg.service.entity;

import com.google.gson.annotations.SerializedName;
import com.ichg.service.object.UserInfo;

public class UserProfileBaseEntity {

	public UserProfileBaseEntity(UserInfo userInfo) {
		userName = userInfo.userName;
		birthday = userInfo.birthday;
		sex = userInfo.gender;
		identityType = userInfo.isNationalsId ? "P" : "W";
		if (userInfo.isNationalsId) {
			personalId = userInfo.id;
		} else {
			workPermitNo = userInfo.id;
		}
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
		userInfo.birthday = birthday;
		userInfo.gender = sex;
		if (identityType != null) {
			userInfo.isNationalsId = identityType.equals("P");
			userInfo.id = userInfo.isNationalsId ? personalId : workPermitNo;
		}
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
	public long birthday;

	@SerializedName("sex")
	public String sex;

	@SerializedName("identityType")
	public String identityType;

	@SerializedName("personalId")
	public String personalId;

	@SerializedName("workPermitNo")
	public String workPermitNo;

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
