package com.daemon.demo.touchdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.daemon.demo.touchdemo.TouchUtil;

/**
 * @author daemon
 * @ClassName: MyLayout
 * @Description: my layout
 * @date 2014年4月22日 下午5:07:16
 */
public class MyLayout extends FrameLayout {

    private final static String TAG = "MyLayout";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean r = super.onInterceptTouchEvent(ev);
//        r = true;
        Log.e(TAG, "onInterceptTouchEvent:" + r + " action:" + TouchUtil.actionToString(ev.getAction()));
        return r;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean r = super.onTouchEvent(event);
//        r = true;
        Log.e(TAG, "onTouchEvent:" + r + " action:" + TouchUtil.actionToString(event.getAction()));
        return r;
    }

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
