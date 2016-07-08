package com.ichg.service.object;

import android.text.TextUtils;

import java.io.Serializable;

public class BaseSearchInfo implements Serializable {

    public String keyword;
    public int timeOption;
    public String cityOption;

    public void setKeyword(String keyword) {
        if(!TextUtils.isEmpty(keyword)) {
            this.keyword = keyword;
        } else {
            this.keyword = null;
        }
    }

    public void setCityOption(String cityOption) {
        if(!TextUtils.isEmpty(cityOption)) {
            this.cityOption = cityOption;
        } else {
            this.cityOption = null;
        }
    }

    public void setTimeOption(int timeOption) {
        this.timeOption = timeOption;
    }
}
