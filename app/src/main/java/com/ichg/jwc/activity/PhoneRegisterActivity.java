package com.ichg.jwc.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.RegisterPhoneListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.RegisterPhonePresenter;
import com.ichg.jwc.receiver.SMSReceiver;
import com.ichg.jwc.utils.DialogManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneRegisterActivity extends ActivityBase implements RegisterPhoneListener {

	private static final String PATTERN = "\\s([0-9]{6})\\s";

	private RegisterPhonePresenter mPresenter;

	private EditText phoneNumberEditText;
	private EditText verifyCodeEditText;
	private Button buttonSubmit;
	private View buttonNext;
	private CountDownTimer countDownTimer;
	private BroadcastReceiver mIntentReceiver;
	private PackageManager mPackageManager;
	private ComponentName mComponentName;
	private String mVerifyCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		initToolbar();
		initPresenter();
		initEditText();
		initButton();
		initSMSReceiver();
		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
		mIntentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String msg = intent.getStringExtra(SMSReceiver.GET_MESSAGE);
				Matcher matcher = Pattern.compile(PATTERN).matcher(msg);
				if (matcher.find()) {
					String verifyCode = matcher.toMatchResult().group(0).trim();
					if (isActive) {
						verifyCodeAutoSubmit(verifyCode);
					} else {
						mVerifyCode = verifyCode;
					}
				}
			}
		};
		registerReceiver(mIntentReceiver, intentFilter);
	}

	private void initSMSReceiver() {
		mPackageManager = getPackageManager();
		mComponentName = new ComponentName(this, SMSReceiver.class);
		mPackageManager.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}

	private void initResendButton(View contentView) {
		contentView.findViewById(R.id.button_resend).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogManager.with(PhoneRegisterActivity.this).setListener(new DialogListener() {
					@Override
					public void onCancel() {
						mPresenter.cancel();
					}
				}).showProgressingDialog();
				mPresenter.resendVerifyCode();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		showKeyboard(phoneNumberEditText);
		if (!TextUtils.isEmpty(mVerifyCode)) {
			verifyCodeAutoSubmit(mVerifyCode);
		}
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_register)
				.backNavigation(v -> onBackPressed());
	}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = new RegisterPhonePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
		}
	}

	private void initEditText() {
		phoneNumberEditText = (EditText) findViewById(R.id.edit_phone_number);
		phoneNumberEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startVerifyPhoneFlow();
				return true;
			}
			return false;
		});
		verifyCodeEditText = (EditText) findViewById(R.id.edit_verify_code);
		verifyCodeEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startVerifyCode();
				return true;
			}
			return false;
		});
	}

	private void showKeyboard(View focusView) {
		focusView.requestFocus();
		showSoftInputFromWindow();
	}

	private void initButton() {
		buttonSubmit = (Button) findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(v -> startVerifyPhoneFlow());
		buttonNext = findViewById(R.id.button_next);
		buttonNext.setOnClickListener(v -> startVerifyCode());
	}

	private void startVerifyPhoneFlow() {
		String phoneNumber =phoneNumberEditText.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			phoneNumberEditText.setError(this.getString(R.string.phone_number_hint));
			return;
		}
		if (phoneNumber.length() < 10 || !phoneNumber.startsWith("09")) {
			phoneNumberEditText.setError(this.getString(R.string.phone_number_size_hint));
			return;
		}
		DialogManager.with(this).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		if(buttonNext.getVisibility() == View.GONE) {
			mPresenter.startVerifyPhoneFlow(phoneNumberEditText.getText().toString());
		} else {
			mPresenter.requestVerifyCodeFromServer(false);
		}
	}

	public void requestVerifyCode() {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).setMessage(R.string.send_verify_code).setListener(new DialogListener() {
			@Override
			public void onPositive() {
				super.onPositive();
				DialogManager.with(PhoneRegisterActivity.this).setListener(new DialogListener() {
					@Override
					public void onCancel() {
						mPresenter.cancel();
					}
				}).showProgressingDialog();
				mPresenter.requestVerifyCodeFromServer(true);
			}
		}).showYesOrNoDialog();
	}

	public void showVerifyCode() {
		DialogManager.with(this).dismissDialog();
		verifyCodeEditText.setVisibility(View.VISIBLE);
		buttonNext.setVisibility(View.VISIBLE);
		resendTimer();
	}

	private void resendTimer() {
		countDownTimer = new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				phoneNumberEditText.setEnabled(false);
				buttonSubmit.setEnabled(false);
				buttonSubmit.setBackgroundResource(R.drawable.bg_gray_round_corner);
				buttonSubmit.setText(getString(R.string.resend) + millisUntilFinished / 1000);
			}

			public void onFinish() {
				buttonSubmit.setText(getString(R.string.resend));
				buttonSubmit.setBackgroundResource(R.drawable.bg_green_round_corner);
				buttonSubmit.setEnabled(true);
				phoneNumberEditText.setEnabled(true);
			}
		};
		countDownTimer.start();
	}

	private void startVerifyCode(){
		final String verifyCode = verifyCodeEditText.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			verifyCodeEditText.setError(this.getString(R.string.verify_code_hint));
			verifyCodeEditText.requestFocus();
			return;
		}
		startVerifyCodeRequest(verifyCode);
	}

	private void startVerifyCodeRequest(String verifyCode){
		DialogManager.with(this).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		mPresenter.checkVerifyCode(verifyCode);
	}

	private void verifyCodeAutoSubmit(String verifyCode) {
		verifyCodeEditText.setText(verifyCode);
		verifyCodeEditText.postDelayed(() -> startVerifyCode(), 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (countDownTimer != null) {
			countDownTimer.cancel();
		}
		mPackageManager.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		unregisterReceiver(mIntentReceiver);
	}

	@Override
	public void onPhoneDuplicate() {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).setMessage(R.string.duplicate_phone).showAlertDialog();
	}

	@Override
	public void onPhoneNoUsed() {
		requestVerifyCode();
	}

	@Override
	public void startVerifyPhone() {
		showVerifyCode();
	}

	@Override
	public void onResendVerifyCodeSuccess() {
		DialogManager.with(this).dismissDialog();
		Toast.makeText(this, R.string.resend_verify_code, Toast.LENGTH_SHORT).show();
		showVerifyCode();
	}

	@Override
	public void onRegisterLoginSuccess(int loginNavigationType) {
		DialogManager.with(this).dismissDialog();
		startActivity(new Intent(this, RegisterPasswordActivity.class));
		//		LoginHandler.navigateLoginFlowActivity(this, presenter.checkPageNavigation());
	}

	@Override
	public void onRequestFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}
}
