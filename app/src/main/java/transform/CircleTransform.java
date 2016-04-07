package transform;

import android.graphics.Bitmap;

import com.ichg.jwc.utils.BitmapUtils;
import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {
	@Override
	public Bitmap transform(Bitmap source) {
		Bitmap bitmap = BitmapUtils.createCircularBitmap(source);
		if (bitmap != source) {
			source.recycle();
		}
		return bitmap;
	}

	@Override
	public String key() {
		return "circle";
	}
}
