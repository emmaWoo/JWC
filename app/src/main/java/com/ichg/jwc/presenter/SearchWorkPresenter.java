package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.SearchWorkListener;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkTypeListApi;
import com.ichg.service.object.SearchInfo;

public class SearchWorkPresenter {

    private ApiFacade mApiFacade;
    private AccountManager mAccountManager;
    private SearchWorkListener searchWorkListener;

    public SearchWorkPresenter(ApiFacade apiFacade, AccountManager accountManager, SearchWorkListener searchWorkListener) {
        mApiFacade = apiFacade;
        mAccountManager = accountManager;
        this.searchWorkListener = searchWorkListener;
    }

    public void SearchWork(SearchInfo searchInfo) {
//        mApiFacade.request(new SearchWorkApi(workDetailInfo)
//                .success(response -> SearchWorkListener.onSuccess())
//                .fail(SearchWorkListener::onFail), this);
    }


    public void getWorkTypeList() {
        mApiFacade.request(new GetWorkTypeListApi()
                .success(response -> searchWorkListener.onSuccessWorkTypeList(response))
                .fail(searchWorkListener::onFail), this);
    }

    public void cancel() {
        mAccountManager.cancelLogin();
    }
}
