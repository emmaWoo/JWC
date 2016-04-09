package com.ichg.jwc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.ichg.jwc.R;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.fragment.login.PhoneDuplicateFragment;
import com.ichg.jwc.fragment.login.PhoneRegisterFragment;
import com.ichg.jwc.listener.RegisterPhoneListener;
import com.ichg.jwc.presenter.PresenterFactory;
import com.ichg.jwc.presenter.VerifyPhonePresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.entity.UserProfileBaseEntity;

public class VerifyPhoneActivity extends ActivityBase implements RegisterPhoneListener {

	public static final String OPTIONAL_INPUT_BOOLEAN_OS_FROM_SETTING = "0";

	private VerifyPhonePresenter mPresenter;

	private PhoneRegisterFragment registerFragment;
	private PhoneDuplicateFragment duplicateFragment;
	//private PhoneVerifyFragment verifyFragment;

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
//		if (verifyFragment == null) {
//			verifyFragment = new PhoneVerifyFragment();
//		}
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
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onGetVerifyCodeFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onPhoneDuplicate() {
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
		registerFragment.showVerifyCode();
	}

	@Override
	public void onVerifyPhoneFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onRegisterLoginSuccess(int loginNavigationType) {
		DialogManager.with(this).dismissDialog();
//		LoginViewNavigateHandler.navigateLoginFlowActivity(this, loginNavigationType);
		startActivity(new Intent(this, RegisterPasswordActivity.class));
		finish();
	}

	@Override
	public void onResendVerifyCodeSuccess() {
		DialogManager.with(this).dismissDialog();
		Toast.makeText(this, R.string.resend_verify_code, Toast.LENGTH_SHORT).show();
		registerFragment.showVerifyCode();
	}


	private void changePage(FragmentBase fragment) {
//		FragmentBase.setAnimation(AnimationType.PUSH);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.registerPhone_contentLayout, fragment, fragment.getClass().getName());
		transaction.addToBackStack(null).commit();
	}

}
