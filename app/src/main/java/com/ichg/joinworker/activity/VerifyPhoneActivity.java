package com.ichg.joinworker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ichg.joinworker.R;
import com.ichg.joinworker.fragment.FragmentBase;
import com.ichg.joinworker.fragment.login.PhoneDuplicateFragment;
import com.ichg.joinworker.fragment.login.PhoneRegisterFragment;
import com.ichg.joinworker.fragment.login.PhoneVerifyFragment;
import com.ichg.joinworker.listener.BindingPhoneListener;
import com.ichg.joinworker.listener.RegisterPhoneListener;
import com.ichg.joinworker.presenter.PresenterFactory;
import com.ichg.joinworker.presenter.VerifyPhonePresenter;
import com.ichg.joinworker.utils.DialogManager;
import com.ichg.service.entity.UserProfileBaseEntity;

public class VerifyPhoneActivity extends ActivityBase implements RegisterPhoneListener, BindingPhoneListener {

	public static final String OPTIONAL_INPUT_BOOLEAN_OS_FROM_SETTING = "0";

	private VerifyPhonePresenter mPresenter;

	private PhoneRegisterFragment registerFragment;
	private PhoneDuplicateFragment duplicateFragment;
	private PhoneVerifyFragment verifyFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		initPresenter();
		initFragments();
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//			if (FragmentBase.getAnimationType() == AnimationType.PUSH) {
//				FragmentBase.setAnimation(AnimationType.POP);
//			}
			getSupportFragmentManager().popBackStack();
//		} else if(!isModifyMode() && mPresenter.isSignIn()){
//			DialogManager.with(this).setMessage(R.string.dialog_is_leave_login).setListener(new DialogListener() {
//				@Override
//				public void onPositive() {
//					super.onPositive();
//					mPresenter.logout();
//					VerifyPhoneActivity.super.onBackPressed();
//					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//				}
//			}).showYesOrNoDialog();
		} else {
			VerifyPhoneActivity.super.onBackPressed();
//			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
	}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = PresenterFactory.createVerifyPhonePresenter(this);
		}
	}

	private void initFragments() {
		if (registerFragment == null) {
			registerFragment = new PhoneRegisterFragment();
		}
		if (verifyFragment == null) {
			verifyFragment = new PhoneVerifyFragment();
		}
		if (duplicateFragment == null) {
			duplicateFragment = new PhoneDuplicateFragment();
		}
//		FragmentBase.setAnimation(AnimationType.PUSH);
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().add(R.id.registerPhone_contentLayout, registerFragment, PhoneRegisterFragment.class.getName()).commit();
	}

	public VerifyPhonePresenter getPresenter() {
		return mPresenter;
	}


	@Override
	public void onRequestFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
