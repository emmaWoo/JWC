package com.ichg.service.object;

import java.io.Serializable;

public class UserInfo implements Serializable{

	public String userName;
	public boolean isNationalsId;
	public String id;
	public String city;
	public String area;
	public String address;
	public String idealWorkCity;
	public String idealWorkArea;
	public String email;
	public String bankCode;
	public String bankAccount;
	public String gender;
	public long birthday;

	public boolean isMan() {
		return "M".equals(gender);
	}
}
