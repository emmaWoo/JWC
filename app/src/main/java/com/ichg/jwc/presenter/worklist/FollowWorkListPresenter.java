package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.FollowWorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkFollowListApi;
import com.ichg.service.api.worklist.WorkUnfollowApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public class FollowWorkListPresenter {

    private ApiFacade mApiFacade;
    private FollowWorkListListener followWorkListListener;
    private ArrayList<WorkListInfo> workListInfoList;

    public FollowWorkListPresenter(ApiFacade apiFacade, FollowWorkListListener followWorkListListener) {
        mApiFacade = apiFacade;
        this.followWorkListListener = followWorkListListener;
    }

    public void getWorkList(int id) {
        mApiFacade.request(new GetWorkFollowListApi(id)
                .success(this::onGetWorkListInfoSuccess)
                .fail(followWorkListListener::onFail), this);
    }

    private void onGetWorkListInfoSuccess(ArrayList<WorkListInfo> response) {
        workListInfoList = response;
        followWorkListListener.onSuccess(workListInfoList);
    }

    public void removeWorkFollow(WorkListInfo workListInfo) {
        mApiFacade.request(new WorkUnfollowApi(Integer.parseInt(workListInfo.id))
                .success(response -> followWorkListListener.onSuccessFollowStatusChange(workListInfo))
                .fail(followWorkListListener::onFail), this);
    }

    public void cancel() {
        mApiFacade.cancel(this);
    }
}