//		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onGetVerifyCodeFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
//		if (errorType == GetVerifyCodeAPI.ErrorCode.PERMISSION_DENY) {
//			DialogManager.with(this).setMessage(R.string.permission_deny).showAlertDialog();
//		} else if (errorType == GetVerifyCodeAPI.ErrorCode.DEVICE_FORMAT_INVALID) {
//			DialogManager.with(this).setMessage(R.string.unknow_device_id).showAlertDialog();
//		} else {
//			DialogManager.with(this).showAPIErrorDialog(errorType, message);
//		}
	}

	@Override
	public boolean isModifyMode() {
		return getIntent().getBooleanExtra(OPTIONAL_INPUT_BOOLEAN_OS_FROM_SETTING, false);
	}

	@Override
	public void onBindPhoneFail(int errorCode, String errorMessage) {
		DialogManager.with(this).dismissDialog();
//		if (errorCode == BindingVerificationAPI.ErrorCode.CELLPHONE_CODE_ERROR) {
//			DialogManager.with(this).setMessage(R.string.cellphone_code_error).showAlertDialog();
//		} else if (errorCode == OAuth2LoginAPI.ErrorCode.USER_NOT_FOUND) {
//			DialogManager.with(this).setMessage(R.string.login_failed).showAlertDialog();
//		} else if (errorCode == OAuth2LoginAPI.ErrorCode.DATABASE_ERROR) {
//			DialogManager.with(this).setMessage(R.string.register_failed).showAlertDialog();
//		} else if (errorCode == OAuth2LoginAPI.ErrorCode.PERMISSION_DENY) {
//			DialogManager.with(this).setMessage(R.string.permission_deny).showAlertDialog();
//		} else if (errorCode == OAuth2LoginAPI.ErrorCode.NETWORK_NOT_AVAILABLE) {
//			DialogManager.with(this).setMessage(R.string.network_not_available).showAlertDialog();
//		} else {
//			DialogManager.with(this).showAPIErrorDialog(errorCode, errorMessage);
//		}
	}

	@Override
	public void onSettingBindSuccess() {
//		setResult(Constant.UIMessage.ON_RELOAD_DATA);
		finish();
	}

	@Override
	public void onPhoneDuplicate(UserProfileBaseEntity userProfileBase) {
		DialogManager.with(this).dismissDialog();
		Bundle arguments = new Bundle();
		//arguments.putSerializable(PhoneDuplicateFragment.INPUT_USER_PROFILE_BASE_ENTITY, userProfileBase);
		duplicateFragment.setArguments(arguments);
		changePage(duplicateFragment);
	}

	@Override
	public void onPhoneNoUsed() {
		registerFragment.requestVerifyCode();
	}

	@Override
	public void startVerifyPhone() {
		DialogManager.with(this).dismissDialog();
		changePage(verifyFragment);
	}

	@Override
	public void onVerifyPhoneSuccess(int loginNavigationType) {
//		LoginViewNavigateHandler.navigateLoginFlowActivity(this, JoinWorkerApp.accountManager.checkLoginNavigation());
	}

	@Override
	public void onVerifyPhoneFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
//		if (errorType == BindingVerificationAPI.ErrorCode.CELLPHONE_CODE_ERROR) {
//			DialogManager.with(this).setMessage(R.string.cellphone_code_error).showAlertDialog();
//		} else if (errorType == LoginApiBase.ErrorCode.DATABASE_ERROR) {
//			DialogManager.with(this).setMessage(R.string.register_failed).showAlertDialog();
//		} else if (errorType == LoginApiBase.ErrorCode.PERMISSION_DENY) {
//			DialogManager.with(this).setMessage(R.string.permission_deny).showAlertDialog();
//		} else {
//			DialogManager.with(this).showAPIErrorDialog(errorType, message);
//		}
	}

	@Override
	public void onRegisterLoginSuccess(int loginNavigationType) {
		DialogManager.with(this).dismissDialog();
//		LoginViewNavigateHandler.navigateLoginFlowActivity(this, loginNavigationType);
		startActivity(new Intent(this, RegisterPasswordActivity.class));
		finish();
	}

	@Override
	public void onRegisterLoginFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
//		if (errorType == LoginApiBase.ErrorCode.USER_NOT_FOUND) {
//			DialogManager.with(this).setMessage(R.string.login_failed).showAlertDialog();
//		} else if (errorType == LoginApiBase.ErrorCode.DATABASE_ERROR) {
//			DialogManager.with(this).setMessage(R.string.register_failed).showAlertDialog();
//		} else if (errorType == LoginApiBase.ErrorCode.PERMISSION_DENY) {
//			DialogManager.with(this).setMessage(R.string.permission_deny).showAlertDialog();
//		} else {
//			DialogManager.with(this).showAPIErrorDialog(errorType, message);
//		}
	}

	@Override
	public void onResendVerifyCodeSuccess() {
		DialogManager.with(this).dismissDialog();
//		Toast.makeText(this, R.string.resend_verify_code, Toast.LENGTH_SHORT).show();
	}


	private void changePage(FragmentBase fragment) {
//		FragmentBase.setAnimation(AnimationType.PUSH);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.registerPhone_contentLayout, fragment, fragment.getClass().getName());
		transaction.addToBackStack(null).commit();
	}

}
