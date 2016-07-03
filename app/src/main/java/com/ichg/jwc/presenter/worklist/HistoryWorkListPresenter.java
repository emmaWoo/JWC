package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.HistoryWorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkHistoryListApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public class HistoryWorkListPresenter {

    private ApiFacade mApiFacade;
    private HistoryWorkListListener historyWorkListListener;
    private ArrayList<WorkListInfo> workListInfoList;

    public HistoryWorkListPresenter(ApiFacade apiFacade, HistoryWorkListListener historyWorkListListener) {
        mApiFacade = apiFacade;
        this.historyWorkListListener = historyWorkListListener;
    }

    public void getWorkList(int id) {
        mApiFacade.request(new GetWorkHistoryListApi(id)
                .success(this::onGetWorkListInfoSuccess)
                .fail(historyWorkListListener::onFail), this);
    }

    private void onGetWorkListInfoSuccess(ArrayList<WorkListInfo> response) {
        workListInfoList = response;
        historyWorkListListener.onSuccess(workListInfoList);
    }


    public void cancel() {
        mApiFacade.cancel(this);
    }
}

