package com.codetiger.we.ui.adapter;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;



import com.codetiger.we.R;
import com.codetiger.we.data.dto.GankMeizi;
import com.codetiger.we.data.dto.GankPicture;
import com.codetiger.we.ui.activity.PictureDetailActivity;
import com.codetiger.we.utils.LogUtil;

import java.util.ArrayList;

/**
 * 描述：
 *
 */

public class GankMZAdapter extends RecyclerView.Adapter<GankMZAdapter.ViewHolder> {
    private String TAG = "GankMZAdapter";

    private Context mContext;
    private ArrayList<GankPicture> mzs = new ArrayList<>();

    public GankMZAdapter(Context mContext, ArrayList<GankPicture> mzs) {
        this.mContext = mContext;
        this.mzs = mzs;
    }

    public void addAll(ArrayList<GankPicture> data) {
        mzs.clear();
        mzs.addAll(data);
        notifyDataSetChanged();
    }

    public void loadMore(ArrayList<GankPicture> data) {
        LogUtil.d(TAG, "loadMore: ");
        mzs.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mz, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.d(TAG, "onBindViewHolder: ");
        holder.bind(mzs.get(position));
    }

    @Override
    public int getItemCount() {
        return mzs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        ImageView img_content;

        ViewHolder(View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.img_content);
        }

        void bind(GankPicture data) {
            LogUtil.d(TAG, "bind: ");
            Glide.with(mContext)
                    .load(data.getUrl())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(img_content);
            img_content.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, PictureDetailActivity.class);
                intent.putExtra("pic_url", data.getUrl());
                mContext.startActivity(intent);
            });
        }
    }
}
