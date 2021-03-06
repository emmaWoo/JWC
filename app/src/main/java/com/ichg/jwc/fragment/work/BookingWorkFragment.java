package com.ichg.jwc.fragment.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.adapter.worklist.BookingWorkListAdapter;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.BookingWorkListListener;
import com.ichg.jwc.presenter.RefreshListViewController;
import com.ichg.jwc.presenter.worklist.BookingWorkListPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookingWorkFragment extends FragmentBase implements BookingWorkListAdapter.OnItemAdapterListener, BookingWorkListListener {

		public static String WORK_DETAIL_ID = "work_detail_id";

		@Bind(R.id.recycler_view)
		RecyclerView recyclerView;

		private ArrayList<WorkListInfo> workListInfoList = new ArrayList<>();

		private int startId;
		private boolean isLoading = false;
		private BookingWorkListPresenter mPresenter;
		private BookingWorkListAdapter bookingWorkListAdapter;

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
			workListInfoList.clear();
			mPresenter.getWorkList(0);
		}

	private void initPresenter() {
		if (mPresenter == null) {
			mPresenter = new BookingWorkListPresenter(JoinWorkerApp.apiFacade, this);
		}
	}

	private void initUI() {
		bookingWorkListAdapter = new BookingWorkListAdapter(getContext(), workListInfoList, this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(bookingWorkListAdapter);

		refreshListViewController = RefreshListViewController.init(getView())
				.setAdapter(bookingWorkListAdapter)
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1) {
			if(resultCode == ActivityBase.RESULT_OK && data != null) {
				String workId = data.getStringExtra("work_id");
				for (WorkListInfo workListInfo : workListInfoList) {
					if (workListInfo.id.equals(workId)) {
						workListInfoList.remove(workListInfo);
						bookingWorkListAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPresenter.cancel();
		ButterKnife.unbind(this);
	}

	@Override
	public void onClickItem(int workListId) {
		String userName = JoinWorkerApp.preference.getUserName();
		if(TextUtils.isEmpty(userName)) {
			DialogManager.with(getActivity()).setMessage(R.string.profile_message).showAlertDialog();
			return;
		}
		Intent intent = new Intent(getContext(), WorkDetailActivity.class);
		intent.putExtra(WORK_DETAIL_ID, workListId);
		startActivityForResult(intent, 1);
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
		bookingWorkListAdapter.setLoadMoreEnable(isNeedLoadMore);
		isLoading = false;

		this.workListInfoList.addAll(workListInfoList);
		bookingWorkListAdapter.notifyDataSetChanged();
	}

}
