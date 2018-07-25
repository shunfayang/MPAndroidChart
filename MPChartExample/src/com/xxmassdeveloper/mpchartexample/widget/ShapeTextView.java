package com.xxmassdeveloper.mpchartexample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by yangshunfa on 07/25/2018
 **/
public class ShapeTextView extends TextView {

    private Paint paint;
    private Rect r;
    private int measuredHeight;
    private int measuredWidth;
    private int mShapeWidth = 40;

    public ShapeTextView(Context context) {
        super(context);
        init();
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        mShapeWidth = 40;
        r = new Rect(0, 0,mShapeWidth, mShapeWidth);
    }

    public ShapeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec) + mShapeWidth;
        setMeasuredDimension(measuredWidth , measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(r, paint);
        super.onDraw(canvas);
    }
}
