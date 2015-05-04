package demo.view.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class head_portrait_view extends ImageView {

	private Bitmap mBitmap;

	private static final int RADIUS = 50;
	private static final int DIAMETER = RADIUS * 2;

	// construcor
	public head_portrait_view(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	public head_portrait_view(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public head_portrait_view(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Drawable drawable = getDrawable();
		if (drawable == null)
			return;

		Bitmap b = ((BitmapDrawable)drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
		
		Bitmap bmp = getCircleBmp(bitmap);
		canvas.drawBitmap(bmp, getWidth() / 2 - RADIUS, getHeight() / 2 - RADIUS, null);
	}

	private void init() {

	}

	private Bitmap getCircleBmp(Bitmap srcBmp) {
		Bitmap scaledBmp;

		// 先对传入的正方形图片进行适当缩放
		scaledBmp = Bitmap.createScaledBitmap(srcBmp, DIAMETER, DIAMETER, true);

		Bitmap output = Bitmap.createBitmap(DIAMETER, DIAMETER, Config.ARGB_8888);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		Rect rect = new Rect(0, 0, DIAMETER, DIAMETER);

		Canvas canvas = new Canvas(output);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledBmp.getWidth() / 2, scaledBmp.getHeight() / 2, RADIUS, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledBmp, rect, rect, paint);

		srcBmp = null;
		scaledBmp = null;

		return output;
	}

	public void setBitmap(Bitmap bmp) {
		mBitmap = bmp;
		invalidate();
	}
}
