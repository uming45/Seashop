package com.seatel.im.ui.activity.list;

import android.support.v7.widget.RecyclerView;

import com.seatel.im.model.MucInfo;
import com.seatel.im.model.Result;
import com.seatel.im.service.http.HttpEngine;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eldorado on 17-5-5.
 */
public class MucListPresenter implements IMucListPresenter, MucListAdapter.OnMucCellClick {
    private final static String TAG = MucListPresenter.class.getSimpleName();
    private final RecyclerView mMucListRv;
    private final MucListAdapter mAdapter;

    private IMucListView mMucListView;
    private List<MucInfo> mMucInfos = new ArrayList<>();

    public MucListPresenter(IMucListView mucListView) {
        this.mMucListView = mucListView;
        this.mMucListRv = mucListView.getMucListRv();
        this.mAdapter = new MucListAdapter(mMucInfos);
        this.mAdapter.setOnItemClickListener(this);
        this.mMucListRv.setAdapter(this.mAdapter);
    }

    @Override
    public void loadData(String token) {
        this.mMucListView.showLoadingView();
        HttpEngine.post().getMucInfos(token, 0, 50)
                .enqueue(new Callback<Result<List<MucInfo>>>() {
                    @Override
                    public void onResponse(Call<Result<List<MucInfo>>> call, Response<Result<List<MucInfo>>> response) {
                        Result<List<MucInfo>> result = response.body();
                        if (response.isSuccessful() && result.getResultCode() == 1) {
                            MucListPresenter.this.mMucInfos = result.getData();
                            MucListPresenter.this.mAdapter.setMucInfos(MucListPresenter.this.mMucInfos);
                            MucListPresenter.this.mMucListView.hideLoadingView();
                        } else {
                            MucListPresenter.this.mMucListView.showNetFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<List<MucInfo>>> call, Throwable t) {
                        MucListPresenter.this.mMucListView.showNetFail();
                    }
                });
    }

    @Override
    public void onClick(MucInfo mucInfo) {
        mMucListView.enterMucRoom(mucInfo);
    }
}
