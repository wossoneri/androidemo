package com.android.example.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconTextView extends TextView {

	private final String namespace = "http://com.android.example";

	private int resourceId = 0;
	private Bitmap bitmap;

	public IconTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		resourceId = attrs.getAttributeResourceValue(namespace, "iconSrc", 0);
		if (resourceId > 0)
			bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			Rect src = new Rect();
			Rect target = new Rect();
			src.left = 0;
			src.top = 0;
			src.right = bitmap.getWidth();
			src.bottom = bitmap.getHeight();
			int textHeight = (int) getTextSize();

			target.left = 0;
			target.top = (int) ((getMeasuredHeight() - getTextSize()) / 2) + 1;
			target.bottom = target.top + textHeight;
			target.right = (int) (textHeight * (bitmap.getWidth() / (float) bitmap.getHeight()));

			canvas.drawBitmap(bitmap, src, target, getPaint());
			canvas.translate(target.right + 2, 0);
		}

		super.onDraw(canvas);
	}

}
