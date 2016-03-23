package com.ichg.joinworker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ichg.joinworker.JoinWorkerApp;
import com.ichg.joinworker.R;
import com.ichg.joinworker.manager.ToolbarManager;
import com.ichg.joinworker.presenter.ProfilePresenter;
import com.ichg.joinworker.utils.DialogManager;
import com.ichg.service.api.login.LoginApiBase;

public class ProfileActivity extends ActivityBase {

	private ProfilePresenter presenter;
	private EditText editTextName;
	private EditText editTextId;
	private EditText editTextPhone;
	private EditText editTextAddress;
	private EditText editTextEmail;
	private EditText editTextBankCode;
	private EditText editTextBankAccount;
	private TextView buttonGender;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initPresenter();
		initToolbar();
		initInputEditTexts();
		initSubmitButton();
	}

	private void initPresenter() {
		presenter = new ProfilePresenter();
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_login)
				.backNavigation(v -> onBackPressed());
	}

	private void initInputEditTexts() {
		editTextName = (EditText) findViewById(R.id.edit_name);
		editTextId = (EditText) findViewById(R.id.edit_id);
		editTextPhone = (EditText) findViewById(R.id.edit_phone);
		editTextAddress = (EditText) findViewById(R.id.edit_address);
		editTextEmail = (EditText) findViewById(R.id.edit_email);
		editTextBankCode = (EditText) findViewById(R.id.edit_bank_code);
		editTextBankAccount = (EditText) findViewById(R.id.edit_bank_accound);
		buttonGender = (TextView) findViewById(R.id.button_gender);
	}

	private void initSubmitButton() {
		Button buttonSave = (Button) findViewById(R.id.button_save);
		buttonSave.setOnClickListener(v -> {
			if (TextUtils.isEmpty(editTextName.getText().toString())) {
				DialogManager.with(this).setMessage("姓名不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextId.getText().toString())) {
				DialogManager.with(this).setMessage("身份證字號不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextPhone.getText().toString())) {
				DialogManager.with(this).setMessage("電話不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextAddress.getText().toString())) {
				DialogManager.with(this).setMessage("住址不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
				DialogManager.with(this).setMessage("E-mail 不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextBankCode.getText().toString())) {
				DialogManager.with(this).setMessage("銀行代號不得為空").showAlertDialog();
				return;
			} else if (TextUtils.isEmpty(editTextBankAccount.getText().toString())) {
				DialogManager.with(this).setMessage("銀行帳號不得為空").showAlertDialog();
				return;
			}

			LoginApiBase.LoginResultInfo resultInfo = new LoginApiBase.LoginResultInfo();
			resultInfo.accessToken = "token";
			resultInfo.isBindCellphone = true;
			JoinWorkerApp.accountManager.updateLoginStatus(resultInfo);
			startActivity(new Intent(ProfileActivity.this, MainActivity.class));
			finishAllActivities();
		});
	}
}
