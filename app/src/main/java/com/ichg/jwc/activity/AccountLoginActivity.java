package com.ichg.jwc.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.AccountLoginPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.utils.LoginHandler;

public class AccountLoginActivity extends ActivityBase implements PresenterListener {

	private AccountLoginPresenter mPresenter;
	private View submitButton;
	private EditText accountEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_login);
		initPresenter();
		initToolbar();
		initInputEditTexts();
		initSubmitButton();
		initRegisterButton();
		initForgetButton();
	}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = new AccountLoginPresenter(JoinWorkerApp.accountManager, this);
		}
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_login);
	}

	private void initInputEditTexts() {
		accountEditText = (EditText) findViewById(R.id.login_edit_account);
		accountEditText.addTextChangedListener(textWatcher);
		passwordEditText = (EditText) findViewById(R.id.login_edit_password);
		passwordEditText.addTextChangedListener(textWatcher);
		passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				startAccountLogin();
				return true;
			}
			return false;
		});
	}

	private void initSubmitButton() {
		submitButton = findViewById(R.id.login_submit_button);
		submitButton.setOnClickListener(v -> startAccountLogin());
	}

	private void initRegisterButton() {
		findViewById(R.id.login_sign_in).setOnClickListener(v -> {
			startActivity(new Intent(getBaseContext(), PhoneRegisterActivity.class));
		});
	}

	private void initForgetButton() {
		findViewById(R.id.login_forget_password).setOnClickListener(v -> {
			startActivity(new Intent(getBaseContext(), ForgetPasswordActivity.class));
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
		}
	}

	@Override
	public void onSuccess() {
		DialogManager.with(this).dismissDialog();
		LoginHandler.navigateLoginFlowActivity(this, mPresenter.checkPageNavigation());
		finishAllActivities();
	}

	@Override
	public void onFail(int errorType, String message) {
		DialogManager.with(this).dismissDialog();
		DialogManager.with(this).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onInputFormatCheckFinish(final boolean isPass) {
		runOnUiThread(() -> {
			submitButton.setEnabled(isPass);
			submitButton.setBackgroundResource(isPass ? R.drawable.bg_orange_round_corner : R.drawable.bg_gray_round_corner);
		});
	}

	private void startAccountLogin() {
		DialogManager.with(AccountLoginActivity.this).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		String account = accountEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		mPresenter.onAccountLogin(account, password);
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			mPresenter.checkDataAvailable(accountEditText.getText().toString(), passwordEditText.getText().toString());
		}
	};

}
