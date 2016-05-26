package com.ichg.jwc.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ichg.jwc.R;
import com.ichg.jwc.manager.ToolbarManager;

public class WorkDetailActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_detail);
		initToolbar();
	}

	private void initToolbar() {
		ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.work_title)
				.backNavigation(v -> onBackPressed());
	}
}
