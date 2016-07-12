package com.ichg.jwc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.ProfileViewListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.ProfileViewPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.object.UserInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileViewActivity extends ActivityBase implements ProfileViewListener {

    @Bind(R.id.icon_avatar) ImageView iconAvatar;
    @Bind(R.id.label_name) TextView labelName;
    @Bind(R.id.label_id) TextView labelId;
    @Bind(R.id.label_address) TextView labelAddress;
    @Bind(R.id.label_location) TextView labelLocation;
    @Bind(R.id.label_email) TextView labelEmail;
    @Bind(R.id.label_bank_code) TextView labelBankCode;
    @Bind(R.id.label_bank_account) TextView labelBankAccount;
    @Bind(R.id.label_gender) TextView labelGender;
    @Bind(R.id.label_birthday) TextView labelBirthday;

    private ProfileViewPresenter presenter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        initPresenter();
        initToolbar();
        initData();
    }

    private void initPresenter() {
        presenter = new ProfileViewPresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.profile_title)
                .backNavigation(v -> onBackPressed())
                .menu(R.menu.menu_profile_view, item -> {
					Intent intent = new Intent(ProfileViewActivity.this, ProfileActivity.class);
					intent.putExtra("user_info", userInfo);
                    intent.putExtra("is_modify", true);
                    startActivityForResult(intent, 1);
                    return false;
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == ActivityBase.RESULT_OK) {
                initData();
            }
        }
    }

    private void initData() {
        DialogManager.with(this).showProgressingDialog();
        presenter.getProfile();
    }

    @Override
    public void onFail(int errorType, String message) {
        DialogManager.with(this).setMessage(message).showAlertDialog();
    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        if (TextUtils.isEmpty(userInfo.userName)) {
            Intent intent = new Intent(ProfileViewActivity.this, ProfileActivity.class);
            intent.putExtra("user_info", userInfo);
            intent.putExtra("is_modify", true);
            startActivity(intent);
            finish();
        }
        this.userInfo = userInfo;
        labelName.setText(userInfo.userName);
        labelId.setText(userInfo.id);
        labelAddress.setText(userInfo.city + userInfo.area + userInfo.address);
        labelLocation.setText(userInfo.idealWorkCity + userInfo.idealWorkArea);
        labelEmail.setText(userInfo.email);
        labelBankCode.setText(userInfo.bankCode);
        labelBankAccount.setText(userInfo.bankAccount);
        if (!TextUtils.isEmpty(userInfo.gender)) {
            labelGender.setText(userInfo.isMan() ? R.string.male : R.string.female);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(userInfo.birthday);
        labelBirthday.setText(simpleDateFormat.format(calendar.getTime()));
        Picasso.with(this).load(JoinWorkerApi.getBaseUrl() + JoinWorkerApp.preference.getAvatarUrl()).placeholder(R.drawable.icon_person_img).into(iconAvatar);
        DialogManager.with(this).dismissDialog();
    }
}
