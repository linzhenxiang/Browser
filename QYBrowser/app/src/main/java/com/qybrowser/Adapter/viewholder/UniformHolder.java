package com.qybrowser.Adapter.viewholder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helper.ItemDragHelperCallback;
import com.helper.ItemTouchHelper;
import com.qybrowser.R;
import com.qybrowser.fragment.BrowserFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class UniformHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.fg_home_list_uniform_recycle)
    RecyclerView mRecycleView;
    private SupportActivity mActivity;

    public UniformHolder(SupportActivity _mActivity, View itemView) {
        super(itemView);
        mActivity = _mActivity;
        ButterKnife.bind(this,itemView);
        initRecycle(itemView);
    }

    private void initRecycle(View itemView) {
        final String[] values = itemView.getResources().getStringArray(R.array.fg_home_titles);
        final String[] urls = itemView.getResources().getStringArray(R.array.fg_home_urls);
        TypedArray array = itemView.getResources().obtainTypedArray(R.array.fg_home_imgs);
        final int[] imgs = new int[array.length()];
        for (int i = 0; i < array.length(); i++) {
            imgs[i] = array.getResourceId(i, R.drawable.ic_iqyi);
        }
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(itemView.getContext(), 5);

        manager.setAutoMeasureEnabled(true);
        mRecycleView.setLayoutManager(manager);
        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycleView);
        mRecycleView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fg_home_list_uniform_item, parent, false);
                return new ItemHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView)  holder.itemView).setCompoundDrawablesWithIntrinsicBounds(0,imgs[position],0,0);
                ((TextView)  holder.itemView).setText(values[position]);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
    }


    class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.start(BrowserFragment.newInstance());
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        ViewCompat.setPivotX(itemView, itemView.getWidth() / 2);
                        ViewCompat.setPivotY(itemView, itemView.getHeight() / 2);
                        ViewCompat.postInvalidateOnAnimation(itemView);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(
                                ObjectAnimator.ofFloat(itemView, "scaleX", 1.0f, 1.2f),
                                ObjectAnimator.ofFloat(itemView, "scaleY", 1.0f, 1.2f)
                        );
                        animatorSet.setDuration(100);
                        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
//                        animatorSet.start();
                    }else{
                        ViewCompat.setPivotX(itemView, itemView.getWidth() / 2);
                        ViewCompat.setPivotY(itemView, itemView.getHeight() / 2);
                        ViewCompat.postInvalidateOnAnimation(itemView);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(
                                ObjectAnimator.ofFloat(itemView, "scaleX", 1.2f, 1.0f),
                                ObjectAnimator.ofFloat(itemView, "scaleY", 1.2f, 1.0f)
                        );
                        animatorSet.setDuration(300);
                        animatorSet.setInterpolator(new FastOutLinearInInterpolator());
//                        animatorSet.start();
                    }

                    return false;
                }
            });
        }
    }
}
