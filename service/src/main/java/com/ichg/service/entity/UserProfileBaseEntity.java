package com.ichg.service.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfileBaseEntity implements Serializable {
	@SerializedName("username")
	public String userName;
	@SerializedName("nickname")
	public String nickname;
	@SerializedName("image")
	public String avatarUrl;
}
