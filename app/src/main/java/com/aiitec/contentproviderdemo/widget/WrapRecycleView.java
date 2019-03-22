package com.aiitec.contentproviderdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author ailibin
 * @description 解决scrollView嵌套recycleView的冲突问题
 * @time 2018/09/07
 */
public class WrapRecycleView extends RecyclerView {
    public WrapRecycleView(Context context) {
        super(context);
    }

    public WrapRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float fx = ev.getX();
                float fy = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
