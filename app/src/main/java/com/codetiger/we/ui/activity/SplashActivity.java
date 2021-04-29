package com.codetiger.we.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codetiger.we.R;


/**
 * 描述：闪屏页
 *
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv_logo = findViewById(R.id.tv_logo);
        tv_logo.postDelayed(this::jump,1000L);

    }

    /* 完成一些初始化操作 */
    private void init() {

    }

    /* 页面逻辑跳转 */
    private void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
