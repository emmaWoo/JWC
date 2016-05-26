package com.ichg.jwc.fragment.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.fragment.FragmentBase;

public class WorkListFragment extends FragmentBase {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_work_list, container, false);
		contentView.findViewById(R.id.button_card).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivityBase().startActivity(new Intent(getActivityBase(), WorkDetailActivity.class));
			}
		});
		return contentView;
	}

}
