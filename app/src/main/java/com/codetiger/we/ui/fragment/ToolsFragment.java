package com.codetiger.we.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codetiger.we.R;
import com.codetiger.we.ui.activity.CameraActivity;


/**
 * 描述：小工具的Fragment
 *
 */

public class ToolsFragment extends Fragment {
    private String TAG = "ToolsFragment";
    private Button button;

    public static ToolsFragment newInstance() {
        ToolsFragment fragment = new ToolsFragment();
        return fragment;
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tools, container,false);
        Log.d(TAG, "onCreateView: ");
        button = view.findViewById(R.id.cameraClick);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Intent intent = new Intent(getContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
