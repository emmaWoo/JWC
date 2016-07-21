package com.ichg.jwc.fragment.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.manager.ToolbarManager;

import java.util.List;

public class WorkTabFragment extends FragmentBase {

	public static String INPUT_TAB_ID = "1";

	private ToolbarManager toolbarManager;
	private WorkListFragment workListFragment;
	private MyWorkListFragment myWorkListFragment;
	private BookingWorkFragment bookingWorkFragment;
	private FollowingWorkFragment followingWorkFragment;
	private RadioGroup groupTab;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_work_tab, container, false);
		initToolbar(contentView);
		initRadioGroup(contentView);
		if (bundle != null) {
			int tabId = bundle.getInt(INPUT_TAB_ID, 0);
			if (tabId > 0) {
				groupTab.check(tabId);
				bundle.putInt(INPUT_TAB_ID, 0);
			}
		}

		return contentView;
	}

	private void initToolbar(View contentView) {
		Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
		toolbarManager = ToolbarManager.init(toolbar);

		((MainActivity) getActivity()).bindToolbarToDrawer(toolbar);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		List<Fragment> fragmentList = getChildFragmentManager().getFragments();
		for (Fragment fragment : fragmentList) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void initRadioGroup(View contentView) {
		groupTab = (RadioGroup) contentView.findViewById(R.id.group_tab);
		groupTab.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.radio_all_work:
					toolbarManager.title(R.string.work_list);
					if (workListFragment == null) {
						workListFragment = new WorkListFragment();
					}
					switchToChildFragment(workListFragment, null);
					break;
				case R.id.radio_my_work:
					toolbarManager.title(R.string.my_work_list);
					if (myWorkListFragment == null) {
						myWorkListFragment = new MyWorkListFragment();
					}
					switchToChildFragment(myWorkListFragment, null);
					break;
				case R.id.radio_booking_work:
					toolbarManager.title(R.string.booking_work_list);
					if (bookingWorkFragment == null) {
						bookingWorkFragment = new BookingWorkFragment();
					}
					switchToChildFragment(bookingWorkFragment, null);
					break;
				case R.id.radio_following_work:
					toolbarManager.title(R.string.following_work_list);
					if (followingWorkFragment == null) {
						followingWorkFragment = new FollowingWorkFragment();
					}
					switchToChildFragment(followingWorkFragment, null);
					break;
			}
		});
		groupTab.check(R.id.radio_all_work);
	}

}
