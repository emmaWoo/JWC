package com.ichg.service.object;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkDetailInfo implements Serializable {

    public static String CREATE_WORK = "P";
    public static String RESPONSE_WORK = "A";
    public static String DETERMINE_WORK = "I";
    public static String DONE_WORK = "W";

    public String id;
    public String typeName;
    public String title;
    public long workingDate;
    public String workingTimeFrom;
    public String workingTimeTo;
    public String city;
    public String district;
    public String address;
    public String content;
    public String needPeople;
    public String matchedPeople;
    public String haveMeals;
    public String mealsContent;
    public String payDate;
    public String payType; // H|D
    public int payAmount;
    public String companyName;
    public String companyContactName;
    public String companyContactPhone;
    public String follow; // Y\N
    public String status; //階段狀態:P 發佈狀態 | A 我有空[等待邀請中] | I 已邀請[可回覆邀請] | W 已確認[等待]

    public boolean isDailyWage() {
        return "D".equals(payType);
    }

    public boolean hasMeals() {
        return  "Y".equals(haveMeals);
    }

    public String getWorkDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(workingDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public void setWorkDate(String workDate) {
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

    public boolean isFollow() {
        return "Y".equals(follow);
    }

    public void updateFollow() {
        if ("Y".equals(follow)) {
            follow = "N";
        } else {
            follow = "Y";
        }
    }

    public String getCompanyContactPhone() {
        if (CREATE_WORK.equals(status) || RESPONSE_WORK.equals(status)) {
            return companyContactPhone.substring(0, companyContactPhone.length() - 6) + "******";
        }
        return companyContactPhone;
    }

}
