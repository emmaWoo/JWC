package com.ichg.jwc.activity.password;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.PresenterListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.ForgetPasswordModifyPresenter;
import com.ichg.jwc.utils.DialogManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPasswordActivity extends ActivityBase implements PresenterListener {

    @Bind(R.id.edit_password) EditText editPassword;
    @Bind(R.id.edit_new_password) EditText editNewPassword;
    @Bind(R.id.edit_checking_new_password) EditText editCheckingNewPassword;
    @Bind(R.id.button_submit) Button buttonSubmit;

    private ForgetPasswordModifyPresenter presenter;

    private final TextView.OnEditorActionListener passwordEditorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            editNewPassword.requestFocus();
            return true;
        }
        return false;
    };

    private final TextView.OnEditorActionListener newPasswordEditorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            editCheckingNewPassword.requestFocus();
            return true;
        }
        return false;
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
            presenter.checkDataAvailable(editPassword.getText().toString(), editNewPassword.getText().toString(), editCheckingNewPassword.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_modify_password);
        ButterKnife.bind(this);
        initPresenter();
        initToolbar();
        initEditText();
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.modify_password)
                .backNavigation(v -> onBackPressed());
    }

    private void initPresenter() {
        presenter = new ForgetPasswordModifyPresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
    }

    private void initEditText() {
        editPassword.setOnEditorActionListener(passwordEditorActionListener);
        editPassword.addTextChangedListener(textWatcher);
        editNewPassword.setOnEditorActionListener(newPasswordEditorActionListener);
        editNewPassword.addTextChangedListener(textWatcher);
        editCheckingNewPassword.setOnEditorActionListener(passwordCheckingEditorActionListener);
        editCheckingNewPassword.addTextChangedListener(textWatcher);
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
        DialogManager.with(this).setMessage(R.string.modify_password_success).setListener(new DialogListener() {
            @Override
            public void onPositive() {
                finish();
            }
        }).showAlertDialog();
    }

    @Override
    public void onInputFormatCheckFinish(boolean isPass) {
        buttonSubmit.setEnabled(isPass);
        buttonSubmit.setBackgroundResource(isPass ? R.drawable.bg_red_round_corner : R.drawable.bg_gray_round_corner);
    }

    @OnClick(R.id.button_submit)
    public void onClick() {
        String errorMessage = "";
        if (!editNewPassword.getText().toString().equals(editCheckingNewPassword.getText().toString())) {
            errorMessage += getString(R.string.password_error);
        }
        if (!TextUtils.isEmpty(errorMessage)) {
            DialogManager.with(ModifyPasswordActivity.this).setMessage(errorMessage).showAlertDialog();
            return;
        }
        DialogManager.with(ModifyPasswordActivity.this).setListener(new DialogListener() {
            @Override
            public void onCancel() {
                presenter.cancel();
            }
        }).showProgressingDialog();
        presenter.updatePassword(editPassword.getText().toString(), editNewPassword.getText().toString());
    }

}
