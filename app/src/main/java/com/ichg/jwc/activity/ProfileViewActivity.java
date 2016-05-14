package com.ichg.jwc.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.ProfileViewListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.ProfileViewPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.object.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileViewActivity extends ActivityBase implements ProfileViewListener {

	private ProfileViewPresenter presenter;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_view);
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
				.menu(R.menu.profile_view, item -> {
//					Intent intent = new Intent(ProfileViewActivity.this, ProfileActivity.class);
//					intent.putExtra("user_info", userInfo);
//					startActivity(intent);
					return false;
				});
	}

	private void initData(){
		DialogManager.with(this).showProgressingDialog();
		presenter.getProfile();
	}

	@Override
	public void onFail(int errorType, String message) {
		DialogManager.with(this).setMessage(message).showAlertDialog();
	}

	@Override
	public void onSuccess(UserInfo userInfo) {
		this.userInfo = userInfo;
		((TextView)findViewById(R.id.label_name)).setText(userInfo.userName);
		((TextView)findViewById(R.id.label_id)).setText(userInfo.id);
		((TextView)findViewById(R.id.label_address)).setText(userInfo.address);
		((TextView)findViewById(R.id.label_location)).setText(userInfo.location);
		((TextView)findViewById(R.id.label_email)).setText(userInfo.email);
		((TextView)findViewById(R.id.label_bank_code)).setText(userInfo.bankCode);
		((TextView)findViewById(R.id.label_bank_account)).setText(userInfo.bankAccount);
		((TextView)findViewById(R.id.label_gender)).setText(userInfo.gender);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(userInfo.birthday);
		((TextView)findViewById(R.id.label_birthday)).setText(simpleDateFormat.format(calendar.getTime()));
		DialogManager.with(this).dismissDialog();
	}
}
