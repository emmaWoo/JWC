package com.ichg.service.object;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchInfo implements Serializable {

    public String id;
    public String typeId;
    public String keyword;
    public long workingDate;
    public String workingTimeFrom;
    public String workingTimeTo;
    public String city;
    public String district;
    public String address;
    public String content;
    public String payType; // H|D
    public String startMoney;
    public String endMoney;
    public int payAmount;
    public String status; //階段狀態:P 發佈狀態 | A 我有空[等待邀請中] | I 已邀請[可回覆邀請] | W 已確認[等待]


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



}
