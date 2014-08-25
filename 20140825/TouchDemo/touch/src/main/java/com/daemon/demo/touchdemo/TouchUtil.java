package com.daemon.demo.touchdemo;

import android.view.MotionEvent;

/**
 * Created by daemon on 14-8-17.
 */
public class TouchUtil {

    public static String actionToString(final int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";

            case MotionEvent.ACTION_UP:
                return "ACTION_UP";

            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";

            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
        }

        return "";
    }
}
