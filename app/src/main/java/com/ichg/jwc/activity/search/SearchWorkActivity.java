package com.ichg.jwc.activity.search;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;
import com.ichg.jwc.listener.SearchWorkListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.SearchWorkPresenter;
import com.ichg.jwc.utils.CityUtils;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.view.CustomTimePickerDialog;
import com.ichg.service.object.SearchInfo;
import com.ichg.service.object.WorkTypeInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class SearchWorkActivity extends ActivityBase implements SearchWorkListener {

    @Bind(R.id.edit_keyword) EditText editKeyword;
    @Bind(R.id.radio_hourly) RadioButton radioHourly;
    @Bind(R.id.radio_daily_wage) RadioButton radioDailyWage;
    @Bind(R.id.edit_start_money) EditText editStartMoney;
    @Bind(R.id.edit_end_money) EditText editEndMoney;
    @Bind(R.id.spinner_city) Spinner spinnerCity;
    @Bind(R.id.spinner_area) Spinner spinnerArea;
    @Bind(R.id.edit_work_time) Button editWorkTime;
    @Bind(R.id.label_start_time) Button labelStartTime;
    @Bind(R.id.label_end_time) Button labelEndTime;
    @Bind(R.id.spinner_work_type) Spinner spinnerWorkType;

    private SearchWorkPresenter presenter;
    private ArrayList<WorkTypeInfo> workTypeInfoList;
    private String selectWorkTypeId;
    private ArrayList<String> cityList;
    private ArrayList<String> areaList;
    private int[] areaIdArray;
    private String selectCity;
    private String selectArea;
    private String defaultCity;
    private String defaultArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_work);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        initUI();
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.search_title)
                .backNavigation(v -> onBackPressed())
                .menu(R.menu.menu_search, item -> {
                    clear();
                    return false;
                });
    }

    private void clear() {
        editKeyword.setText("");
        radioDailyWage.setChecked(false);
        radioHourly.setChecked(false);
        editStartMoney.setText("");
        editEndMoney.setText("");
        spinnerCity.setSelection(0);
        spinnerArea.setSelection(0);
        editWorkTime.setText("");
        labelStartTime.setText("");
        labelEndTime.setText("");
        spinnerWorkType.setSelection(0);
    }

    private void initPresenter() {
        presenter = new SearchWorkPresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
    }

    private void initUI() {
        DialogManager.with(this).setCancelable(false).showProgressingDialog();
        presenter.getWorkTypeList();
        areaIdArray = CityUtils.getAreaIdArray();
        initSpinnerCity();
        initSpinnerArea(0);
    }

    private void initSpinnerCity() {
        cityList = CityUtils.getCityList(this);
        defaultCity = getString(R.string.search_city_hint);
        cityList.add(0, defaultCity);
        ArrayAdapter cityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cityList);
        spinnerCity.setAdapter(cityAdapter);
        selectCity = cityList.get(0);
    }

    private void initSpinnerArea(int position) {
        areaList = CityUtils.getAreaList(this, areaIdArray[position]);
        defaultArea = getString(R.string.search_area_hint);
        areaList.add(0, defaultArea);
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, areaList);
        areaAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerArea.setAdapter(areaAdapter);
        selectArea = areaList.get(0);
    }

    private void initDate() {
        SearchInfo searchInfo = (SearchInfo) getIntent().getSerializableExtra("search_info");
        editKeyword.setText(searchInfo.title);
        if("D".equals(searchInfo.payType)) {
            radioDailyWage.setChecked(true);
        } else if ("H".equals(searchInfo.payType)) {
            radioHourly.setChecked(true);
        }
        editStartMoney.setText(searchInfo.payAmountFrom);
        editEndMoney.setText(searchInfo.payAmountTo);

        int cityIndex = cityList.indexOf(searchInfo.city);
        if (cityIndex != -1) {
            spinnerCity.setSelection(cityIndex);
            int areaIndex = areaList.indexOf(searchInfo.district);
            if (areaIndex != -1) {
                spinnerArea.postDelayed(() -> spinnerArea.setSelection(areaIndex), 200);
            }
        }

        editWorkTime.setText(searchInfo.getWorkDate());
        labelStartTime.setText(searchInfo.workingTimeFrom);
        labelEndTime.setText(searchInfo.workingTimeTo);

        int typeIndex = 0;
        for (int i = 0 ; i < workTypeInfoList.size(); i++) {
            WorkTypeInfo workTypeInfo = workTypeInfoList.get(i);
            if (workTypeInfo.id.equals(searchInfo.typeId)) {
                typeIndex = i;
                break;
            }
        }
        if (typeIndex != -1) {
            spinnerWorkType.setSelection(typeIndex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.title_background_orange));
        }
    }

    @OnClick(R.id.button_search)
    public void onClickSearch() {
        SearchInfo searchInfo = getSearchInfo();
        Intent intent = new Intent();
        intent.putExtra("search_info", searchInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    private SearchInfo getSearchInfo() {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setTypeId(selectWorkTypeId);
        searchInfo.setTitle(editKeyword.getText().toString());
        searchInfo.setWorkDate(editWorkTime.getText().toString());
        searchInfo.setWorkingTimeFrom(labelStartTime.getText().toString());
        searchInfo.setWorkingTimeTo(labelEndTime.getText().toString());
        if (!defaultCity.equals(selectCity)) {
            searchInfo.setCity(selectCity);
        }
        if (!defaultArea.equals(selectArea)) {
            searchInfo.setDistrict(selectArea);
        }
        if (radioDailyWage.isChecked() || radioHourly.isChecked()) {
            searchInfo.setPayType(radioDailyWage.isChecked() ? "D" : "H");
        }
        searchInfo.setPayAmountFrom(editStartMoney.getText().toString());
        searchInfo.setPayAmountTo(editEndMoney.getText().toString());
        return searchInfo;
    }

    @Override
    public void onFail(int errorType, String message) {
        DialogManager.with(this).showAPIErrorDialog(errorType, message);
    }

    @Override
    public void onSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSuccessWorkTypeList(ArrayList<WorkTypeInfo> workTypeInfoList) {
        DialogManager.with(this).dismissDialog();
        WorkTypeInfo workTypeInfo = new WorkTypeInfo();
        workTypeInfo.id = "-1";
        workTypeInfo.name = getString(R.string.search_work_type_hint);
        workTypeInfoList.add(0, workTypeInfo);
        this.workTypeInfoList = workTypeInfoList;
        String[] workTypes = new String[workTypeInfoList.size()];
        for (int i = 0; i < workTypeInfoList.size() ; i++) {
            workTypes[i] = workTypeInfoList.get(i).name;
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, workTypes);
        spinnerWorkType.setAdapter(arrayAdapter);
        selectWorkTypeId = workTypeInfoList.get(0).id;
        if (getIntent().getSerializableExtra("search_info") != null) {
            initDate();
        }
    }

    @OnItemSelected(R.id.spinner_work_type)
    public void OnItemSelectedWorkType(int position) {
        selectWorkTypeId = workTypeInfoList.get(position).id;
    }

    @OnItemSelected(R.id.spinner_city)
    public void OnItemSelectedCity(int position) {
        if (position != 0) {
            initSpinnerArea(position - 1);
        }
        selectCity = cityList.get(position);
    }

    @OnItemSelected(R.id.spinner_area)
    public void OnItemSelectedArea(int position) {
        selectArea = areaList.get(position);
    }

    @OnClick(R.id.edit_work_time)
    public void OnClickWorkTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            Calendar selectedDay = Calendar.getInstance();
            selectedDay.set(year1, month1, day1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            String workDate = simpleDateFormat.format(selectedDay.getTime());
            editWorkTime.setText(workDate);
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 10000);
        datePickerDialog.show();
    }

    @OnClick(R.id.label_start_time)
    public void OnClickStartTime() {
        int hour = 0;
        int minute = 0;
        if (TextUtils.isEmpty(labelStartTime.getText().toString())) {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                Date date = simpleDateFormat.parse(labelStartTime.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String hour1 = String.format(Locale.getDefault(), "%02d", hourOfDay);
            String minute1 = String.format(Locale.getDefault(), "%02d", minuteOfHour);
            labelStartTime.setText(hour1 + ":" + minute1);
        }, hour, minute, true);
        customTimePickerDialog.setTitle(R.string.app_name);
        customTimePickerDialog.show();
    }

    @OnClick(R.id.label_end_time)
    public void OnClickEndTime() {
        int hour = 0;
        int minute = 0;
        if (TextUtils.isEmpty(labelEndTime.getText().toString())) {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        } else {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                Date date = simpleDateFormat.parse(labelEndTime.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String hour1 = String.format(Locale.getDefault(), "%02d", hourOfDay);
            String minute1 = String.format(Locale.getDefault(), "%02d", minuteOfHour);
            labelEndTime.setText(hour1 + ":" + minute1);
        }, hour, minute, true);
        customTimePickerDialog.setTitle(R.string.app_name);
        customTimePickerDialog.show();
    }

}
