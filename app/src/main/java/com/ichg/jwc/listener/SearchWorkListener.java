package com.ichg.jwc.listener;

import com.ichg.service.object.WorkTypeInfo;

import java.util.ArrayList;

public interface SearchWorkListener {

    void onFail(int errorType, String message);

    void onSuccess();

    void onSuccessWorkTypeList(ArrayList<WorkTypeInfo> workTypeInfoList);

}
