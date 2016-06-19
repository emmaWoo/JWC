package com.ichg.jwc.presenter.worklist;

import com.ichg.jwc.listener.BookingWorkListListener;
import com.ichg.service.api.base.ApiFacade;
import com.ichg.service.api.worklist.GetWorkDetermineListApi;
import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public class BookingWorkListPresenter {

    private ApiFacade mApiFacade;
    private BookingWorkListListener bookingWorkListListener;
    private ArrayList<WorkListInfo> workListInfoList;

    public BookingWorkListPresenter(ApiFacade apiFacade, BookingWorkListListener bookingWorkListListener) {
        mApiFacade = apiFacade;
        this.bookingWorkListListener = bookingWorkListListener;
    }

    public void getWorkList(int id) {
        mApiFacade.request(new GetWorkDetermineListApi(id)
                .success(this::onGetWorkListInfoSuccess)
                .fail(bookingWorkListListener::onFail), this);
    }

    private void onGetWorkListInfoSuccess(ArrayList<WorkListInfo> response) {
        workListInfoList = response;
        bookingWorkListListener.onSuccess(workListInfoList);
    }

    public void cancel() {
        mApiFacade.cancel(this);
    }
}

