package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.DownWorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkDownListApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public class MyWorkListPresenter {

    private ApiFacade mApiFacade;
    private DownWorkListListener downWorkListListener;
    private ArrayList<WorkListInfo> workListInfoList;

    public MyWorkListPresenter(ApiFacade apiFacade, DownWorkListListener downWorkListListener) {
        mApiFacade = apiFacade;
        this.downWorkListListener = downWorkListListener;
    }

    public void getWorkList(int id) {
        mApiFacade.request(new GetWorkDownListApi(id)
                .success(this::onGetWorkListInfoSuccess)
                .fail(downWorkListListener::onFail), this);
    }

    private void onGetWorkListInfoSuccess(ArrayList<WorkListInfo> response) {
        workListInfoList = response;
        downWorkListListener.onSuccess(workListInfoList);
    }

    public void cancel() {
        mApiFacade.cancel(this);
    }
}

