package com.qybrowser.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qybrowser.Adapter.viewholder.UniformHolder;
import com.qybrowser.Adapter.viewholder.VideoHolder;
import com.qybrowser.R;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class FGHomeRecyclerAdapter extends RecyclerView.Adapter {
    private SupportActivity _mActivity;

    public FGHomeRecyclerAdapter(SupportActivity mActivity) {
        _mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_list_video_layout, parent, false);
                holder = new VideoHolder(_mActivity,view);
                break;
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_list_uniform_layout, parent, false);
                holder = new UniformHolder(_mActivity,view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
