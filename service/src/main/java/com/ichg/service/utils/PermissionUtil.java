package com.ichg.service.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

	public static final int DEFAULT_PERMISSION_REQUEST_CODE = 32768;

	public static boolean isPermissionDenied(Context context, List<String> permissions) {
		return isPermissionDenied(context, permissions.toArray(new String[permissions.size()]), null);
	}

	public static boolean isPermissionDenied(Context context, List<String> permissions, List<String> deniedPermissions) {
		return isPermissionDenied(context, permissions.toArray(new String[permissions.size()]), deniedPermissions);
	}

	public static boolean isPermissionDenied(Context context, String... permissions) {
		return isPermissionDenied(context, permissions, null);
	}

	@SuppressWarnings("NewApi")
	public static boolean isPermissionDenied(Context context, String[] permissions, List<String> deniedPermissions) {
		if (needToCheckPermission()) {
			if (deniedPermissions == null) {
				deniedPermissions = new ArrayList<>();
			}
			for (int i = 0, size = permissions.length; i < size; i++) {
				String permission = permissions[i];
				int permissionStatus = context.checkSelfPermission(permissions[i]);
				if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
					deniedPermissions.add(permission);
				}
			}
			return !deniedPermissions.isEmpty();
		}
		return true;
	}

	public static void requestPermission(Activity activity, String... permissions) {
		requestPermission(activity, DEFAULT_PERMISSION_REQUEST_CODE, permissions);
	}

	public static void requestPermission(Activity activity, List<String> permissions) {
		requestPermission(activity, DEFAULT_PERMISSION_REQUEST_CODE, permissions);
	}

	public static void requestPermission(Activity activity, int requestCode, List<String> permissions) {
		requestPermission(activity, requestCode, permissions.toArray(new String[permissions.size()]));
	}

	@SuppressWarnings("NewApi")
	public static void requestPermission(Activity activity, int requestCode, String... permissions) {
		if (needToCheckPermission()) {
			activity.requestPermissions(permissions, requestCode);
		}
	}

	public static boolean needToCheckPermission(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

}
