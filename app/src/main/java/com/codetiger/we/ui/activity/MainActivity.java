package com.codetiger.we.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.codetiger.we.WeConstant;
import com.codetiger.we.R;
import com.codetiger.we.ui.fragment.LittleSisterFragment;
import com.codetiger.we.ui.fragment.SubwayFragment;
import com.codetiger.we.ui.fragment.PictureOneFragment;
import com.codetiger.we.ui.fragment.ToolsFragment;
import com.codetiger.we.ui.fragment.VerticalPictureFragment;
import com.codetiger.we.ui.fragment.WeatherFragment;
import com.codetiger.we.utils.PackageUtils;
import com.codetiger.we.utils.ResUtils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private String TAG = "MainActivity";

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;
    private TextView tv_nav_title;
    private ConstraintLayout cly_main_content;
    private FragmentManager mFgManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFgManager = getSupportFragmentManager();
        initView();
        initData();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        tv_nav_title = nav_view.getHeaderView(0).findViewById(R.id.tv_nav_title);
        drawer_layout = findViewById(R.id.drawer_layout);
        cly_main_content = findViewById(R.id.cly_main_content);

        setSupportActionBar(toolbar);
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initData() {
        mFgManager.beginTransaction().replace(R.id.cly_main_content,
                VerticalPictureFragment.newInstance(), WeConstant.FG_VERTICAl_PICTURE).commit();
        toolbar.setTitle(ResUtils.getString(R.string.menu_see_one_pic));
        String version = PackageUtils.packageName();
        if(version != null) {
            String msg = String.format(ResUtils.getString(R.string.menu_drysister_version), version);
            tv_nav_title.setText(msg);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_see_little_sister:
                Log.d(TAG, "onNavigationItemSelected:"+(mFgManager.findFragmentByTag(WeConstant.FG_LITTLE_SISTER)==null));
                if (mFgManager.findFragmentByTag(WeConstant.FG_LITTLE_SISTER) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            LittleSisterFragment.newInstance(), WeConstant.FG_LITTLE_SISTER).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_see_little_sister));
                }
                break;
            case R.id.nav_see_one_pic:
               /* if (mFgManager.findFragmentByTag(WeConstant.FG_VERTICAl_PICTURE) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            NewsFragment.newInstance(), WeConstant.FG_VERTICAl_PICTURE).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_see_one_pic));
                }*/
                if (mFgManager.findFragmentByTag(WeConstant.FG_VERTICAl_PICTURE) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            VerticalPictureFragment.newInstance(), WeConstant.FG_VERTICAl_PICTURE).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_see_one_pic));
                }
                break;
            case R.id.nav_use_check_weather:
                if (mFgManager.findFragmentByTag(WeConstant.FG_WEATHER) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            WeatherFragment.newInstance(), WeConstant.FG_WEATHER).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_use_check_weather));
                }
                break;
            case R.id.nav_use_check_subway:
                if (mFgManager.findFragmentByTag(WeConstant.FG_SUBWAY) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            SubwayFragment.newInstance(), WeConstant.FG_SUBWAY).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_use_check_subway));
                }
                break;
            case R.id.nav_use_tools:
                if (mFgManager.findFragmentByTag(WeConstant.FG_TOOLS) == null) {
                    mFgManager.beginTransaction().replace(R.id.cly_main_content,
                            ToolsFragment.newInstance(), WeConstant.FG_TOOLS).commit();
                    toolbar.setTitle(ResUtils.getString(R.string.menu_use_tools));
                }
                break;
            case R.id.nav_else_setting:
                startActivity(new Intent(this, com.codetiger.we.ui.activity.SettingActivity.class));
                break;
            case R.id.nav_else_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
