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
import com.ichg.jwc.presenter.ForgetPasswordPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.utils.IDUtils;

public class ForgetPasswordActivity extends ActivityBase implements PresenterListener {

	private EditText editId;
	private EditText editPhoneNo;
	private View buttonSubmit;

	private ForgetPasswordPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_forget_password);
		initPresenter();
		initToolbar();
		initEditText();
		initSubmit();
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.forget_password)
				.backNavigation(v -> onBackPressed());
	}

	private void initPresenter() {
		presenter = new ForgetPasswordPresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
	}

	private void initEditText() {
		editId = (EditText) findViewById(R.id.edit_id);
		editId.setOnEditorActionListener(idEditorActionListener);
		editId.addTextChangedListener(textWatcher);
		editPhoneNo = (EditText) findViewById(R.id.edit_phone_no);
		editPhoneNo.setOnEditorActionListener(phoneNoEditorActionListener);
		editPhoneNo.addTextChangedListener(textWatcher);
	}

	private void initSubmit() {
		buttonSubmit = findViewById(R.id.button_submit);
		buttonSubmit.setEnabled(false);
		buttonSubmit.setOnClickListener(submitListener);
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
		finish();
	}

	@Override
	public void onInputFormatCheckFinish(boolean isPass) {
		buttonSubmit.setEnabled(isPass);
		buttonSubmit.setBackgroundResource(isPass ? R.drawable.bg_orange_round_corner : R.drawable.bg_gray_round_corner);
	}

	private final View.OnClickListener submitListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!IDUtils.checkID(editId.getText().toString())) {
				editId.setError(getString(R.string.id_error));
				editId.requestFocus();
				return;
			}
			DialogManager.with(ForgetPasswordActivity.this).setListener(new DialogListener() {
				@Override
				public void onCancel() {
					presenter.cancel();
				}
			}).showProgressingDialog();
			presenter.forgetPassword(editId.getText().toString(), editId.getText().toString());
		}
	};

	private final TextView.OnEditorActionListener idEditorActionListener = (v, actionId, event) -> {
		if (actionId == EditorInfo.IME_ACTION_NEXT) {
			editPhoneNo.requestFocus();
			return true;
		}
		return false;
	};

	private final TextView.OnEditorActionListener phoneNoEditorActionListener = new TextView.OnEditorActionListener() {
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
			presenter.checkDataAvailable(editId.getText().toString(), editPhoneNo.getText().toString());
		}
	};
}
