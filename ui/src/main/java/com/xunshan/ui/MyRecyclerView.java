package com.xunshan.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by eldorado on 17-5-5.
 *
 * RecyclerView原理探索
 */
public class MyRecyclerView extends RecyclerView {
    private final static String TAG = MyRecyclerView.class.getSimpleName();

    public MyRecyclerView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        Log.d(TAG, "onMeasure: " + widthSpec + ", " +  heightSpec);
        super.onMeasure(widthSpec, heightSpec);
    }
}
