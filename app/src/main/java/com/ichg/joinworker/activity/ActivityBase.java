package com.ichg.joinworker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ActivityBase extends AppCompatActivity {
	private final static ArrayList<Activity> activities = new ArrayList<>();
//	private final ArrayList<FragmentBase> activateFragments = new ArrayList<>();
	public boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		checkAppVersion();
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
	public static void finishAllActivities() {
		for (Activity activity : activities) {
			activity.finish();
		}
		activities.clear();
	}

	public static void finishOthersActivities(Activity currentActivity){
		for (Activity activity : activities) {
			if(activity != currentActivity) {
				activity.finish();
			}
		}
		activities.clear();
		activities.add(currentActivity);
	}

//	public void shareProvider(String message) {
//		Intent sendIntent = new Intent();
//		sendIntent.setAction(Intent.ACTION_SEND);
//		sendIntent.putExtra(Intent.EXTRA_TEXT, message);
//		sendIntent.setType("text/plain");
//		startActivity(Intent.createChooser(sendIntent, getString(R.string.reminder)));
//	}

	public boolean hasShareProvider() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");
		List activities = getPackageManager().queryIntentActivities(sendIntent, 0);
		return activities != null && !activities.isEmpty();
	}

//	public void sendMessageToActiveFragments(Bundle arguments) {
//		int fragmentSize = activateFragments.size();
//		if (fragmentSize != 0) {
//			activateFragments.get(activateFragments.size() - 1).onReceiveMessage(arguments);
//		}
//	}

//	public void activateFragment(FragmentBase fragment) {
//		activateFragments.add(fragment);
//	}
//
//	public void deactivateFragment(FragmentBase fragment) {
//		activateFragments.remove(fragment);
//	}
}

