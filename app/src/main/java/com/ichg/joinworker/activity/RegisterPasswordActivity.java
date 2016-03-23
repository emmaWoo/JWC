package com.ichg.joinworker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ichg.joinworker.JoinWorkerApp;
import com.ichg.joinworker.R;
import com.ichg.joinworker.listener.DialogListener;
import com.ichg.joinworker.manager.ToolbarManager;
import com.ichg.joinworker.utils.DialogManager;

public class RegisterPasswordActivity extends ActivityBase {

	private EditText editPassword;
	private EditText editCheckingPassword;
	//private ResetAccountAPI resetAccountAPI;
	private View buttonSubmit;

//	private final APIListener resetAccountAPIListener = new APIListener() {
//		@Override
//		public void onAPIComplete() {
//			String account = editAccount.getText().toString();
//			JoinWorkerApp.accountManager.updateNickname(account);
//			JoinWorkerApp.accountManager.updateUserName(account);
//			ModifyProfileAPI modifyProfileAPI = new ModifyProfileAPI();
//			modifyProfileAPI.setAPIListener(new APIListener() {
//				@Override
//				public void onAPIComplete() {
//					super.onAPIComplete();
//					DialogManager.with(this).dismissDialog();
//					LoginViewNavigateHandler.navigateLoginFlow(RegisterAccountFragment.this, JoinWorkerApp.accountManager.checkLoginNavigation());
//				}
//			});
//			String genderNumber = "0";
//			String description = getActivity().getString(R.string.default_description, account);
//			modifyProfileAPI.start(account, description, "", genderNumber);
//		}
//
//		@Override
//		public void onAPIError(int errorCode, String errorMessage) {
//			DialogManager.with(this).dismissDialog();
//			if (errorCode == ResetAccountAPI.ErrorCode.USERNAME_ALREADY_EXISTS) {
//				DialogManager.with(this).setMessage(R.string.same_username).showAlertDialog();
//			} else if (errorCode == ResetAccountAPI.ErrorCode.USER_NOT_FOUND) {
//				DialogManager.with(this).setMessage(R.string.user_not_found).showAlertDialog();
//			} else {
//				DialogManager.with(this).showAPIErrorDialog(errorCode, errorMessage);
//			}
//		}
//	};

	private final View.OnClickListener submitListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (TextUtils.isEmpty(editPassword.getText().toString())) {
				editPassword.setError(getString(R.string.password_hint));
				return;
			}
			if (TextUtils.isEmpty(editCheckingPassword.getText().toString())) {
				editCheckingPassword.setError(getString(R.string.hint_enter_checking_password));
				return;
			}
			if (!editPassword.getText().toString().equals(editCheckingPassword.getText().toString())) {
				DialogManager.with(RegisterPasswordActivity.this).setMessage(R.string.confirm_password_error).showAlertDialog();
				return;
			}
			startActivity(new Intent(RegisterPasswordActivity.this, ProfileActivity.class));
			finish();
//			DialogManager.with(AccountSettingActivity.this).setListener(new DialogListener() {
//				@Override
//				public void onCancel() {
//					if (resetAccountAPI != null) {
//						resetAccountAPI.cancel();
//					}
//				}
//			}).showProgressingDialog();
//			if (resetAccountAPI != null) {
//				resetAccountAPI.cancel();
//			}
//			resetAccountAPI = new ResetAccountAPI();
//			resetAccountAPI.setAPIListener(resetAccountAPIListener);
//			resetAccountAPI.start(editAccount.getText().toString(), editPassword.getText().toString());
		}
	};

	private final TextView.OnEditorActionListener accountEditorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				editPassword.requestFocus();
				return true;
			}
			return false;
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
			refreshSubmitButton();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register_account);
		initToolbar();
		editPassword = (EditText) findViewById(R.id.edit_password);
		editPassword.setOnEditorActionListener(passwordEditorActionListener);
		editPassword.addTextChangedListener(textWatcher);
		editCheckingPassword = (EditText) findViewById(R.id.edit_checking_password);
		editCheckingPassword.setOnEditorActionListener(passwordCheckingEditorActionListener);
		editCheckingPassword.addTextChangedListener(textWatcher);
		buttonSubmit = findViewById(R.id.button_submit);
		buttonSubmit.setOnClickListener(submitListener);
		refreshSubmitButton();
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_register)
				.backNavigation(v -> onBackPressed());
	}

	private void refreshSubmitButton() {
		buttonSubmit.setEnabled(!TextUtils.isEmpty(editPassword.getText().toString()) && !TextUtils.isEmpty(editCheckingPassword.getText().toString()));
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
//		if (resetAccountAPI != null) {
//			resetAccountAPI.cancel();
//		}
	}
}
