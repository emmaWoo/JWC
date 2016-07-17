package com.ichg.service.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

	private static final SparseArray<Fragment> requestFragmentMap = new SparseArray<>();
	public static final SparseArray<PermissionGrantedListener> requestGrantedListenerMap = new SparseArray<>();
	public static final SparseArray<PermissionDeniedListener> requestDeniedListenerMap = new SparseArray<>();

	public static void checkRequirePermissionIfDenied(Activity activity, int requestCode, String[] permissions, PermissionGrantedListener listener, PermissionDeniedListener deniedListener) {
		if (PermissionUtil.needToCheckPermission() && PermissionUtil.isPermissionDenied(activity, permissions)) {
			if(requestGrantedListenerMap.get(requestCode) != null){
				Debug.e("Double request");
				return;
			}
			requestGrantedListenerMap.put(requestCode, listener);
			requestDeniedListenerMap.put(requestCode, deniedListener);
			PermissionUtil.requestPermission(activity, requestCode, permissions);
		}else{
			listener.onPermissionGranted();
		}
	}

	public static void checkRequirePermissionIfDenied(Fragment fragment, int requestCode, String[] permissions, PermissionGrantedListener grantedListener, PermissionDeniedListener deniedListener) {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
			grantedListener.onPermissionGranted();
			return;
		}
		Activity activity = fragment.getActivity();
		if(requestFragmentMap.get(requestCode) != null){
			requestFragmentMap.remove(requestCode);
			requestGrantedListenerMap.remove(requestCode);
			requestDeniedListenerMap.remove(requestCode);
			List<String> deniedPermissions = new ArrayList<>();
			if (PermissionUtil.isPermissionDenied(activity, permissions, deniedPermissions)) {
				deniedListener.onPermissionDenied(deniedPermissions);
			}else{
				grantedListener.onPermissionGranted();
			}
			return;
		}
		requestFragmentMap.put(requestCode, fragment);
		if (PermissionUtil.needToCheckPermission() && PermissionUtil.isPermissionDenied(activity, permissions)) {
			if(requestGrantedListenerMap.get(requestCode) != null){
				Debug.i("Double permission request");
				return;
			}
			requestGrantedListenerMap.put(requestCode, grantedListener);
			requestDeniedListenerMap.put(requestCode, deniedListener);
			if(PermissionUtil.isPermissionDenied(activity, permissions)) {
				PermissionUtil.requestPermission(activity, requestCode, permissions);
			}
		}else{
			grantedListener.onPermissionGranted();
		}
	}

	public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if(requestFragmentMap.get(requestCode) != null){
			return;
		}
		onRequestResult(requestCode, permissions, grantResults);
	}

	private static void onRequestResult(int requestCode, String[] permissions, int[] grantResults) {
		PermissionGrantedListener permissionGrantedListener = requestGrantedListenerMap.get(requestCode);
		PermissionDeniedListener permissionDeniedListener = requestDeniedListenerMap.get(requestCode);
		requestGrantedListenerMap.remove(requestCode);
		requestDeniedListenerMap.remove(requestCode);
		List<String> deniedPermissions = new ArrayList<>();
		for (int i = 0, size = permissions.length; i < size; i++) {
			String permission = permissions[i];
			if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
				deniedPermissions.add(permission);
			}
		}
		if (deniedPermissions.size() == 0) {
			permissionGrantedListener.onPermissionGranted();
		} else {
			permissionDeniedListener.onPermissionDenied(deniedPermissions);
		}
	}

	public interface PermissionGrantedListener {
		void onPermissionGranted();
	}

	public interface PermissionDeniedListener {
		void onPermissionDenied(List<String> deniedPermissions);
	}
}
