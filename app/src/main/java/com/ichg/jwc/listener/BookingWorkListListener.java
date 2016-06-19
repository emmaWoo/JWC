package com.ichg.jwc.listener;

import com.ichg.service.object.WorkListInfo;

import java.util.ArrayList;

public interface BookingWorkListListener {

    void onFail(int errorType, String message);

    void onSuccess(ArrayList<WorkListInfo> workListInfoList);


}
