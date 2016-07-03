package com.ichg.jwc.fragment.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.MainActivity;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.adapter.worklist.HistoryWorkListAdapter;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.HistoryWorkListListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.RefreshListViewController;
import com.ichg.jwc.presenter.worklist.HistoryWorkListPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryWorkFragment extends FragmentBase implements HistoryWorkListAdapter.OnItemAdapterListener, HistoryWorkListListener {

	public static String WORK_DETAIL_ID = "work_detail_id";

	@Bind(R.id.recycler_view)
	RecyclerView recyclerView;

	private ArrayList<WorkListInfo> workListInfoList = new ArrayList<>();

	private int startId;
	private boolean isLoading = false;
	private HistoryWorkListPresenter mPresenter;
	private HistoryWorkListAdapter historyWorkListAdapter;

	private RefreshListViewController refreshListViewController;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_toolbar_work_list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		initToolbar(view);
		initPresenter();
		initUI();
		mPresenter.getWorkList(0);
	}

	private void initToolbar(View contentView) {
		Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
		ToolbarManager.init(toolbar).title(R.string.history_work_list);

		((MainActivity) getActivity()).bindToolbarToDrawer(toolbar);
	}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = new HistoryWorkListPresenter(JoinWorkerApp.apiFacade, this);
		}
	}

	private void initUI() {
		historyWorkListAdapter = new HistoryWorkListAdapter(getContext(), workListInfoList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(historyWorkListAdapter);

		refreshListViewController = RefreshListViewController.init(getView())
				.setAdapter(historyWorkListAdapter)
				.onRefresh(this::onRefresh)
				.onLoadMore(this::onLoadMore);
	}

	public void onRefresh() {
		mPresenter.getWorkList(0);
		refreshListViewController.setEnableLoadMore(false);
	}

	public void onLoadMore() {
		if (!isLoading) {
			loadNextPageData();
		}
	}

	private void loadNextPageData() {
		mPresenter.getWorkList(startId);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPresenter.cancel();
		ButterKnife.unbind(this);
	}

	@Override
	public void onClickItem(WorkListInfo workListInfo) {
		if(!workListInfo.isOneMonth()) {
			DialogManager.with(getActivity()).setMessage(R.string.history_over).showAlertDialog();
			return;
		}
		String userName = JoinWorkerApp.preference.getUserName();
		if(TextUtils.isEmpty(userName)) {
			DialogManager.with(getActivity()).setMessage(R.string.profile_message).showAlertDialog();
			return;
		}
		Intent intent = new Intent(getContext(), WorkDetailActivity.class);
		intent.putExtra(WORK_DETAIL_ID, Integer.parseInt(workListInfo.id));
		startActivity(intent);
	}

	@Override
	public void onFail(int errorType, String message) {
		isLoading = false;
		refreshListViewController.setRefreshing(false);
		DialogManager.with(getActivity()).showAPIErrorDialog(errorType, message);
	}

	@Override
	public void onSuccess(ArrayList<WorkListInfo> workListInfoList) {
		if (refreshListViewController.isRefreshing()) {
			this.workListInfoList.clear();
			refreshListViewController.setRefreshing(false);
		}
		if (!workListInfoList.isEmpty()) {
			startId = Integer.parseInt(workListInfoList.get(workListInfoList.size() - 1).id);
		}
		boolean isNeedLoadMore = workListInfoList.size() == JoinWorkerApi.LIMIT_COUNT;
		historyWorkListAdapter.setLoadMoreEnable(isNeedLoadMore);
		isLoading = false;

		this.workListInfoList.addAll(workListInfoList);
		historyWorkListAdapter.notifyDataSetChanged();
	}

}
