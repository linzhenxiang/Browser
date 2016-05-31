package com.qybrowser.Adapter.viewholder;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qybrowser.R;
import com.qybrowser.fragment.BrowserFragment;
import com.qybrowser.fragment.MenuFragment;
import com.qybrowser.widget.DividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class VideoHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.fg_home_list_video_top_recycle)
    RecyclerView mTopRecycle;
    @Bind(R.id.fg_home_list_video_bottom_recycle)
    RecyclerView mBottomRecycle;

    private SupportActivity mActivity;

    public VideoHolder(SupportActivity _mActivity, View itemView) {
        super(itemView);
        mActivity = _mActivity;

        ButterKnife.bind(this,itemView);
        mTopRecycle.setHasFixedSize(true);
        mTopRecycle.setItemAnimator(new DefaultItemAnimator());
        mTopRecycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(8, 8, 8, 8);
            }
        });
        mTopRecycle.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
        mTopRecycle.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_list_video_item1, parent, false);
                return new VideoHolder_1(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        mBottomRecycle.setHasFixedSize(true);
        mBottomRecycle.setItemAnimator(new DefaultItemAnimator());
        mBottomRecycle.addItemDecoration(new DividerItemDecoration(mActivity,DividerItemDecoration.VERTICAL_LIST));
        mBottomRecycle.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        mBottomRecycle.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_list_video_item2, parent, false);
                return new VideoHolder_2(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
    }

    class VideoHolder_1 extends RecyclerView.ViewHolder{
        @Bind(R.id.fg_home_video_top_img)
        ImageView imageView;

        public VideoHolder_1(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    imageView.onTouchEvent(event);
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.start(BrowserFragment.newInstance());
                }
            });
        }
    }
    class VideoHolder_2 extends RecyclerView.ViewHolder{

        public VideoHolder_2(View itemView) {
            super(itemView);
        }
    }
}
