package com.ichg.service.object;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchInfo implements Serializable {

    public String title;
    public String typeId;
    public Long workingDate;
    public String workingTimeFrom;
    public String workingTimeTo;
    public String city;
    public String district;
    public String payType; // H|D
    public String payAmountFrom;
    public String payAmountTo;

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            this.title = title;
        } else {
            this.title = null;
        }
    }

    public void setTypeId(String typeId) {
        if (!TextUtils.isEmpty(typeId) && !"-1".equals(typeId)) {
            this.typeId = typeId;
        } else {
            this.typeId = null;
        }
    }

    public void setWorkingTimeFrom(String workingTimeFrom) {
        if (!TextUtils.isEmpty(workingTimeFrom)) {
            this.workingTimeFrom = workingTimeFrom;
        } else {
            this.workingTimeFrom = null;
        }
    }

    public void setWorkingTimeTo(String workingTimeTo) {
        if (!TextUtils.isEmpty(workingTimeTo)) {
            this.workingTimeTo = workingTimeTo;
        } else {
            this.workingTimeTo = null;
        }
    }

    public void setCity(String city) {
        if (!TextUtils.isEmpty(city)) {
            this.city = city;
        } else {
            this.city = null;
        }
    }

    public void setDistrict(String district) {
        if (!TextUtils.isEmpty(district)) {
            this.district = district;
        } else {
            this.district = null;
        }
    }

    public void setPayType(String payType) {
        if (!TextUtils.isEmpty(payType)) {
            this.payType = payType;
        } else {
            this.payType = null;
        }
    }

    public void setPayAmountFrom(String payAmountFrom) {
        if (!TextUtils.isEmpty(payAmountFrom)) {
            this.payAmountFrom = payAmountFrom;
        } else {
            this.payAmountFrom = null;
        }
    }

    public void setPayAmountTo(String payAmountTo) {
        if (!TextUtils.isEmpty(payAmountTo)) {
            this.payAmountTo = payAmountTo;
        } else {
            this.payAmountTo = null;
        }
    }

    public String getWorkDate() {
        if (workingDate == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(workingDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public void setWorkDate(String workDate) {
        if (TextUtils.isEmpty(workDate)) {
            this.workingDate = null;
            return;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Date date = simpleDateFormat.parse(workDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            this.workingDate = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
