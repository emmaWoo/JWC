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

public class ProfileActivity extends ActivityBase implements ProfileListener {

	private static final int REQUEST_UPDATE_PHOTO_GALLERY = 88;
	private static final int REQUEST_UPDATE_PHOTO_CAMERA = 99;

	private UserInfo userInfo;
	private ProfilePresenter presenter;
	private EditText editTextName;
	private RadioButton buttonNationalsId;
	private EditText editTextId;
	private EditText editTextAddress;
	private EditText editTextEmail;
	private EditText editTextBankCode;
	private EditText editTextBankAccount;
	private TextView buttonGender;
	private Spinner spinnerCity;
	private Spinner spinnerArea;
	private Spinner spinnerYear;
	private Spinner spinnerMonth;
	private Spinner spinnerDay;
	private Spinner spinnerLocation;
	private ImageView avatar;
	private String city;
	private String area;
	private String gender;
	private String birthday;
	private Uri imageUri;
	private ArrayList<String[]> areaList = new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initPresenter();
		initToolbar();
		initInputEditTexts();
		initAddressSpinner();
		initLocationSpinner();
		initBirthdaySpinner();
		initDate();
	}

	private void initPresenter() {
		presenter = new ProfilePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, this);
	}

	private void initToolbar() {
		ToolbarManager toolbarManager = ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
				.title(R.string.account_login)
				.menu(R.menu.profile, item -> {
					clickMenuListener(item);
					return false;
				});
		//toolbarManager.backNavigation(v -> onBackPressed());
	}

	private void clickMenuListener(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.finish:
				checkDate();
				break;
			case R.id.skip:
				skipProfile();
				break;
		}

	}

	private void initInputEditTexts() {
		editTextName = (EditText) findViewById(R.id.edit_name);
		buttonNationalsId = (RadioButton) findViewById(R.id.button_nationals_id);
		editTextId = (EditText) findViewById(R.id.edit_id);
		editTextId.addTextChangedListener(textWatcher);
		editTextAddress = (EditText) findViewById(R.id.edit_address);
		editTextEmail = (EditText) findViewById(R.id.edit_email);
		editTextBankCode = (EditText) findViewById(R.id.edit_bank_code);
		editTextBankAccount = (EditText) findViewById(R.id.edit_bank_accound);
		buttonGender = (TextView) findViewById(R.id.button_gender);
		buttonGender.setEnabled(false);

		avatar = (ImageView) findViewById(R.id.icon_avatar);
		registerForContextMenu(avatar);
		avatar.setOnClickListener(v -> avatar.showContextMenu());
	}

	private void initBirthdaySpinner() {
		spinnerYear = (Spinner) findViewById(R.id.spinner_year);
		spinnerMonth = (Spinner) findViewById(R.id.spinner_month);
		spinnerDay = (Spinner) findViewById(R.id.spinner_day);

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
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinnerYear.setAdapter(yearAdapter);
		monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
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
		dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinnerDay.setAdapter(dayAdapter);
		return dayArray;
	}

	private void initAddressSpinner() {
		String[] cities = getResources().getStringArray(R.array.filter_cities);
		initAreaList();
		spinnerCity = (Spinner) findViewById(R.id.spinner_city);
		spinnerArea = (Spinner) findViewById(R.id.spinner_area);
		spinnerCity.setTag(0);

		ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, cities);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinnerCity.setAdapter(cityAdapter);
		initSpinnerArea((int) spinnerCity.getTag());
		city = cities[(int) spinnerCity.getTag()];

		spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				initSpinnerArea(position);
				city = cities[position];
				area = areaList.get(position)[0];
				spinnerCity.setTag(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				area = areaList.get((int) spinnerCity.getTag())[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void initLocationSpinner() {
		String[] cities = getResources().getStringArray(R.array.filter_cities);
		spinnerLocation = (Spinner) findViewById(R.id.spinner_location);
		ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, cities);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinnerLocation.setAdapter(cityAdapter);
	}

	private void initAreaList() {
		areaList.add(getResources().getStringArray(R.array.filter_taipei));
		areaList.add(getResources().getStringArray(R.array.filter_keelung));
		areaList.add(getResources().getStringArray(R.array.filter_new_taipei));
		areaList.add(getResources().getStringArray(R.array.filter_yilan));
		areaList.add(getResources().getStringArray(R.array.filter_hsinchu));
		areaList.add(getResources().getStringArray(R.array.filter_hsinchu_county));
		areaList.add(getResources().getStringArray(R.array.filter_taoyuan));
		areaList.add(getResources().getStringArray(R.array.filter_miaoli));
		areaList.add(getResources().getStringArray(R.array.filter_taichung));
		areaList.add(getResources().getStringArray(R.array.filter_changhua));
		areaList.add(getResources().getStringArray(R.array.filter_nantou));
		areaList.add(getResources().getStringArray(R.array.filter_chiayi));
		areaList.add(getResources().getStringArray(R.array.filter_chiayi_county));
		areaList.add(getResources().getStringArray(R.array.filter_yunlin));
		areaList.add(getResources().getStringArray(R.array.filter_tainan));
		areaList.add(getResources().getStringArray(R.array.filter_kaohsiung));
		areaList.add(getResources().getStringArray(R.array.filter_penghu));
		areaList.add(getResources().getStringArray(R.array.filter_pingtung));
		areaList.add(getResources().getStringArray(R.array.filter_taitung));
		areaList.add(getResources().getStringArray(R.array.filter_hualien));
		areaList.add(getResources().getStringArray(R.array.filter_kinmen));
		areaList.add(getResources().getStringArray(R.array.filter_lianjiang));
	}

	private void initSpinnerArea(int position) {
		ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner, areaList.get(position));
		areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinnerArea.setAdapter(areaAdapter);
		area = areaList.get(position)[0];
	}

	private void initDate() {
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
		birthday = sdf.format(current);
		buttonNationalsId.setChecked(true);
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
		if (TextUtils.isEmpty(editTextName.getText().toString())) {
			errorMessage += getString(R.string.name_empty);
		}
		if (TextUtils.isEmpty(editTextId.getText().toString())) {
			errorMessage = checkLineBreak(errorMessage, getString(R.string.id_empty));
		}
		if (TextUtils.isEmpty(editTextAddress.getText().toString())) {
			errorMessage = checkLineBreak(errorMessage, getString(R.string.address_empty));
		}
		if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
			errorMessage = checkLineBreak(errorMessage, getString(R.string.email_empty));
		}
		if (buttonNationalsId.isChecked() && !IDUtils.isValidIDorRCNumber(editTextId.getText().toString())) {
			errorMessage = checkLineBreak(errorMessage, getString(R.string.nationals_id_error));
		}
		if (!buttonNationalsId.isChecked() && !IDUtils.isValidIDorRCNumber(editTextId.getText().toString())) {
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
		userInfo.userName = editTextName.getText().toString();
		userInfo.isNationalsId = buttonNationalsId.isChecked();
		userInfo.id = editTextId.getText().toString();
		userInfo.city = city;
		userInfo.area = area;
		userInfo.address = editTextAddress.getText().toString();
		userInfo.email = editTextEmail.getText().toString();
		userInfo.gender = gender;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
			Date date = sdf.parse(birthday);
			userInfo.birthday = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(editTextBankCode.getText().toString()) && !TextUtils.isEmpty(editTextBankAccount.getText().toString())) {
			userInfo.bankCode = editTextBankCode.getText().toString();
			userInfo.bankAccount = editTextBankAccount.getText().toString();
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
		LoginHandler.navigateLoginFlowActivity(this, presenter.checkPageNavigation());
		finishAllActivities();
	}

	@Override
	public void onAvatarUpdateSuccess(Bitmap bitmap) {
		avatar.setImageBitmap(BitmapUtils.createCircularBitmap(bitmap));
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
			gender = IDUtils.getGender(editTextId.getText().toString());
			buttonGender.setText(getGender());
		}
	};

	private String getGender() {
		if (TextUtils.isEmpty(gender)) {
			return "";
		}
		return gender.equals("M") ? getString(R.string.male) : getString(R.string.female);
	}

}
