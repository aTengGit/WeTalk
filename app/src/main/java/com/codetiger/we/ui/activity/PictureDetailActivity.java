package com.codetiger.we.ui.activity;

import android.os.Bundle;

import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.codetiger.we.R;
import com.r0adkll.slidr.Slidr;

/**
 * 描述：图片详情页
 *
 */

public class PictureDetailActivity extends AppCompatActivity {

    private ImageView img_picture;
    private String picUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        Slidr.attach(this);
        initData();
        initView();
    }

    private void initData() {
        picUrl = getIntent().getStringExtra("pic_url");
    }

    private void initView() {
        img_picture = findViewById(R.id.img_picture);
        if(picUrl != null) {
            Glide.with(this).load(picUrl).into(img_picture);
        }
    }
}
