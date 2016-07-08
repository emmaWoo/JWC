package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.WorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkListApi;
import com.ichg.service.api.worklist.WorkFollowApi;
import com.ichg.service.api.worklist.WorkUnfollowApi;
import com.ichg.service.object.BaseSearchInfo;
import com.ichg.service.object.SearchInfo;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public class WorkListPresenter {

    private ApiFacade mApiFacade;
    private WorkListListener workListListener;
    private ArrayList<WorkListInfo> workListInfoList;

    public WorkListPresenter(ApiFacade apiFacade, WorkListListener workListListener) {
        mApiFacade = apiFacade;
        this.workListListener = workListListener;
    }

    public void getWorkList(int id, BaseSearchInfo baseSearchInfo) {
        mApiFacade.request(new GetWorkListApi(id, baseSearchInfo)
                .success(this::onGetWorkListInfoSuccess)
                .fail(workListListener::onFail), this);
    }

    public void getWorkList(int id, SearchInfo searchInfo) {
        mApiFacade.request(new GetWorkListApi(id, searchInfo)
                .success(this::onGetWorkListInfoSuccess)
                .fail(workListListener::onFail), this);
    }

    public void addWorkFollow(WorkListInfo workListInfo) {
        mApiFacade.request(new WorkFollowApi(Integer.parseInt(workListInfo.id))
                .success(response -> workListListener.onSuccessFollowStatusChange(workListInfo))
                .fail(workListListener::onFail), this);
    }

    public void delWorkFollow(WorkListInfo workListInfo) {
        mApiFacade.request(new WorkUnfollowApi(Integer.parseInt(workListInfo.id))
                .success(response -> workListListener.onSuccessFollowStatusChange(workListInfo))
                .fail(workListListener::onFail), this);
    }

    private void onGetWorkListInfoSuccess(ArrayList<WorkListInfo> response) {
        workListInfoList = response;
        workListListener.onSuccess(workListInfoList);
    }

    public void cancel() {
        mApiFacade.cancel(this);
    }
}

