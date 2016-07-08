package com.ichg.jwc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.listener.ProfileListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.ProfilePresenter;
import com.ichg.jwc.utils.BitmapUtils;
import com.ichg.jwc.utils.CacheUtils;
import com.ichg.jwc.utils.CityUtils;
import com.ichg.jwc.utils.DateUtils;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.jwc.utils.IDUtils;
import com.ichg.jwc.utils.LoginHandler;
import com.ichg.jwc.utils.crop.Crop;
import com.ichg.service.object.UserInfo;
import com.ichg.service.utils.Debug;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class ProfileActivity extends ActivityBase implements ProfileListener {

    private static final int REQUEST_UPDATE_PHOTO_GALLERY = 88;
    private static final int REQUEST_UPDATE_PHOTO_CAMERA = 99;
    @Bind(R.id.icon_avatar) ImageView iconAvatar;
    @Bind(R.id.edit_name) EditText editName;
    @Bind(R.id.button_nationals_id) RadioButton buttonNationalsId;
    @Bind(R.id.button_foreigner_id) RadioButton buttonForeignerId;
    @Bind(R.id.edit_id) EditText editId;
    @Bind(R.id.spinner_city) Spinner spinnerCity;
    @Bind(R.id.spinner_area) Spinner spinnerArea;
    @Bind(R.id.edit_address) EditText editAddress;
    @Bind(R.id.spinner_ideal_work_city) Spinner spinnerIdealWorkCity;
    @Bind(R.id.spinner_ideal_work_area) Spinner spinnerIdealWorkArea;
    @Bind(R.id.edit_email) EditText editEmail;
    @Bind(R.id.edit_bank_code) EditText editBankCode;
    @Bind(R.id.edit_bank_account) EditText editBankAccount;
    @Bind(R.id.button_gender) TextView buttonGender;
    @Bind(R.id.spinner_year) Spinner spinnerYear;
    @Bind(R.id.spinner_month) Spinner spinnerMonth;
    @Bind(R.id.spinner_day) Spinner spinnerDay;

    private ProfilePresenter presenter;

    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> areaAdapter;
    private ArrayAdapter<String> idealCityAdapter;
    private ArrayAdapter<String> idealAreaAdapter;
    private ArrayList<String> idealCityList;
    private ArrayList<String> idealAreaList;
    private ArrayList<String> cityList;
    private ArrayList<String> areaList;
    private int[] areaIdArray;
    private String selectCity;
    private String selectArea;
    private String selectIdealCity;
    private String selectIdealArea;
    private String gender;
    private String birthday;
    private Uri imageUri;
    private boolean isModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        isModify = getIntent().getBooleanExtra("is_modify", false);
        areaIdArray = CityUtils.getAreaIdArray();
        initPresenter();
        initToolbar();
        initInputEditTexts();
        initSpinnerCity();
        initSpinnerArea(0);
        initSpinnerIdealCity();
        initSpinnerIdealArea(0);
        initBirthdaySpinner();
        initDate();
        if (isModify) {
            initModifyData();
        }
    }

    private void initPresenter() {
        presenter = new ProfilePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
    }

    private void initToolbar() {
        ToolbarManager toolbarManager = ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.account_login)
                .menu(isModify ? R.menu.menu_modify_profile : R.menu.menu_profile, item -> {
                    clickMenuListener(item);
                    return false;
                });
        if (isModify) {
            toolbarManager.backNavigation(v -> onBackPressed());
        }
    }

    private void clickMenuListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
            case R.id.finish:
                checkDate();
                break;
            case R.id.skip:
                skipProfile();
                break;
        }
    }

    private void initInputEditTexts() {
        editId.addTextChangedListener(textWatcher);
        registerForContextMenu(iconAvatar);
        iconAvatar.setOnClickListener(v -> iconAvatar.showContextMenu());
    }

    private void initBirthdaySpinner() {
        Calendar calendar = Calendar.getInstance();
        int newYear = calendar.get(Calendar.YEAR);
        int newMonth = calendar.get(Calendar.MONTH) + 1;
        int newDay = calendar.get(Calendar.DAY_OF_MONTH);
        String[] yearArray = new String[100];
        for (int i = 0; i < 100; i++) {
            yearArray[i] = String.valueOf(newYear - (99 - i));
        }

        String[] monthArray = new String[12];
        for (int i = 0; i < 12; i++) {
            monthArray[i] = String.valueOf(i + 1);
        }
        String[] dayArray = initDay(String.valueOf(newYear), String.valueOf(newMonth));

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, yearArray);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, monthArray);
        yearAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerYear.setAdapter(yearAdapter);
        monthAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerYear.setSelection(99);
        spinnerMonth.setSelection(newMonth - 1);
        spinnerDay.setSelection(Integer.parseInt(dayArray[newDay - 1]));
        birthday = yearArray[0] + "/" + monthArray[newMonth - 1] + "/" + dayArray[newDay - 1];
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int yearPosition = spinnerYear.getSelectedItemPosition();
                int monthPosition = spinnerMonth.getSelectedItemPosition();
                initDay(yearArray[yearPosition], monthArray[monthPosition]);
                birthday = yearArray[yearPosition] + "/" + monthArray[monthPosition] + "/" + dayArray[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int yearPosition = spinnerYear.getSelectedItemPosition();
                int monthPosition = spinnerMonth.getSelectedItemPosition();
                initDay(yearArray[yearPosition], monthArray[monthPosition]);
                birthday = yearArray[yearPosition] + "/" + monthArray[monthPosition] + "/" + dayArray[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int yearPosition = spinnerYear.getSelectedItemPosition();
                int monthPosition = spinnerMonth.getSelectedItemPosition();
                birthday = yearArray[yearPosition] + "/" + monthArray[monthPosition] + "/" + dayArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private String[] initDay(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] dayArray = new String[maxDay];
        for (int i = 0; i < maxDay; i++) {
            dayArray[i] = String.valueOf(i + 1);
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, dayArray);
        dayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDay.setAdapter(dayAdapter);
        return dayArray;
    }

    private void initSpinnerCity() {
        cityList = CityUtils.getCityList(this);
        cityAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, cityList);
        cityAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCity.setAdapter(cityAdapter);
        selectCity = cityList.get(0);
    }

    private void initSpinnerArea(int position) {
        areaList = CityUtils.getAreaList(this, areaIdArray[position]);
        areaAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, areaList);
        areaAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerArea.setAdapter(areaAdapter);
        selectArea = areaList.get(0);
    }

    private void updateSpinnerArea(int position) {
        areaList = CityUtils.getAreaList(this, areaIdArray[position]);
        areaAdapter.clear();
        areaAdapter.addAll(areaList);
        selectArea = areaList.get(0);
    }

    private void initSpinnerIdealCity() {
        idealCityList = CityUtils.getCityList(this);
        idealCityAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, idealCityList);
        idealCityAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerIdealWorkCity.setAdapter(idealCityAdapter);
        selectIdealCity = idealCityList.get(0);
    }

    private void initSpinnerIdealArea(int position) {
        idealAreaList = CityUtils.getAreaList(this, areaIdArray[position]);
        idealAreaAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, idealAreaList);
        idealAreaAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerIdealWorkArea.setAdapter(idealAreaAdapter);
        selectIdealArea = idealAreaList.get(0);
    }

    private void updateSpinnerIdealArea(int position) {
        idealAreaList = CityUtils.getAreaList(this, areaIdArray[position]);
        idealAreaAdapter.clear();
        idealAreaAdapter.addAll(idealAreaList);
        selectIdealArea = idealAreaList.get(0);
    }

    private void initDate() {
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        birthday = sdf.format(current);
        buttonNationalsId.setChecked(true);
    }

    private void initModifyData() {
        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("user_info");
        editName.setText(userInfo.userName);
        if(userInfo.isNationalsId) {
            buttonNationalsId.setChecked(true);
        } else {
            buttonForeignerId.setChecked(true);
        }
        editId.setText(userInfo.id);

        int cityIndex = cityList.indexOf(userInfo.city);
        if (cityIndex != -1) {
            spinnerCity.setSelection(cityIndex);
            areaList = CityUtils.getAreaList(this, areaIdArray[cityIndex]);
            int areaIndex = areaList.indexOf(userInfo.area);
            spinnerArea.postDelayed(() -> spinnerArea.setSelection(areaIndex), 200);
        }

        int idealCityIndex = idealCityList.indexOf(userInfo.idealWorkCity);
        if (idealCityIndex != -1) {
            spinnerIdealWorkCity.setSelection(idealCityIndex);
            idealAreaList = CityUtils.getAreaList(this, areaIdArray[idealCityIndex]);
            int idealAreaIndex = idealAreaList.indexOf(userInfo.idealWorkArea);
            spinnerIdealWorkArea.postDelayed(() -> spinnerIdealWorkArea.setSelection(idealAreaIndex), 200);
        }

        editBankCode.setText(userInfo.bankCode);
        editBankAccount.setText(userInfo.bankAccount);
        editAddress.setText(userInfo.address);
        editEmail.setText(userInfo.email);
        if(!TextUtils.isEmpty(userInfo.gender)) {
            buttonGender.setText(userInfo.isMan() ? R.string.male : R.string.female);
        }
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(userInfo.birthday);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        spinnerYear.setSelection(year - (nowYear - 99));
        spinnerMonth.setSelection(month -1);
        spinnerDay.postDelayed(() -> spinnerDay.setSelection(day - 1), 200);
    }

    private void skipProfile() {
        DialogManager.with(this).setMessage(R.string.skip_profile).setPositiveText(R.string.confirm)
                .setNegativeText(R.string.cancel).setListener(new DialogListener() {
            @Override
            public void onPositive() {
                presenter.skipProfile();
                LoginHandler.navigateLoginFlowActivity(ProfileActivity.this, presenter.checkPageNavigation());
                finishAllActivities();
            }
        }).showYesOrNoDialog();
    }

    private void checkDate() {
        String errorMessage = "";
        if (TextUtils.isEmpty(editName.getText().toString())) {
            errorMessage += getString(R.string.name_empty);
        }
        if (TextUtils.isEmpty(editId.getText().toString())) {
            errorMessage = checkLineBreak(errorMessage, getString(R.string.id_empty));
        }
        if (TextUtils.isEmpty(editAddress.getText().toString())) {
            errorMessage = checkLineBreak(errorMessage, getString(R.string.address_empty));
        }
        if (TextUtils.isEmpty(editEmail.getText().toString())) {
            errorMessage = checkLineBreak(errorMessage, getString(R.string.email_empty));
        }
        if (buttonNationalsId.isChecked() && !IDUtils.isValidIDorRCNumber(editId.getText().toString())) {
            errorMessage = checkLineBreak(errorMessage, getString(R.string.nationals_id_error));
        }
        if (!buttonNationalsId.isChecked() && !IDUtils.isValidIDorRCNumber(editId.getText().toString())) {
            errorMessage = checkLineBreak(errorMessage, getString(R.string.foreigner_id_error));
        }

        if (!TextUtils.isEmpty(errorMessage)) {
            DialogManager.with(this).setMessage(errorMessage).showAlertDialog();
            return;
        }
        UserInfo userInfo = initUserInfo();

        presenter.saveProfile(userInfo);
    }

    private UserInfo initUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.userName = editName.getText().toString();
        userInfo.isNationalsId = buttonNationalsId.isChecked();
        userInfo.id = editId.getText().toString();
        userInfo.city = selectCity;
        userInfo.area = selectArea;
        userInfo.idealWorkCity = selectIdealCity;
        userInfo.idealWorkArea = selectIdealArea;
        userInfo.address = editAddress.getText().toString();
        userInfo.email = editEmail.getText().toString();
        userInfo.gender = gender;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
            Date date = sdf.parse(birthday);
            userInfo.birthday = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(editBankCode.getText().toString()) && !TextUtils.isEmpty(editBankAccount.getText().toString())) {
            userInfo.bankCode = editBankCode.getText().toString();
            userInfo.bankAccount = editBankAccount.getText().toString();
        }
        return userInfo;
    }

    private String checkLineBreak(String errorMessage, String message) {
        if (!TextUtils.isEmpty(errorMessage)) {
            errorMessage += "\n";
        }
        errorMessage += message;
        return errorMessage;
    }

    @Override
    public void onFail(int errorType, String message) {
        DialogManager.with(this).dismissDialog();
        DialogManager.with(this).showAPIErrorDialog(errorType, message);
    }

    @Override
    public void onSuccess() {
        if(isModify) {
            setResult(RESULT_OK);
            finish();
        } else {
            LoginHandler.navigateLoginFlowActivity(this, presenter.checkPageNavigation());
            finishAllActivities();
        }
    }

    @Override
    public void onAvatarUpdateSuccess(Bitmap bitmap) {
        iconAvatar.setImageBitmap(BitmapUtils.createCircularBitmap(bitmap));
    }

    private void uploadPhoto(Bitmap bitmap) {
        DialogManager.with(this).showProgressingDialog();
        presenter.uploadPhoto(bitmap);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_menu_send_photo, menu);
        menu.setHeaderTitle(R.string.avatar_upload_title);

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery_menu:
                Crop.pickImage(this);
                return true;
            case R.id.take_picture_menu:
                Crop.takePicture(this, getOutputFileUri());
                return true;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Crop.REQUEST_CROP:
                Uri fileUri = Crop.getOutput(data);
                try {
                    File imageFile = new File(fileUri.getPath());
                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        if (bitmap != null) {
                            uploadPhoto(bitmap);
                        } else {
                            Toast.makeText(this, R.string.save_failed, Toast.LENGTH_SHORT).show();
                        }
                        if (imageFile.delete()) {
                            Debug.e("Delete file fail: " + imageFile.getPath());
                        }
                    }
                } catch (Exception e) {
                    Debug.e(Log.getStackTraceString(e));
                }
                break;
            case REQUEST_UPDATE_PHOTO_CAMERA:
                //isRequestActivityForUpdate = true;
            case Crop.REQUEST_CAMERA:
                doCrop(imageUri, imageUri);
                break;
            case REQUEST_UPDATE_PHOTO_GALLERY:
                //isRequestActivityForUpdate = true;
            case Crop.REQUEST_PICK:
                if (data != null) {
                    doCrop(data.getData(), getOutputFileUri());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doCrop(Uri source, Uri destination) {
        Crop.of(source, destination).withMaxSize(300, 300).asSquare().start(this);
    }

    private Uri getOutputFileUri() {
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            File file = new File(CacheUtils.getSDCacheDir(this), "join_worker_" + DateUtils.getNowTimeString() + ".jpg");
            if (file.exists()) {
                if (file.delete()) {
                    Debug.e("Delete file fail: " + file.getPath());
                }
            }
            imageUri = Uri.fromFile(file);
        } else {
            imageUri = null;
        }
        return imageUri;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            gender = IDUtils.getGender(editId.getText().toString());
            buttonGender.setText(getGender());
        }
    };

    private String getGender() {
        if (TextUtils.isEmpty(gender)) {
            return "";
        }
        return gender.equals("M") ? getString(R.string.male) : getString(R.string.female);
    }

    @OnItemSelected(R.id.spinner_city)
    public void OnItemSelectedCity(int position) {
        updateSpinnerArea(position);
        selectCity = cityList.get(position);
    }

    @OnItemSelected(R.id.spinner_area)
    public void OnItemSelectedArea(int position) {
        selectArea = areaList.get(position);
    }

    @OnItemSelected(R.id.spinner_ideal_work_city)
    public void OnItemSelectedIdealWorkCity(int position) {
        updateSpinnerIdealArea(position);
        selectIdealCity = idealCityList.get(position);
    }

    @OnItemSelected(R.id.spinner_ideal_work_area)
    public void OnItemSelectedIdealWorkArea(int position) {
        selectIdealArea = idealAreaList.get(position);
    }

}
