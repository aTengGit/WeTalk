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
 * 目前没有作用了
 *
 */
public class VerticalPictureFragment extends Fragment {
    private static String TAG = "VerticalPictureFragment";

    private Context mContext;
    private TabLayout tl_verticel_pic;
    private ViewPager vp_content;
    protected CompositeDisposable mSubscriptions;


    public static VerticalPictureFragment newInstance() {
        LogUtil.d(TAG, "newInstance: ");
        VerticalPictureFragment fragment = new VerticalPictureFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_verticel_pic_sister, container, false);
        tl_verticel_pic = view.findViewById(R.id.tl_verticel_pic);
        vp_content = view.findViewById(R.id.vp_content);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mSubscriptions = new CompositeDisposable();
        VerticalPictureFragment.TabFragmentPagerAdapter adapter = new VerticalPictureFragment.TabFragmentPagerAdapter(this.getChildFragmentManager());
        vp_content.setAdapter(adapter);
        tl_verticel_pic.setupWithViewPager(vp_content);
    }


    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private final String[] mTitles = {"picture","test"};

        private TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            LogUtil.d(TAG, "TabFragmentPagerAdapter: ");
        }

        @Override
        public Fragment getItem(int position) {
            LogUtil.d(TAG, "getItem: " + position);
            if (position == 0){
                return PictureOneFragment.newInstance();
            }
            return GankMZFragment.newInstance();
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
