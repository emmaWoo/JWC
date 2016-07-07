package com.ichg.jwc.activity.password;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.ForgetPasswordListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.ForgetPasswordPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.utils.IDUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends ActivityBase implements ForgetPasswordListener {

    @Bind(R.id.edit_id) EditText editId;
    @Bind(R.id.edit_phone_no) EditText editPhoneNo;
    @Bind(R.id.button_submit) Button buttonSubmit;

    private ForgetPasswordPresenter presenter;
    private CountDownTimer countDownTimer;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget_password);
        ButterKnife.bind(this);
        initPresenter();
        initToolbar();
        initEditText();
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
        editId.setOnEditorActionListener(idEditorActionListener);
        editId.addTextChangedListener(textWatcher);
        editPhoneNo.setOnEditorActionListener(phoneNoEditorActionListener);
        editPhoneNo.addTextChangedListener(textWatcher);
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
    public void onResendPasswordSuccess() {
        DialogManager.with(this).dismissDialog();
        Toast.makeText(this, R.string.resend_password, Toast.LENGTH_SHORT).show();
        showResendTimerCode();
    }

    @Override
    public void onSendPasswordSuccess() {
        isFirstTime = false;
        showResendTimerCode();
    }

    public void showResendTimerCode() {
        DialogManager.with(this).dismissDialog();
        resendTimer();
    }

    private void resendTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                editId.setEnabled(false);
                editPhoneNo.setEnabled(false);
                buttonSubmit.setEnabled(false);
                buttonSubmit.setBackgroundResource(R.drawable.bg_gray_round_corner);
                buttonSubmit.setText(getString(R.string.resend) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                buttonSubmit.setText(getString(R.string.resend));
                buttonSubmit.setBackgroundResource(R.drawable.bg_green_round_corner);
                buttonSubmit.setEnabled(true);
                editPhoneNo.setEnabled(true);
                editId.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onInputFormatCheckFinish(boolean isPass) {
        buttonSubmit.setEnabled(isPass);
        buttonSubmit.setBackgroundResource(isPass ? R.drawable.bg_green_round_corner : R.drawable.bg_gray_round_corner);
    }

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

    @OnClick(R.id.button_submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                if (!IDUtils.isValidIDorRCNumber(editId.getText().toString())) {
                    editId.setError(getString(R.string.id_error));
                    editId.requestFocus();
                    return;
                }
                String phoneNumber = editPhoneNo.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    editPhoneNo.setError(this.getString(R.string.phone_number_hint));
                    return;
                }
                if (phoneNumber.length() < 10 || !phoneNumber.startsWith("09")) {
                    editPhoneNo.setError(this.getString(R.string.phone_number_size_hint));
                    return;
                }
                requestVerifyCode();
                break;
        }
    }

    public void requestVerifyCode() {
        DialogManager.with(this).dismissDialog();
        String verifyCodeMessage = getString(R.string.send_verify_code, editPhoneNo.getText().toString());
        DialogManager.with(this).setMessage(verifyCodeMessage).setListener(new DialogListener() {
            @Override
            public void onPositive() {
                super.onPositive();
                DialogManager.with(ForgetPasswordActivity.this).setListener(new DialogListener() {
                    @Override
                    public void onCancel() {
                        presenter.cancel();
                    }
                }).showProgressingDialog();
                presenter.forgetPassword(editId.getText().toString(), editPhoneNo.getText().toString(), isFirstTime);
            }
        }).showYesOrNoDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
