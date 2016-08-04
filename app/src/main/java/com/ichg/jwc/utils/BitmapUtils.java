package com.ichg.jwc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.renderscript.RenderScript;

public class BitmapUtils {

	public static RenderScript renderScript;

	public static void init(Context context){
		renderScript = RenderScript.create(context);
	}

	public static Bitmap createCircularBitmap(Bitmap bitmap) {
		return createCircularBitmap(bitmap, 0x25000000, 1);
	}

	public static Bitmap createCircularBitmap(Bitmap bitmap, int strokeColor, int strokeWidth) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - strokeWidth, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		paint.setColor(strokeColor);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		return output;
	}

}
