package com.seatel.im.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by eldorado on 17-4-11.
 *
 * 显示网络异常
 */
public class NetFailBar extends TextView {
    private final static String TAG = NetFailBar.class.getSimpleName();
    private boolean mIsShowing = false;

    public NetFailBar(Context context) {
        super(context);
    }

    public NetFailBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void show() {
        if (!mIsShowing) {
            setVisibility(View.VISIBLE);
            mIsShowing = true;
        }
    }

    public void dismiss() {
        if (mIsShowing) {
            setVisibility(View.GONE);
            mIsShowing = false;
        }
    }

    //@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(boolean isConnected) {
        if (isConnected) {
            dismiss();
        } else {
            show();
        }
    }
}
