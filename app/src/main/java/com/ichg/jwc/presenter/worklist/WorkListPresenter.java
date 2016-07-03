package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.WorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkListApi;
import com.ichg.service.api.worklist.WorkFollowApi;
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

    public void getWorkList(int id, int time, String city) {
        mApiFacade.request(new GetWorkListApi(id, time, city)
                .success(this::onGetWorkListInfoSuccess)
                .fail(workListListener::onFail), this);
    }

    public void addWorkFollow(WorkListInfo workListInfo) {
        mApiFacade.request(new WorkFollowApi(Integer.parseInt(workListInfo.id))
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

