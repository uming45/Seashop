package com.seatel.im.ui.activity.list;

import android.support.v7.widget.RecyclerView;

import com.seatel.im.model.MucInfo;


/**
 * Created by eldorado on 17-5-5.
 *
 * 视图层接口
 */
public interface IMucListView {
    /**
     * 正在加载中
     */
    void showLoadingView();

    /**
     * 加载成功并完成
     */
    void hideLoadingView();

    /**
     * 网络失败
     */
    void showNetFail();

    /**
     * 获取RecyclerView对象
     *
     * @return RecyclerView对象
     */
    RecyclerView getMucListRv();

    /**
     * 进入房间聊天
     *
     * @param mucInfo 房间信息
     */
    void enterMucRoom(MucInfo mucInfo);
}
