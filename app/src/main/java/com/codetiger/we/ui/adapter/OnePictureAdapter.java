package com.codetiger.we.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codetiger.we.R;
import com.codetiger.we.data.dto.GankPicture;
import com.codetiger.we.ui.activity.PictureDetailActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class OnePictureAdapter extends RecyclerView.Adapter<OnePictureAdapter.ViewHolder> {
    private String TAG = "OnePictureAdapter";

    private Context mContext;
    private ResponseBody responseBody;

    public OnePictureAdapter(Context mContext, ResponseBody res) {
        Log.d(TAG, "OnePictureAdapter: ");
        this.mContext = mContext;
        this.responseBody = res;
    }

    public void loadPicture(ResponseBody rb){
        Log.d(TAG, "loadPicture: ");
        responseBody = rb;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OnePictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnePictureAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.bind(responseBody);
    }


    @Override
    public int getItemCount() {
        if (null != responseBody){
            return 1;
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_content;

        ViewHolder(View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.img_content);
        }

        void bind(ResponseBody data) {

            try {
                Glide.with(mContext)
                        .load(data.bytes())
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(img_content);
            } catch (IOException e) {
                Log.e(TAG, "error: "+e);
                e.printStackTrace();
            }

/*            img_content.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, PictureDetailActivity.class);
                intent.putExtra("pic_url", data.getUrl());
                mContext.startActivity(intent);
            });*/
        }
    }
}
