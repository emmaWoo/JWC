package com.ichg.jwc.fragment.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.adapter.worklist.FollowWorkListAdapter;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.FollowWorkListListener;
import com.ichg.jwc.presenter.RefreshListViewController;
import com.ichg.jwc.presenter.worklist.FollowWorkListPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FollowingWorkFragment extends FragmentBase implements FollowWorkListAdapter.OnItemAdapterListener, FollowWorkListListener {

	public static String WORK_DETAIL_ID = "work_detail_id";

	@Bind(R.id.recycler_view)
	RecyclerView recyclerView;

	private ArrayList<WorkListInfo> workListInfoList = new ArrayList<>();

	private int startId;
	private boolean isLoading = false;
	private FollowWorkListPresenter mPresenter;
	private FollowWorkListAdapter followWorkListAdapter;

	private RefreshListViewController refreshListViewController;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_work_list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		initPresenter();
		initUI();
		mPresenter.getWorkList(0);
	}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = new FollowWorkListPresenter(JoinWorkerApp.apiFacade, this);
		}
	}

	private void initUI() {
		followWorkListAdapter = new FollowWorkListAdapter(getContext(), workListInfoList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(followWorkListAdapter);

		refreshListViewController = RefreshListViewController.init(getView())
				.setAdapter(followWorkListAdapter)
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
	public void onClickItem(int workListId) {
		Intent intent = new Intent(getContext(), WorkDetailActivity.class);
		intent.putExtra(WORK_DETAIL_ID, workListId);
		startActivity(intent);
	}

	@Override
	public void onClickDelete(WorkListInfo workListInfo) {
		DialogManager.with(getActivity()).setMessage(R.string.remove_follow)
				.setPositiveText(R.string.confirm).setNegativeText(R.string.cancel)
				.setListener(new DialogListener() {
					@Override
					public void onPositive() {
						mPresenter.removeWorkFollow(workListInfo);
					}
				}).showYesOrNoDialog();
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
		followWorkListAdapter.setLoadMoreEnable(isNeedLoadMore);
		isLoading = false;

		this.workListInfoList.addAll(workListInfoList);
		followWorkListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSuccessFollowStatusChange(WorkListInfo workListInfo) {
		workListInfoList.remove(workListInfo);
		if (!workListInfoList.isEmpty()) {
			startId = Integer.parseInt(workListInfoList.get(workListInfoList.size() - 1).id);
		}
		followWorkListAdapter.notifyDataSetChanged();
	}

}
