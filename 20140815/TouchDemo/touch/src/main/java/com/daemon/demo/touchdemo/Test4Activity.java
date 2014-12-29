package com.daemon.demo.touchdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;


public class Test4Activity extends Activity {

    private final static String TAG = Test4Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret;
        ret =  super.dispatchTouchEvent(ev);
//        ret = true;
//        ret = false;
        Log.e(TAG, "dispatchTouchEvent:" + TouchUtil.actionToString(ev.getAction()));
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret;
        ret = super.onTouchEvent(event);
//        ret = true;
//        ret = false;
        Log.e(TAG, "onTouchEvent:" + TouchUtil.actionToString(event.getAction()));
        return ret;
    }
}