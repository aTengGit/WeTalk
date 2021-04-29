package com.codetiger.we.ui.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codetiger.we.R;


/**
 * 描述：小工具的Fragment
 *
 */

public class ToolsFragment extends Fragment {

    public static ToolsFragment newInstance() {
        ToolsFragment fragment = new ToolsFragment();
        return fragment;
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container,false);
    }
}
