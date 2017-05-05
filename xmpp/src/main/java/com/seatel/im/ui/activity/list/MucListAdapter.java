package com.seatel.im.ui.activity.list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seatel.im.R;
import com.seatel.im.model.MucInfo;

import java.util.List;

/**
 * Created by eldorado on 17-5-5.
 *
 * 群组列表的适配器
 * 1. 在这里适配器主要关心的是视图item的数据填充
 */
public class MucListAdapter extends RecyclerView.Adapter<MucListAdapter.MucViewWrapper> {
    private final static String TAG = MucListAdapter.class.getSimpleName();
    private List<MucInfo> mMucInfos;

    private OnMucCellClick mOnMucCellClick;
    public interface OnMucCellClick {
        void onClick(MucInfo mucInfo);
    }

    public MucListAdapter(List<MucInfo> mucInfos) {
        this.mMucInfos = mucInfos;
    }

    public void setOnItemClickListener(OnMucCellClick onMucCellClick) {
        this.mOnMucCellClick = onMucCellClick;
    }

    public void setMucInfos(List<MucInfo> mucInfos) {
        this.mMucInfos = mucInfos;
        notifyDataSetChanged();
        Log.d(TAG, "setMucInfos: " + mucInfos);
    }

    @Override
    public MucViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcell_muc, null);
        return new MucViewWrapper(rootView);
    }

    @Override
    public void onBindViewHolder(MucViewWrapper holder, int position) {
        MucInfo mucInfo = mMucInfos.get(position);
        if (mucInfo != null && holder != null) {
            holder.mucTitleTv.setText(mucInfo.getName());
            holder.mucRecentTv.setText(mucInfo.getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return mMucInfos == null ? 0 : mMucInfos.size();
    }

    public class MucViewWrapper extends RecyclerView.ViewHolder {
        public ImageView mucAvatarIv;
        public ImageView mucToolIv;
        public TextView mucTitleTv;
        public TextView mucRecentTv;

        public MucViewWrapper(View itemView) {
            super(itemView);
            this.mucAvatarIv = (ImageView) itemView.findViewById(R.id.listcell_muc_avatar_iv);
            this.mucToolIv = (ImageView) itemView.findViewById(R.id.listcell_muc_tool_iv);
            this.mucTitleTv = (TextView) itemView.findViewById(R.id.listcell_muc_title_tv);
            this.mucRecentTv = (TextView) itemView.findViewById(R.id.listcell_muc_recent_msg_tv);

            setOnClick(itemView);
        }

        public void setOnClick(View itemView) {
            // TODO 这里用lambda会报NoSuchMethodError
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = MucViewWrapper.this.getAdapterPosition();
                    MucListAdapter.this.mOnMucCellClick.onClick(MucListAdapter.this.mMucInfos.get(i));
                }
            });
        }
    }
}
