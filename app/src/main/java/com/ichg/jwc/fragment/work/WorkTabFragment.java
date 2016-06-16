package com.ichg.jwc.fragment.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.manager.ToolbarManager;

public class WorkTabFragment extends FragmentBase {

	private ToolbarManager toolbarManager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_work_tab, container, false);
		initToolbar(contentView);
		initRadioGroup(contentView);
		return contentView;
	}

	private void initToolbar(View contentView) {
		Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
		toolbarManager = ToolbarManager.init(toolbar);

		((MainActivity) getActivity()).bindToolbarToDrawer(toolbar);
	}

	private void initRadioGroup(View contentView) {
		RadioGroup groupTab = (RadioGroup) contentView.findViewById(R.id.group_tab);
		groupTab.setOnCheckedChangeListener((group, checkedId) -> {
			switch (checkedId) {
				case R.id.radio_all_work:
					toolbarManager.title(R.string.work_list);
					switchToChildFragment(new WorkListFragment(), new Bundle());
					break;
				case R.id.radio_my_work:
					toolbarManager.title(R.string.my_work_list);
					switchToChildFragment(new MyWorkFragment(), new Bundle());
					break;
				case R.id.radio_booking_work:
					toolbarManager.title(R.string.booking_work_list);
					switchToChildFragment(new BookingWorkFragment(), new Bundle());
					break;
				case R.id.radio_following_work:
					toolbarManager.title(R.string.following_work_list);
					switchToChildFragment(new FollowingWorkFragment(), new Bundle());
					break;
			}
		});
		groupTab.check(R.id.radio_all_work);
	}



}
