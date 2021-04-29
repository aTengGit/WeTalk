package com.codetiger.we.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codetiger.we.R;


/**
 * 描述：看天气的Fragment
 */

public class SubwayFragment extends Fragment {

    public static SubwayFragment newInstance() {
        SubwayFragment fragment = new SubwayFragment();
        return fragment;
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subway, container,false);
    }
}
