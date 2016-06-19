package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.WorkDetailListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkDetailApi;
import com.ichg.service.api.worklist.WorkFollowApi;
import com.ichg.service.api.worklist.WorkResponseApi;
import com.ichg.service.api.worklist.WorkUnfollowApi;

public class WorkDetailPresenter {

    private ApiFacade mApiFacade;
    private WorkDetailListener workDetailListener;

    public WorkDetailPresenter(ApiFacade apiFacade, WorkDetailListener workDetailListener) {
        mApiFacade = apiFacade;
        this.workDetailListener = workDetailListener;
    }

    public void getWorkDetail(int id) {
        mApiFacade.request(new GetWorkDetailApi(id)
                .success(workDetailListener::onSuccess)
                .fail(workDetailListener::onFail), this);
    }

    public void sendWorkResponse(int id) {
        mApiFacade.request(new WorkResponseApi(id)
                .success(workDetailListener::onSuccessResponse)
                .fail(workDetailListener::onFail), this);
    }

    public void addWorkFollow(int id) {
        mApiFacade.request(new WorkFollowApi(id)
                .success(workDetailListener::onSuccessFollowStatusChange)
                .fail(workDetailListener::onFail), this);
    }

    public void removeWorkFollow(int id) {
        mApiFacade.request(new WorkUnfollowApi(id)
                .success(workDetailListener::onSuccessFollowStatusChange)
                .fail(workDetailListener::onFail), this);
    }

    public void cancel() {
        mApiFacade.cancel(this);
    }
}

