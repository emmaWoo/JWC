package com.ichg.jwc.activity.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.activity.ProfileViewActivity;
import com.ichg.jwc.activity.password.ModifyPasswordActivity;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.utils.DialogManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.setting_title)
                .backNavigation(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @OnClick({R.id.label_modify_account, R.id.label_modify_password, R.id.label_profile, R.id.label_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.label_modify_account:
                DialogManager.with(this).setMessage(R.string.modify_account_message).showAlertDialog();
                break;
            case R.id.label_modify_password:
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;
            case R.id.label_profile:
                startActivity(new Intent(this, ProfileViewActivity.class));
                break;
            case R.id.label_logout:
                JoinWorkerApp.logout(() -> finish());
                break;
        }
    }
}
