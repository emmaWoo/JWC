package com.ichg.jwc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.CheckVersionApi;

import java.util.ArrayList;

public class ActivityBase extends AppCompatActivity {
	private final static ArrayList<Activity> activities = new ArrayList<>();
	public boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		checkAppVersion();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activities.remove(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return false;
	}

	private void checkAppVersion() {
		String verName = "0.0.0";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			verName = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		JoinWorkerApp.apiFacade.request(new CheckVersionApi(verName, new CheckVersionApi.CheckAppVersionListener() {
			@Override
			public void onForcedUpdate(String version) {
				if (!JoinWorkerApp.preference.getVersionNow().equals(version)) {
					DialogManager.with(ActivityBase.this).setMessage(getString(R.string.version_update_message, version)).
							setPositiveText(getString(R.string.version_update_text)).
							setNegativeText(getString(R.string.version_do_not_text)).
							setListener(new DialogListener() {
								@Override
								public void onPositive() {
									startUpdate();
									JoinWorkerApp.preference.setVersionNow(version);
								}

								@Override
								public void onNegative() {
									JoinWorkerApp.preference.setVersionNow(version);
								}

								@Override
								public void onCancel() {
									onNegative();
								}
							}).showYesOrNoDialog();
				}
			}

			@Override
			public void onVersionUpdate(String version) {
				DialogManager.with(ActivityBase.this).setMessage(getString(R.string.force_update_message, version)).setCancelable(false).
						setPositiveText(getString(R.string.force_update_text)).
						setNegativeText(getString(R.string.force_exit_text)).
						setListener(new DialogListener() {
							@Override
							public void onPositive() {
								startUpdate();
							}

							@Override
							public void onNegative() {
								finishAllActivities();
								System.exit(0);
							}

							@Override
							public void onCancel() {
								super.onCancel();
								onNegative();
							}
						}).showYesOrNoDialog();
			}

			private void startUpdate() {
				Intent updateAppIntent;
				try {
					updateAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url,
							getPackageName())));
					startActivity(updateAppIntent);
				} catch (android.content.ActivityNotFoundException e) {
					updateAppIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(getString(R.string.market_web_url, getPackageName())));
					startActivity(updateAppIntent);
				}
			}
		}).success(response -> {}), this);
	}

	public static void finishAllActivities() {
		for (Activity activity : activities) {
			activity.finish();
		}
		activities.clear();
	}

	protected final void showSoftInputFromWindow() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

}

