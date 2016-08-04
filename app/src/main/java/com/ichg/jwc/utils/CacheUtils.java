package com.ichg.jwc.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ichg.service.utils.Debug;

import java.io.File;
import java.io.IOException;

public class CacheUtils {

	public static String getSDCacheDir(Context context) {
		final String dirPath = getDefaultSDCardDir(context);
		if (dirPath == null) {
			return null;
		}
		final File cacheDir = new File(dirPath + File.separator + "cache");
		if (!cacheDir.exists()) {
			if (!cacheDir.mkdir()) {
				return null;
			}
		}
		return cacheDir.getAbsolutePath();
	}

	public static String getDefaultSDCardDir(Context context) {
		String dirPath = getDefaultSDCardPath(context);
		if (!TextUtils.isEmpty(dirPath)) {
			File file = new File(dirPath);
			if (file.exists() || file.mkdirs()) {
				File dir = new File(dirPath + File.separator + "joinworker");
				if (!dir.exists()) {
					if (!dir.mkdir()) {
						return null;
					}
				}
				return dir.getAbsolutePath();
			}
		}

		File dir = new File(dirPath);
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				return null;
			}
		}
		return dir.getAbsolutePath();
	}

	public static String getDefaultSDCardPath(Context context) {
		if (context != null) {
			if (Build.VERSION.SDK_INT >= 19 && context.getExternalFilesDir(null) != null) {
				return context.getExternalFilesDir(null).getAbsolutePath();
			} else if (Build.VERSION.SDK_INT >= 17) {
				String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
				if (!TextUtils.isEmpty(externalStorageDirectory)) {
					String defaultPath = externalStorageDirectory.replace("emulated/", "sdcard");
					File file = new File(defaultPath);
					if (file.exists()) {
						return defaultPath + File.separator + "Android" + File.separator + "data" + File.separator + context
								.getPackageName()
								+ File.separator + "files";
					} else {
						return externalStorageDirectory + File.separator + "Android" + File.separator + "data" + File.separator + context
								.getPackageName() + File.separator + "files";
					}
				}
			} else {
				try {
					return Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + "Android" + File.separator +
							"data"
							+ File.separator + context.getPackageName() + File.separator + "files";
				} catch (IOException e) {
					Debug.e("getDefaultSDCardPath " + Log.getStackTraceString(e));
				}
			}
		}
		return null;
	}

}
