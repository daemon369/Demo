package com.daemon.demo.touchdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.daemon.demo.touchdemo.TouchUtil;

/**
 * @author daemon
 * @ClassName: MyView
 * @Description: my view
 * @date 2014年4月13日 下午11:49:35
 */
public class MyView extends View {

    private final static String TAG = "MyView";

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
//        result = true;
        Log.e(TAG, "onTouchEvent:" + result + " action:" + TouchUtil.actionToString(event.getAction()));
        return result;
    }

}
