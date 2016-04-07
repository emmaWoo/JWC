package com.ichg.jwc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.manager.ToolbarManager;

public class WorkListFragment extends FragmentBase {


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_work_list, container, false);
		initToolbar(contentView);
		return contentView;
	}

	private void initToolbar(View contentView) {
		Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
		ToolbarManager.init(toolbar)
				.title(R.string.work_list)
				.backNavigation(v -> onNavigationClick())
				.menu(R.menu.main, v -> {

					return false;
				});

		((MainActivity) getActivity()).bindToolbarToDrawer(toolbar);
	}

}
