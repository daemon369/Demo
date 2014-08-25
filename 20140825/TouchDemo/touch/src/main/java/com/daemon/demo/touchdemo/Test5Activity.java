package com.daemon.demo.touchdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.daemon.demo.touchdemo.widget.MyView;


public class Test5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5);

        MyView view = (MyView) findViewById(R.id.my_view);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret;
                ret = false;
//                ret = true;
                Log.e("Test5Activity", "onTouch:" + ret + " action:" + TouchUtil.actionToString(event.getAction()));
                return ret;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test5Activity", "onClick");
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean ret;
//                ret = false;
                ret = true;
                Log.e("Test5Activity", "onLongClick:" + ret);
                return ret;
            }
        });
    }
}
