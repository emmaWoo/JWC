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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.WorkDetailActivity;
import com.ichg.jwc.activity.search.SearchWorkActivity;
import com.ichg.jwc.adapter.worklist.WorkListAdapter;
import com.ichg.jwc.fragment.FragmentBase;
import com.ichg.jwc.listener.WorkListListener;
import com.ichg.jwc.presenter.RefreshListViewController;
import com.ichg.jwc.presenter.worklist.WorkListPresenter;
import com.ichg.jwc.utils.CityUtils;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.api.base.JoinWorkerApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class WorkListFragment extends FragmentBase implements WorkListAdapter.OnItemAdapterListener, WorkListListener {

    public static String WORK_DETAIL_ID = "work_detail_id";

    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.edit_search) EditText editSearch;
    @Bind(R.id.spinner_filter_time) Spinner spinnerFilterTime;
    @Bind(R.id.spinner_filter_city) Spinner spinnerFilterCity;

    private ArrayList<WorkListInfo> workListInfoList = new ArrayList<>();

    private int startId;
    private boolean isLoading = false;
    private WorkListPresenter mPresenter;
    private WorkListAdapter workListAdapter;
    private ArrayList<String> cityList;
    private String[] time;
    private int[] timeValue;
    private String selectCity;
    private int selectTime;
    private String searchAll;

    private RefreshListViewController refreshListViewController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_work_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
        initUI();
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new WorkListPresenter(JoinWorkerApp.apiFacade, this);
        }
    }

    private void initUI() {
        initSpinnerCity();
        initSpinnerTime();
        workListAdapter = new WorkListAdapter(getContext(), workListInfoList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(workListAdapter);

        refreshListViewController = RefreshListViewController.init(getView())
                .setAdapter(workListAdapter)
                .onRefresh(this::onRefresh)
                .onLoadMore(this::onLoadMore);
    }

    private void initSpinnerCity() {
        cityList = CityUtils.getCityList(getActivity());
        searchAll = getString(R.string.search_all);
        cityList.add(0, searchAll);
        ArrayAdapter cityAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, cityList);
        spinnerFilterCity.setAdapter(cityAdapter);
        String city = cityList.get(0);
        selectCity = city.equals(searchAll) ? "" : city;
    }

    private void initSpinnerTime() {
        time = getActivity().getResources().getStringArray(R.array.filter_time);
        timeValue = getActivity().getResources().getIntArray(R.array.filter_time_value);
        ArrayAdapter timeAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, time);
        spinnerFilterTime.setAdapter(timeAdapter);
        selectTime = timeValue[0];
    }

    public void onRefresh() {
        mPresenter.cancel();
        mPresenter.getWorkList(0, selectTime, selectCity);
        refreshListViewController.setEnableLoadMore(false);
    }

    public void onLoadMore() {
        if (!isLoading) {
            loadNextPageData();
        }
    }

    private void loadNextPageData() {
        mPresenter.getWorkList(startId, selectTime, selectCity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.cancel();
        ButterKnife.unbind(this);
    }

    @OnItemSelected(R.id.spinner_filter_city)
    public void OnItemSelectedCity(int position) {
        String city = cityList.get(position);
        selectCity = city.equals(searchAll) ? "" : city;
        this.workListInfoList.clear();
        onRefresh();
    }

    @OnItemSelected(R.id.spinner_filter_time)
    public void OnItemSelectedTime(int position) {
        selectTime = timeValue[position];
        this.workListInfoList.clear();
        onRefresh();
    }

    @Override
    public void onClickItem(int workListId) {
        String userName = JoinWorkerApp.preference.getUserName();
        if (TextUtils.isEmpty(userName)) {
            DialogManager.with(getActivity()).setMessage(R.string.profile_message).showAlertDialog();
            return;
        }
        Intent intent = new Intent(getContext(), WorkDetailActivity.class);
        intent.putExtra(WORK_DETAIL_ID, workListId);
        startActivity(intent);
    }

    @Override
    public void onClickFollow(WorkListInfo workListInfo) {
        mPresenter.addWorkFollow(workListInfo);
    }

    @Override
    public void onSuccessFollowStatusChange(WorkListInfo workListInfo) {
        workListInfo.updateFollow();
        workListAdapter.notifyDataSetChanged();
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
        workListAdapter.setLoadMoreEnable(isNeedLoadMore);
        isLoading = false;

        this.workListInfoList.addAll(workListInfoList);
        workListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.button_search, R.id.button_advanced_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                break;
            case R.id.button_advanced_search:
                startActivity(new Intent(getContext(), SearchWorkActivity.class));
                break;
        }
    }
}
