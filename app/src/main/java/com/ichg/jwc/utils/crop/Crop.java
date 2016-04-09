package com.ichg.jwc.utils.crop;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.ichg.service.utils.Debug;

public class Crop {

	public static final int REQUEST_CROP = 0x1;
	public static final int REQUEST_PICK = 0x2;
	public static final int REQUEST_CAMERA = 0x3;
	public static final int RESULT_ERROR = -0x9;

	interface Extra {
		String ASPECT_X = "aspect_x";
		String ASPECT_Y = "aspect_y";
		String MAX_X = "max_x";
		String MAX_Y = "max_y";
		String ERROR = "error";
	}

	private Intent cropIntent;

	public static Crop of(Uri source, Uri destination) {
		return new Crop(source, destination);
	}

	private Crop(Uri source, Uri destination) {
		cropIntent = new Intent();
		cropIntent.setData(source);
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
	}

	public Crop withAspect(int x, int y) {
		cropIntent.putExtra(Extra.ASPECT_X, x);
		cropIntent.putExtra(Extra.ASPECT_Y, y);
		return this;
	}

	public Crop asSquare() {
		cropIntent.putExtra(Extra.ASPECT_X, 1);
		cropIntent.putExtra(Extra.ASPECT_Y, 1);
		return this;
	}

	public Crop withMaxSize(int width, int height) {
		cropIntent.putExtra(Extra.MAX_X, width);
		cropIntent.putExtra(Extra.MAX_Y, height);
		return this;
	}

	public void start(Activity activity) {
		start(activity, REQUEST_CROP);
	}

	public void start(Activity activity, int requestCode) {
		activity.startActivityForResult(getIntent(activity), requestCode);
	}

	public void start(Context context, Fragment fragment) {
		start(context, fragment, REQUEST_CROP);
	}

	public void start(Context context, android.support.v4.app.Fragment fragment) {
		start(context, fragment, REQUEST_CROP);
	}

	public void start(Context context, Fragment fragment, int requestCode) {
		fragment.startActivityForResult(getIntent(context), requestCode);
	}

	public void start(Context context, android.support.v4.app.Fragment fragment, int requestCode) {
		fragment.startActivityForResult(getIntent(context), requestCode);
	}

	public Intent getIntent(Context context) {
		cropIntent.setClass(context, CropImageActivity.class);
		return cropIntent;
	}

	public static Uri getOutput(Intent result) {
		return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
	}

	public static Throwable getError(Intent result) {
		return (Throwable) result.getSerializableExtra(Extra.ERROR);
	}

	public static void pickImage(Activity activity) {
		pickImage(activity, REQUEST_PICK);
	}

	public static void pickImage(Activity activity, int requestCode) {
		Intent intent = getPickImageIntent();
		try {
			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Debug.e(Log.getStackTraceString(e));
		}
	}

	public static void pickImage(android.support.v4.app.Fragment fragment) {
		pickImage(fragment, REQUEST_PICK);
	}

	public static void pickImage(android.support.v4.app.Fragment fragment, int requestCode) {
		Intent intent = getPickImageIntent();
		try {
			fragment.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Debug.e(Log.getStackTraceString(e));
		}
	}

	private static Intent getPickImageIntent() {
		Intent intent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
		}
		intent.setType("image/*");
		return intent;
	}

	public static void takePicture(Activity activity, Uri destination) {
		takePicture(activity, destination, Crop.REQUEST_CAMERA);
	}

	public static void takePicture(Activity activity, Uri destination, int requestCode) {
		Intent intent = getTakePictureIntent(destination);
		try {
			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Debug.e(Log.getStackTraceString(e));
		}
	}

	public static void takePicture(android.support.v4.app.Fragment fragment, Uri destination) {
		takePicture(fragment, destination, Crop.REQUEST_CAMERA);
	}

	public static void takePicture(android.support.v4.app.Fragment fragment, Uri destination, int requestCode) {
		Intent intent = getTakePictureIntent(destination);
		try {
			fragment.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			Debug.e(Log.getStackTraceString(e));
		}
	}

	private static Intent getTakePictureIntent(Uri destination) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
		return intent;
	}

}
