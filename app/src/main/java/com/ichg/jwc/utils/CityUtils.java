package com.ichg.jwc.utils;

import android.content.Context;

import com.ichg.jwc.R;

import java.util.ArrayList;

public class CityUtils {

   public static ArrayList<String> getCityList(Context context) {
       String[] city = context.getResources().getStringArray(R.array.filter_cities);
       ArrayList<String> arrayList = new ArrayList<>();
       for(int i = 0 ; i < city.length ; i ++) {
           arrayList.add(city[i]);
       }
       return arrayList;
   }

    public static int[] getAreaIdArray() {
        int[] areaIdArray = {R.array.filter_taipei, R.array.filter_keelung, R.array.filter_new_taipei, R.array.filter_yilan,
                R.array.filter_hsinchu, R.array.filter_hsinchu_county, R.array.filter_taoyuan, R.array.filter_miaoli,
                R.array.filter_taichung, R.array.filter_changhua, R.array.filter_nantou, R.array.filter_chiayi, R.array.filter_chiayi_county,
                R.array.filter_yunlin, R.array.filter_tainan, R.array.filter_kaohsiung, R.array.filter_penghu, R.array.filter_pingtung,
                R.array.filter_taitung, R.array.filter_hualien, R.array.filter_kinmen, R.array.filter_lianjiang};
        return areaIdArray;
    }

    public static ArrayList<String> getAreaList(Context context, int id) {
        String[] area = context.getResources().getStringArray(id);
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0 ; i < area.length ; i ++) {
            arrayList.add(area[i]);
        }
        return arrayList;
    }

}
