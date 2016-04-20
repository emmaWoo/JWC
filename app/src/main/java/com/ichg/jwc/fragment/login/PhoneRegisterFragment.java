package com.ichg.jwc.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.VerifyPhoneActivity;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.VerifyPhonePresenter;
import com.ichg.jwc.utils.DialogManager;

public class PhoneRegisterFragment extends FragmentBase {

	private VerifyPhonePresenter mPresenter;

	private EditText phoneNumberEditText;
	private EditText verifyCodeEditText;
	private Button buttonSubmit;
	private View buttonNext;
	private CountDownTimer countDownTimer;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mPresenter = ((VerifyPhoneActivity) context).getPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_register_phone, container, false);
		initToolbar(contentView);
		initEditText(contentView);
		initButton(contentView);
		return contentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		showKeyboard(phoneNumberEditText);
	}

	private void initToolbar(View contentView) {
		ToolbarManager.init((Toolbar) contentView.findViewById(R.id.toolbar))
				.title(R.string.account_register)
				.backNavigation(v -> onNavigationClick());
	}

	private void initEditText(View contentView) {
		phoneNumberEditText = (EditText) contentView.findViewById(R.id.edit_phone_number);
		phoneNumberEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startVerifyPhoneFlow();
				return true;
			}
			return false;
		});
		verifyCodeEditText = (EditText) contentView.findViewById(R.id.edit_verify_code);
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

	private void initButton(View contentView) {
		buttonSubmit = (Button) contentView.findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(v -> startVerifyPhoneFlow());
		buttonNext = contentView.findViewById(R.id.button_next);
		buttonNext.setOnClickListener(v -> startVerifyCode());
	}

	private void startVerifyPhoneFlow() {
		String phoneNumber =phoneNumberEditText.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			phoneNumberEditText.setError(getActivityBase().getString(R.string.phone_number_hint));
			return;
		}
		if (phoneNumber.length() < 10 || !phoneNumber.startsWith("09")) {
			phoneNumberEditText.setError(getActivityBase().getString(R.string.phone_number_size_hint));
			return;
		}
		DialogManager.with(getActivityBase()).setListener(new DialogListener() {
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
		DialogManager.with(getActivityBase()).dismissDialog();
		DialogManager.with(getActivityBase()).setMessage(R.string.send_verify_code).setListener(new DialogListener() {
			@Override
			public void onPositive() {
				super.onPositive();
				DialogManager.with(getActivityBase()).setListener(new DialogListener() {
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
			verifyCodeEditText.setError(getActivity().getString(R.string.verify_code_hint));
			verifyCodeEditText.requestFocus();
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
	}
}
