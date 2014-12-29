package com.daemon.demo.touchdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.daemon.demo.touchdemo.TouchUtil;

/**
 * @author daemon
 * @ClassName: MyLayout2
 * @Description: my layout 2
 * @date 2014年4月22日 下午5:07:16
 */
public class MyLayout2 extends FrameLayout {

    private final static String TAG = "MyLayout2";

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean r = super.onInterceptTouchEvent(ev);
//        r = true;
        Log.e(TAG, "onInterceptTouchEvent:" + r + " action:" + TouchUtil.actionToString(ev.getAction()));
        return r;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean r;
        r = super.onTouchEvent(event);
//        r = true;
        Log.e(TAG, "onTouchEvent:" + r + " action:" + TouchUtil.actionToString(event.getAction()));
        return r;
    }

    public MyLayout2(Context context) {
        super(context);
    }

    public MyLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
