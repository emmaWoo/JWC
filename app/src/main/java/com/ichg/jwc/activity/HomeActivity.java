package com.ichg.jwc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.ichg.jwc.R;

public class HomeActivity extends Activity {
	private Handler handler = new Handler();
	private Runnable mainRunnable;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mainRunnable = () -> {
			Intent intent = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		};
		handler.postDelayed(mainRunnable, 1000);
	}

}
