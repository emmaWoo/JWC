package com.ichg.joinworker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ichg.joinworker.R;
import com.ichg.joinworker.listener.AccountLoginListener;
import com.ichg.joinworker.listener.DialogListener;
import com.ichg.joinworker.manager.ToolbarManager;
import com.ichg.joinworker.presenter.RegisterPhonePresenter;
import com.ichg.joinworker.utils.DialogManager;

public class RegisterPhoneActivity extends ActivityBase implements AccountLoginListener {

	private RegisterPhonePresenter mPresenter;
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
		initForgetButton();
	}

	@Override
	public void onLoginSuccess() {
		DialogManager.with(this).dismissDialog();
//		LoginViewNavigateHandler.navigateLoginFlowActivity(this, mPresenter.checkPageNavigation());
//		finishAllActivities();
	}

	@Override
	public void onLoginFail(int errorType, String message) {
//		DialogManager.with(this).dismissDialog();
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
	public void onInputFormatCheckFinish(final boolean isPass) {
		runOnUiThread(() -> submitButton.setEnabled(isPass));
	}

	private void initPresenter() {
		if (mPresenter == null) {
			//mPresenter = PresenterFactory.createAccountLoginPresenter(JoinWorkerApp.accountManager, this);
		}
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_login)
				.backNavigation(v -> onBackPressed());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

	private void startAccountLogin() {
		DialogManager.with(RegisterPhoneActivity.this).setListener(new DialogListener() {
			@Override
			public void onCancel() {
				mPresenter.cancel();
			}
		}).showProgressingDialog();
		String account = accountEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		//mPresenter.onAccountLogin(account, password);
		startActivity(new Intent(this, MainActivity.class));
	}

	private void initForgetButton() {
		findViewById(R.id.login_forget_password).setOnClickListener(v -> {
//				Intent intent = new Intent(getBaseContext(), FragmentContentActivity.class);
//				intent.putExtra(FragmentContentActivity.INPUT_STRING_FRAGMENT_NAME, ResetPasswordFragment.class.getName());
//				startActivity(intent);
//				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		});
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
			//mPresenter.checkDataAvailable(accountEditText.getText().toString(), passwordEditText.getText().toString());
		}
	};

}
