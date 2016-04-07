package com.ichg.jwc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.activity.ProfileActivity;
import com.ichg.jwc.activity.RegisterPasswordActivity;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.utils.Debug;

public class LoginHandler {

	public static void navigateLoginFlowActivity(Activity activity, int navigateType){
		Debug.e("navigateLoginFlowActivity: " + navigateType);
		switch(navigateType){
			case AccountManager.LoginNavigationType.MAIN_PAGE:
				Debug.e("navigate: " + "MAIN_PAGE");
				startMainPage(activity);
				ActivityBase.finishAllActivities();
				break;
			case AccountManager.LoginNavigationType.REGISTER_ACCOUNT:
				Debug.e("navigate: " + "REGISTER_ACCOUNT");
				activity.startActivity(new Intent(activity, RegisterPasswordActivity.class));
				break;
			case AccountManager.LoginNavigationType.PROFILE_SETTING:
				Debug.e("navigate: " + "PROFILE_SETTING");
				activity.startActivity(new Intent(activity, ProfileActivity.class));
				break;
			default:
				//Something wrong!!!
				Debug.e("navigate: " + "ERROR_TYPE");
		}
	}

	private static void startMainPage(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
