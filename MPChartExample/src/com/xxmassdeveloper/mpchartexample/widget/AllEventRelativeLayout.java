package com.xxmassdeveloper.mpchartexample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by yangshunfa on 07/26/2018
 **/
public class AllEventRelativeLayout extends RelativeLayout {

    public AllEventRelativeLayout(Context context) {
        super(context);
    }

    public AllEventRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllEventRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int childCount = getChildCount();
        for (int i = 0;i<childCount; i++){
            View view = getChildAt(i);
            view.dispatchTouchEvent(ev);
        }
        return true;
    }
}
