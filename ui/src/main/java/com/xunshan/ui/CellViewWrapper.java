package com.xunshan.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by eldorado on 17-5-5.
 *
 * 通用的列表Cell封装
 */
public class CellViewWrapper extends RecyclerView.ViewHolder {
    private final static String TAG = CellViewWrapper.class.getSimpleName();

    public CellViewWrapper(View itemView) {
        super(itemView);
    }
}
