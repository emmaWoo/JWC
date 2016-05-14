package com.ichg.jwc.fragment.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.manager.ToolbarManager;

public class MyWorkFragment extends FragmentBase {


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_my_work, container, false);
		return contentView;
	}

}
