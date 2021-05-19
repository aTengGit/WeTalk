package com.codetiger.we.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.codetiger.we.R;
import com.codetiger.we.data.dto.Picture;




public class TwoPictureAdapter extends RecyclerView.Adapter<TwoPictureAdapter.ViewHolder> {

    private Context mContext;
    private Picture pic = null;

    public TwoPictureAdapter(Context mContext, Picture pic) {
        this.mContext = mContext;
        this.pic = pic;
    }

    public void addAll(Picture data) {
        this.pic = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TwoPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TwoPictureAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture_two, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull TwoPictureAdapter.ViewHolder holder, int position) {
        holder.bind(pic);

    }

    @Override
    public int getItemCount() {
        if (null != pic){
            return 1;
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_content = itemView.findViewById(R.id.img_content);
        }

        public void bind(Picture data) {
            Glide.with(mContext).load(data.getImgurl()).into(img_content);
        }
    }


}
