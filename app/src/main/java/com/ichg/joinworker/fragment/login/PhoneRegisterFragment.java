package com.ichg.joinworker.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ichg.joinworker.R;
import com.ichg.joinworker.activity.VerifyPhoneActivity;
import com.ichg.joinworker.fragment.FragmentBase;
import com.ichg.joinworker.listener.DialogListener;
import com.ichg.joinworker.manager.ToolbarManager;
import com.ichg.joinworker.presenter.VerifyPhonePresenter;
import com.ichg.joinworker.utils.DialogManager;

public class PhoneRegisterFragment extends FragmentBase {

	private VerifyPhonePresenter mPresenter;

	private EditText phoneNumberEditText;

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
		initPhoneEditText(contentView);
		initSubmitButton(contentView);
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

	private void initPhoneEditText(View contentView) {
		phoneNumberEditText = (EditText) contentView.findViewById(R.id.edit_phone_number);
		phoneNumberEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startVerifyPhoneFlow();
				return true;
			}
			return false;
		});
	}

	private void showKeyboard(View focusView) {
		focusView.requestFocus();
		showSoftInputFromWindow();
	}

	private void initSubmitButton(View contentView) {
		View buttonSubmit = contentView.findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(v -> startVerifyPhoneFlow());
	}


	private void startVerifyPhoneFlow() {
		if (TextUtils.isEmpty(phoneNumberEditText.getText().toString())) {
			phoneNumberEditText.setError(getActivityBase().getString(R.string.phone_number_hint));
			return;
		}
		DialogManager.with(getActivityBase()).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		mPresenter.startVerifyPhoneFlow(phoneNumberEditText.getText().toString());
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
}
