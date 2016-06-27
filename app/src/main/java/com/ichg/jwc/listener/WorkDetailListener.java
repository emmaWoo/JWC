package com.ichg.jwc.listener;

import com.ichg.service.object.WorkDetailInfo;

public interface WorkDetailListener {

    void onFail(int errorType, String message);

    void onSuccess(WorkDetailInfo workDetailInfo);

    void onSuccessResponse(String status);

    void onSuccessDetermine(String status);

    void onSuccessFollowStatusChange(String status);

}
