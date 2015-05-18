package demo.DataBaseDemo.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.android_database_demo.R;

public class head_portrait_view extends ImageView {

	private Bitmap mBitmap;

	private static final int RADIUS = 50;
	private static final int DIAMETER = RADIUS * 2;
	private static final int CIRCLE_LINE_WIDTH = 2;

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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension((RADIUS + CIRCLE_LINE_WIDTH) * 2, (RADIUS + CIRCLE_LINE_WIDTH) * 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		Drawable drawable = getDrawable();
		if (drawable == null)
			return;

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}

		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		if (null == b) {
			return;
		}

		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		Bitmap bmp = getCircleBmp(bitmap);

		drawCircleBorder(canvas);
		canvas.drawBitmap(bmp, CIRCLE_LINE_WIDTH, CIRCLE_LINE_WIDTH, null);
	}

	private void drawCircleBorder(Canvas canvas) {
		Paint mCircleLinePaint = new Paint();
		mCircleLinePaint.setColor(getResources().getColor(R.color.login_head_border_color));
		mCircleLinePaint.setStrokeWidth(CIRCLE_LINE_WIDTH);
		mCircleLinePaint.setStyle(Paint.Style.STROKE);
		mCircleLinePaint.setStrokeJoin(Paint.Join.ROUND);
		mCircleLinePaint.setAntiAlias(true);
		mCircleLinePaint.setFilterBitmap(true);
		mCircleLinePaint.setDither(true);
		canvas.drawCircle(RADIUS + CIRCLE_LINE_WIDTH, RADIUS + CIRCLE_LINE_WIDTH, RADIUS,
				mCircleLinePaint);

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

	public void longClick() {

	}
}
