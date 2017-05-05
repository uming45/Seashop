package com.seatel.im.ui.activity.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.seatel.im.R;
import com.seatel.im.model.MucInfo;

public class MucListActivity extends AppCompatActivity implements IMucListView {

    private RecyclerView mMucRv;

    private MucListPresenter mMucListPresenter;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc);

        initView();

        mMucListPresenter.loadData("fd25539ecab3454ea551d893932d2200");
    }

    private void initView() {
        mMucRv = (RecyclerView) this.findViewById(R.id.muc_list_rv);
        mPb = (ProgressBar) this.findViewById(R.id.pb);

        mMucRv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mMucRv.setLayoutManager(llm);
        mMucRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mMucListPresenter = new MucListPresenter(this);
    }

    @Override
    public void showLoadingView() {
        mPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        mPb.setVisibility(View.GONE);
    }

    @Override
    public void showNetFail() {
        hideLoadingView();
        Toast.makeText(this, "网络失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public RecyclerView getMucListRv() {
        return mMucRv;
    }

    @Override
    public void enterMucRoom(MucInfo mucInfo) {
        Toast.makeText(this, "进入房间：" + mucInfo.getName(), Toast.LENGTH_SHORT).show();
    }
}
