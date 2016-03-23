package com.ichg.joinworker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ichg.joinworker.activity.ActivityBase;
import com.ichg.joinworker.activity.MainActivity;
import com.ichg.joinworker.activity.ProfileActivity;
import com.ichg.joinworker.activity.RegisterPasswordActivity;
import com.ichg.joinworker.activity.VerifyPhoneActivity;
import com.ichg.joinworker.fragment.FragmentBase;
import com.ichg.joinworker.manager.AccountManager;
import com.ichg.service.utils.Debug;

public class LoginHandler {

	public static void navigateLoginFlow(FragmentBase fragmentBase, int navigateType){
		switch(navigateType){
			case AccountManager.LoginNavigationType.MAIN_PAGE:
				Debug.e("navigate: " + "MAIN_PAGE");
				startMainPage(fragmentBase.getActivityBase());
				break;
			case AccountManager.LoginNavigationType.REGISTER_CELLPHONE:
				Debug.e("navigate: " + "REGISTER_CELLPHONE");
				startRegisterCellphonePage(fragmentBase);
				break;
			case AccountManager.LoginNavigationType.REGISTER_ACCOUNT:
				Debug.e("navigate: " + "REGISTER_ACCOUNT");
				//startRegisterAccountPage(fragmentBase);
				break;
			case AccountManager.LoginNavigationType.PROFILE_SETTING:
				Debug.e("navigate: " + "PROFILE_SETTING");
				//startProfileSettingPage(fragmentBase);
				break;
			default:
				//Something wrong!!!
				Debug.e("navigate: " + "ERROR_TYPE");
		}
	}

	public static void navigateLoginFlowActivity(Activity activity, int navigateType){
		Debug.e("navigateLoginFlowActivity: " + navigateType);
		switch(navigateType){
			case AccountManager.LoginNavigationType.MAIN_PAGE:
				Debug.e("navigate: " + "MAIN_PAGE");
				startMainPage(activity);
				ActivityBase.finishAllActivities();
				break;
			case AccountManager.LoginNavigationType.REGISTER_CELLPHONE:
				Debug.e("navigate: " + "REGISTER_CELLPHONE");
				activity.startActivity(new Intent(activity, VerifyPhoneActivity.class));
				//activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

	private static void startRegisterCellphonePage(FragmentBase fragmentBase) {
		Intent intent = new Intent(fragmentBase.getActivityBase(), VerifyPhoneActivity.class);
		fragmentBase.getActivityBase().startActivity(intent);
		//fragmentBase.getActivityBase().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	private static void startMainPage(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
