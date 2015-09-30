package com.example.du.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wossoneri on 9/30/15.
 */
public class CircleProgressBar extends View{

    private static final int STYLE_STROKE = 0;
    private static final int STYLE_TEXT   = 1;
    private static final int STYLE_FILL   = 2;

    private Paint mPaint;

    private int mBgColor;
    private int mProgressColor;
    private int mTextColor;

    private float mTextSize;
    private float mHalfTextSize;
    private float mBorderWidth;
    private float mHalfBorderWidth;

    private int mMode;
    private boolean isFill;
    private Paint.Style mStyle;

    private int mMaxValue;
    private int mCurrentValue;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        TypedArray mArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        mBgColor = mArray.getColor(R.styleable.CircleProgressBar_bgColor, Color.LTGRAY);
        mProgressColor = mArray.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE);
        mTextColor = mArray.getColor(R.styleable.CircleProgressBar_textColor, Color.YELLOW);

        mTextSize = mArray.getDimension(R.styleable.CircleProgressBar_textSize, getResources().getDimension(R.dimen.progressbar_text_size));
        mHalfTextSize = mTextSize * 0.5f;

        mBorderWidth = mArray.getDimension(R.styleable.CircleProgressBar_borderWidth, getResources().getDimension(R.dimen.progressbar_border_width));
        mHalfBorderWidth = mBorderWidth * 0.5f;

        mMode = mArray.getInteger(R.styleable.CircleProgressBar_mode, STYLE_STROKE);
        isFill = mMode == STYLE_FILL;

        mStyle = isFill ? Paint.Style.FILL : Paint.Style.STROKE;
        mArray.recycle();
    }

    public int getMax() {
        return mMaxValue;
    }

    public void setMax(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public int getValue() {
        return mCurrentValue;
    }

    public synchronized void setValue(int mCurrentValue) {
        mCurrentValue = Math.max(0, mCurrentValue);
        mCurrentValue = Math.min(mMaxValue, mCurrentValue);

        this.mCurrentValue = mCurrentValue;
        postInvalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = (int)(getWidth() * 0.5f);
        int radius = (int)(center - mHalfBorderWidth);

        mPaint.setColor(mBgColor);//大 circle
        mPaint.setStyle(mStyle);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(center, center, radius, mPaint);

        int percent = (int)(mCurrentValue * 100f / mMaxValue);

        if (mMode == STYLE_TEXT){
            mPaint.setStrokeWidth(0);
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);

            float mHalfTextWidth = mPaint.measureText(percent + "%") * 0.5f;
            canvas.drawText(percent + "%", center - mHalfTextWidth, center + mHalfTextSize, mPaint);


        }

        //draw progress
        if (mCurrentValue > 0){
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mProgressColor);

            RectF rct = new RectF(center-radius, center - radius, center+radius, center+radius);
            int angle = (int)(360*percent / 100f);
            mPaint.setStyle(mStyle);
            canvas.drawArc(rct, -90, angle, isFill, mPaint);
        }

    }
}
