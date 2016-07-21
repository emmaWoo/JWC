package com.ichg.jwc.utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.fragment.work.WorkListFragment;
import com.ichg.jwc.fragment.work.WorkTabFragment;
import com.ichg.service.utils.Debug;

import java.net.URLDecoder;

public class ProtocolUtils {
	public static final String PROTOCOL = "jwc://";

	public final static String ACTION_PUBLISH = "job/publish";
	public final static String ACTION_INVITE = "job/invite";
	public final static String ACTION_COMPLETED = "job/completed";
	public final static String ACTION_ABSENCE = "job/absence";

	public static void execute(MainActivity activity, String url) {
		String initialData = getInitDate(url);

		String action = formatActionStringFromProtocolUrl(initialData);
		String[] pieces = action.split("/");
		if (initialData.startsWith(PROTOCOL)) {
			actionProtocol(activity, action, pieces);
		}
	}

	private static String getInitDate(String url) {
		String initialData = URLDecoder.decode(url);
		if (initialData.endsWith("/")) {
			initialData = initialData.substring(0, initialData.length() - 1);
		}
		Debug.i("execute Protocol data " + initialData);
		return initialData;
	}

	public static String formatActionStringFromProtocolUrl(String url) {
		// Filter「?target_url=」 for facebook url
		return url.replace(PROTOCOL, "").split("\\?target_url=")[0];
	}

	protected static void actionProtocol(ActivityBase activity, String action, String pieces[]) {
		try {
			if (action.startsWith(ACTION_PUBLISH)) {
				performPublishAction(activity, Integer.valueOf(pieces[2]));
			} else if (action.startsWith(ACTION_INVITE)) {
				performInviteAction(activity);
			} else if (action.startsWith(ACTION_COMPLETED)) {
				performCompletedAction(activity);
			} else if (action.startsWith(ACTION_ABSENCE)) {
				performAbsenceAction(activity);
			}
		} catch (NumberFormatException e) {
			Debug.e("ID does not conform to the format:" + e.getMessage());
		}
	}

	private static void performPublishAction(ActivityBase activity, int workId) {
		String userName = JoinWorkerApp.preference.getUserName();
		if (TextUtils.isEmpty(userName)) {
			DialogManager.with(activity).setMessage(R.string.profile_message).showAlertDialog();
			return;
		}
		MainActivity mainActivity = ((MainActivity)activity);
		Bundle arguments = new Bundle();
		arguments.putInt(WorkTabFragment.INPUT_TAB_ID, R.id.radio_all_work);
		mainActivity.setNavigationItemArguments(MainActivity.NavigationType.WORK_LIST, arguments);
		mainActivity.selectNavigationItem(MainActivity.NavigationType.WORK_LIST);

		Intent intent = new Intent(activity, WorkDetailActivity.class);
		intent.putExtra(WorkListFragment.WORK_DETAIL_ID, workId);
		activity.startActivityForResult(intent, 2);
	}

	private static void performInviteAction(ActivityBase activity) {
		MainActivity mainActivity = ((MainActivity)activity);
		Bundle arguments = new Bundle();
		arguments.putInt(WorkTabFragment.INPUT_TAB_ID, R.id.radio_booking_work);
		mainActivity.setNavigationItemArguments(MainActivity.NavigationType.WORK_LIST, arguments);
		mainActivity.selectNavigationItem(MainActivity.NavigationType.WORK_LIST);
	}

	private static void performCompletedAction(ActivityBase activity) {
		MainActivity mainActivity = ((MainActivity)activity);
		Bundle arguments = new Bundle();
		arguments.putInt(WorkTabFragment.INPUT_TAB_ID, R.id.radio_my_work);
		mainActivity.setNavigationItemArguments(MainActivity.NavigationType.WORK_LIST, arguments);
		mainActivity.selectNavigationItem(MainActivity.NavigationType.WORK_LIST);
	}

	private static void performAbsenceAction(ActivityBase activity) {
		MainActivity mainActivity = ((MainActivity)activity);
		mainActivity.selectNavigationItem(MainActivity.NavigationType.WORK_HISTORY);
	}
}
