package com.codetiger.we.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codetiger.we.R;
import com.codetiger.we.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.disposables.CompositeDisposable;


/**
 * 描述：看图片的Fragment
 *
 */

public class PictureFragment extends Fragment {
    private static String TAG = "LittleSisterFragment";

    private Context mContext;
    private TabLayout tl_little_sister;
    private ViewPager vp_content;
    protected CompositeDisposable mSubscriptions;

    public static PictureFragment newInstance() {
        LogUtil.d(TAG, "newInstance: ");
        PictureFragment fragment = new PictureFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_little_sister, container, false);
        tl_little_sister = view.findViewById(R.id.tl_little_sister);
        vp_content = view.findViewById(R.id.vp_content);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mSubscriptions = new CompositeDisposable();
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(this.getChildFragmentManager());
        vp_content.setAdapter(adapter);
        tl_little_sister.setupWithViewPager(vp_content);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private final String[] mTitles = {"Picture","Two"/*,"Gank"*/};

        private TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            LogUtil.d(TAG, "TabFragmentPagerAdapter: ");
        }

        @Override
        public Fragment getItem(int position) {
            LogUtil.d(TAG, "getItem: ");
         /*   switch (position){
                case 0: return PictureOneFragment.newInstance();
                case 1: return GankMZFragment.newInstance();
                case 2: return PictureTwoFragment.newInstance();
            }*/

            if (position ==1 ){
                return PictureTwoFragment.newInstance();
            } else if (position == 2){
//                return GankMZFragment.newInstance();
                LogUtil.d(TAG,"11111");
            }
            return PictureOneFragment.newInstance();
        }

        @Override
        public int getCount() {
            LogUtil.d(TAG, "getCount: ");
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            LogUtil.d(TAG, "getPageTitle: position: " + position);
            return mTitles[position];
        }
    }



}
