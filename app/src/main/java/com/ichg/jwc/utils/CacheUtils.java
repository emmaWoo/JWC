package com.ichg.jwc.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ichg.service.utils.Debug;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

public class CacheUtils {

//	public static BitmapLruCache bitmapLruCache;
	public static Executor fileCacheExecutor;
	public static Executor loadBitmapExecutor;

//	public static void initBitmapLruCache(Context context) {
//		BitmapLruCache.Builder builder = new BitmapLruCache.Builder(context);
//		builder.setMemoryCacheEnabled(true).setMemoryCacheMaxSizeUsingHeapSize(0.05F);
//		builder.setDiskCacheEnabled(true)
//				.setDiskCacheLocation(new File(getDefaultSDCardPath(context)))
//				.setDiskCacheMaxSize(200 * 1024 * 1024);
//		bitmapLruCache = builder.build();
//		fileCacheExecutor = Executors.newFixedThreadPool(2);
//		loadBitmapExecutor = Executors.newFixedThreadPool(3);
//	}

//	public static void saveBitmapToCache(final String url, final Bitmap bitmap) {
//
//		fileCacheExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				if(bitmapLruCache.contains(url)){
//					return;
//				}
//				bitmapLruCache.put(url, bitmap);
//			}
//		});
//	}

//	public static void removeAllBitmapFromCache(final Context context) {
//		new AsyncTask<Void, Void, Bitmap>() {
//			@Override
//			protected Bitmap doInBackground(Void... params) {
//				File coverDir = new File(getDefaultSDCardPath(context));
//				File[] coverFiles = coverDir.listFiles(new FileFilter() {
//					@Override
//					public boolean accept(File file) {
//						return !file.getName().equals("journal");
//					}
//				});
//				if(coverFiles != null) {
//					for (int i = 0; i < coverFiles.length; i++) {
//						coverFiles[i].delete();
//					}
//				}
//				return null;
//			}
//		}.execute();
//	}

//	public static void loadBitmapFromCache( String url,  Config bitmapConfig,  LoadBitmapFromCacheListener listener) {
//		final String finalUrl = url;
//		final LoadBitmapFromCacheListener finalListener = listener;
//		new AsyncTask<Void, Void, Bitmap>() {
//			boolean isFromMemory;
//
//			@Override
//			protected Bitmap doInBackground(Void... params) {
//				Bitmap output = null;
//				CacheableBitmapDrawable outputDrawable;
//				if (CacheUtils.bitmapLruCache.containsInMemoryCache(finalUrl)) {
//					outputDrawable = CacheUtils.bitmapLruCache.getFromMemoryCache(finalUrl);
//					isFromMemory = true;
//					output = Bitmap.createBitmap(outputDrawable.getBitmap());
////					outputDrawable.getBitmap().recycle();
//				} else if (CacheUtils.bitmapLruCache.containsInDiskCache(finalUrl)) {
//					Options options = new Options();
//					options.inPreferredConfig = Config.RGB_565;
////					options.inPreferredConfig = bitmapConfig;
//					outputDrawable = CacheUtils.bitmapLruCache.getFromDiskCache(finalUrl, options);
//					isFromMemory = false;
//					output = Bitmap.createBitmap(outputDrawable.getBitmap());
////					outputDrawable.getBitmap().recycle();
//				}
//				return output;
//			}
//
//			@Override
//			protected void onPostExecute(final Bitmap bitmap) {
//				if (bitmap != null) {
//					finalListener.onCacheHit(bitmap, isFromMemory);
//				} else {
//					finalListener.onCacheMiss();
//				}
//			}
//		}.executeOnExecutor(loadBitmapExecutor);
//	}

//	public interface LoadBitmapFromCacheListener {
//		void onCacheHit(Bitmap bitmap, boolean isFromMemory);
//
//		void onCacheMiss();
//	}

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

	public static String getDir() {
		File direct = new File(Environment.getExternalStorageDirectory() + "/Joinworker/");
		if (!direct.exists()) {
			direct = new File("/sdcard/Joinworker/");
			if (!direct.mkdirs()) {
				return null;
			}
		}
		return direct.getAbsolutePath();
	}
}
