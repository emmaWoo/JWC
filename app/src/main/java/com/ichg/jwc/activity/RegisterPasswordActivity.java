package com.ichg.jwc.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.RegisterPasswordPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.utils.LoginHandler;

public class RegisterPasswordActivity extends ActivityBase implements PresenterListener {

	private EditText editPassword;
	private EditText editCheckingPassword;
	private View buttonSubmit;

	private RegisterPasswordPresenter presenter;

	private final View.OnClickListener submitListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			DialogManager.with(RegisterPasswordActivity.this).setListener(new DialogListener() {
				@Override
				public void onCancel() {
					presenter.cancel();
				}
			}).showProgressingDialog();
			presenter.activateAccount(editPassword.getText().toString());
		}
	};

	private final TextView.OnEditorActionListener passwordEditorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				editCheckingPassword.requestFocus();
				return true;
			}
			return false;
		}
	};

	private final TextView.OnEditorActionListener passwordCheckingEditorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				buttonSubmit.performClick();
				return true;
			}
			return false;
		}
	};

	private final TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			presenter.checkDataAvailable(editPassword.getText().toString(), editCheckingPassword.getText().toString());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register_account);
		initPresenter();
		initToolbar();
		initEditText();
		initSubmit();
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_register)
				.backNavigation(v -> onBackPressed());
	}

	private void initPresenter() {
		presenter = new RegisterPasswordPresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
	}

	private void initEditText() {
		editPassword = (EditText) findViewById(R.id.edit_password);
		editPassword.setOnEditorActionListener(passwordEditorActionListener);
		editPassword.addTextChangedListener(textWatcher);
		editCheckingPassword = (EditText) findViewById(R.id.edit_checking_password);
		editCheckingPassword.setOnEditorActionListener(passwordCheckingEditorActionListener);
		editCheckingPassword.addTextChangedListener(textWatcher);
	}

	private void initSubmit() {
		buttonSubmit = findViewById(R.id.button_submit);
		buttonSubmit.setEnabled(false);
		buttonSubmit.setOnClickListener(submitListener);
	}

	@Override
	public void onBackPressed() {
		DialogManager.with(this).setMessage(R.string.dialog_is_leave_register).setListener(new DialogListener() {
			@Override
			public void onPositive() {
				super.onPositive();
				JoinWorkerApp.accountManager.logout();
				finish();
				//getActivity().overridePendingTransition(R.anim.activity_slide_in_up, android.R.anim.fade_out);
			}
		}).showYesOrNoDialog();
	}

	@Override
	public void onPause() {
		super.onPause();
		presenter.cancel();
	}

	@Override
	public void onFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onSuccess() {
		LoginHandler.navigateLoginFlowActivity(this, presenter.checkPageNavigation());
		finishAllActivities();
	}

	@Override
	public void onInputFormatCheckFinish(boolean isPass) {
		buttonSubmit.setEnabled(isPass);
		buttonSubmit.setBackgroundResource(isPass ? R.drawable.bg_orange_round_corner : R.drawable.bg_gray_round_corner);
	}
}
