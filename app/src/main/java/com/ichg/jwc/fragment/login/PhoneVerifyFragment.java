package com.ichg.jwc.fragment.login;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.VerifyPhoneActivity;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.VerifyPhonePresenter;
import com.ichg.jwc.utils.DialogManager;


public class PhoneVerifyFragment extends FragmentBase {

	private static final String PATTERN = "\\s([0-9]{5})\\s";

	private EditText verifyCodeEditText;
	private VerifyPhonePresenter mPresenter;
	private BroadcastReceiver mIntentReceiver;
	private PackageManager mPackageManager;
	private ComponentName mComponentName;
	private String mVerifyCode;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mPresenter = ((VerifyPhoneActivity) getActivity()).getPresenter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		initSMSReceiver();
//		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
//		mIntentReceiver = new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				String msg = intent.getStringExtra(SMSReceiver.GET_MESSAGE);
//				Matcher matcher = Pattern.compile(PATTERN).matcher(msg);
//				if (matcher.find()) {
//					String verifyCode = matcher.toMatchResult().group(0).trim();
//					if (getActivityBase().isActive) {
//						verifyCodeAutoSubmit(verifyCode);
//					} else {
//						mVerifyCode = verifyCode;
//					}
//				}
//			}
//		};
//		getActivityBase().registerReceiver(mIntentReceiver, intentFilter);
	}

//	private void initSMSReceiver() {
//		mPackageManager = getActivityBase().getPackageManager();
//		mComponentName = new ComponentName(getActivityBase(), SMSReceiver.class);
//		mPackageManager.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_verify_phone_number, container, false);
		initToolbar(contentView);
		initVerifyCodeEditText(contentView);
		initVerifySubmitButton(contentView);
		initResendButton(contentView);
		return contentView;
	}

	private void initToolbar(View contentView) {
		ToolbarManager.init((Toolbar) contentView.findViewById(R.id.toolbar))
				.title(R.string.verify_phone_number)
				.backNavigation(v -> onNavigationClick());
	}

	private void initResendButton(View contentView) {
		contentView.findViewById(R.id.button_resend).setOnClickListener(v -> {
			DialogManager.with(getActivityBase()).setListener(new DialogListener() {
				@Override
				public void onCancel() {
					mPresenter.cancel();
				}
			}).showProgressingDialog();
			mPresenter.resendVerifyCode();
		});
	}

	private void initVerifyCodeEditText(View contentView) {
		verifyCodeEditText = (EditText) contentView.findViewById(R.id.edit_verify_code);
		verifyCodeEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startVerifyCode();
				return true;
			}
			return false;
		});

	}

	private void initVerifySubmitButton(View contentView) {
		View buttonSubmit = contentView.findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(v -> startVerifyCode());
	}

	private void startVerifyCode(){
		final String verifyCode = verifyCodeEditText.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			verifyCodeEditText.setError(getActivity().getString(R.string.verify_code_hint));
			return;
		}
		startVerifyCodeRequest(verifyCode);
	}

	private void startVerifyCodeRequest(String verifyCode){
		DialogManager.with(getActivityBase()).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		mPresenter.checkVerifyCode(verifyCode);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(mVerifyCode)) {
			verifyCodeAutoSubmit(mVerifyCode);
		}
	}

	private void verifyCodeAutoSubmit(String verifyCode) {
		verifyCodeEditText.setText(verifyCode);
		verifyCodeEditText.postDelayed(() -> startVerifyCode(), 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//mPackageManager.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		//getActivityBase().unregisterReceiver(mIntentReceiver);
	}
}
