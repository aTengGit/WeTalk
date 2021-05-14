package com.codetiger.we.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.os.Environment;
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
import com.codetiger.we.utils.LogUtil;
import com.codetiger.we.utils.SaveImage;
import com.codetiger.we.utils.ToastUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Response;

public class OnePictureAdapter extends RecyclerView.Adapter<OnePictureAdapter.ViewHolder> {
    private String TAG = "OnePictureAdapter";

    private Context mContext;
    private ResponseBody responseBody;

    public OnePictureAdapter(Context mContext, ResponseBody res) {
        LogUtil.d(TAG, "OnePictureAdapter: ");
        this.mContext = mContext;
        this.responseBody = res;
    }

    public void loadPicture(ResponseBody rb){
        LogUtil.d(TAG, "loadPicture: ");
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
        LogUtil.d(TAG, "onBindViewHolder: ");
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
        byte[] bytes = null;
        SaveImage saveImage = new SaveImage();

        ViewHolder(View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.img_content);
        }

        void bind(ResponseBody data) {

            try {
                bytes = data.bytes();
                Glide.with(mContext)
                        .load(bytes)
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(img_content);
            } catch (IOException e) {
                LogUtil.e(TAG, "error: "+e);
                e.printStackTrace();
            }

            img_content.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * @param view
                 * @return
                 */
                @SuppressLint("WrongConstant")
                @Override
                public boolean onLongClick(View view) {
                    Snackbar.make(img_content,"保存图片",Snackbar.LENGTH_LONG)
                            .setAction("是否保存", new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    LogUtil.d(TAG, "onLongClick: "+bitmap);
                                    saveImage.onSaveBitmap(bitmap,mContext);
                                }
                            }).setDuration(Snackbar.LENGTH_LONG).show();
                    return false;
                }
            });

        }
    }
}
