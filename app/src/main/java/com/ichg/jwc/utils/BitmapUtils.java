package com.ichg.jwc.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

public class BitmapUtils {

	public static RenderScript renderScript;


	public static void init(Context context){
		renderScript = RenderScript.create(context);
	}

	public static Bitmap resizeBitmap(Bitmap bitmap) {
		int oldWidth = bitmap.getWidth();
		int oldHeight = bitmap.getHeight();
		Log.i("Alex", "SettingManager====>bmp size is :" + oldWidth + "," + oldHeight);
		float scaleWidth = 51 / (float) oldWidth;
		float scaleHeight = 51 / (float) oldHeight;
		Log.i("Alex", "SettingManager====> set scale value : " + scaleWidth + ":" + scaleHeight);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true);
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static Bitmap blurBitmap(Bitmap sentBitmap, float radius){
		final Allocation input = Allocation.createFromBitmap(renderScript, sentBitmap); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
		final Allocation output = Allocation.createTyped(renderScript, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(sentBitmap);
		return sentBitmap;
	}

	public static Bitmap createBlurBitmap(Bitmap sentBitmap, int radius) {
		try {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
			if (radius < 1) {
				return sentBitmap;
			}
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			int[] pix = new int[w * h];
			bitmap.getPixels(pix, 0, w, 0, 0, w, h);
			int wm = w - 1;
			int hm = h - 1;
			int wh = w * h;
			int div = radius + radius + 1;
			int r[] = new int[wh];
			int g[] = new int[wh];
			int b[] = new int[wh];
			int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
			int vmin[] = new int[Math.max(w, h)];
			int divsum = (div + 1) >> 1;
			divsum *= divsum;
			int dv[] = new int[256 * divsum];
			for (i = 0; i < 256 * divsum; i++) {
				dv[i] = (i / divsum);
			}
			yw = yi = 0;
			int[][] stack = new int[div][3];
			int stackpointer;
			int stackstart;
			int[] sir;
			int rbs;
			int r1 = radius + 1;
			int routsum, goutsum, boutsum;
			int rinsum, ginsum, binsum;
			for (y = 0; y < h; y++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				for (i = -radius; i <= radius; i++) {
					p = pix[yi + Math.min(wm, Math.max(i, 0))];
					sir = stack[i + radius];
					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					rbs = r1 - Math.abs(i);
					rsum += sir[0] * rbs;
					gsum += sir[1] * rbs;
					bsum += sir[2] * rbs;
					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}
				}
				stackpointer = radius;
				for (x = 0; x < w; x++) {
					r[yi] = dv[rsum];
					g[yi] = dv[gsum];
					b[yi] = dv[bsum];
					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;
					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];
					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];
					if (y == 0) {
						vmin[x] = Math.min(x + radius + 1, wm);
					}
					p = pix[yw + vmin[x]];
					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;
					stackpointer = (stackpointer + 1) % div;
					sir = stack[(stackpointer) % div];
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];
					yi++;
				}
				yw += w;
			}
			for (x = 0; x < w; x++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				yp = -radius * w;
				for (i = -radius; i <= radius; i++) {
					yi = Math.max(0, yp) + x;
					sir = stack[i + radius];
					sir[0] = r[yi];
					sir[1] = g[yi];
					sir[2] = b[yi];
					rbs = r1 - Math.abs(i);
					rsum += r[yi] * rbs;
					gsum += g[yi] * rbs;
					bsum += b[yi] * rbs;
					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}
					if (i < hm) {
						yp += w;
					}
				}
				yi = x;
				stackpointer = radius;
				for (y = 0; y < h; y++) {
					// Preserve alpha channel: ( 0xff000000 & pix[yi] )
					pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;
					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];
					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];
					if (x == 0) {
						vmin[y] = Math.min(y + r1, hm) * w;
					}
					p = x + vmin[y];
					sir[0] = r[p];
					sir[1] = g[p];
					sir[2] = b[p];
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;
					stackpointer = (stackpointer + 1) % div;
					sir = stack[stackpointer];
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];
					yi += w;
				}
			}
			bitmap.setPixels(pix, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (Exception e) {
			return sentBitmap;
		}
	}
}
