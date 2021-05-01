package com.codetiger.we.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codetiger.we.R;
import com.codetiger.we.net.APIService;
import com.codetiger.we.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureOneFragment extends Fragment {
    private int SUCCESS = 1;
    private int ERROR = 0;
    private String TAG = "TestFragment";
    private Handler handler;
    private SwipeRefreshLayout srl_refresh;

    private FloatingActionButton fab_top;
    private ImageView img;

    private CompositeDisposable mSubscriptions;

    public static PictureOneFragment newInstance() {
        PictureOneFragment fragment = new PictureOneFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_picture, container, false);
        fab_top = view.findViewById(R.id.fab_top1);
        img = view.findViewById(R.id.image_test);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: 22222222");
        super.onViewCreated(view, savedInstanceState);
        mSubscriptions = new CompositeDisposable();
        srl_refresh.setOnRefreshListener(() -> {
            showImg();
        });

        srl_refresh.setRefreshing(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImg();
            }
        });
        initHandler();
        showImg();
    }

    private void initHandler() {
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@SuppressLint("HandlerLeak") @NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == SUCCESS) {
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    img.setImageBitmap(bitmap);
                } else {
                    ToastUtils.longToast("网络请求失败！！！");
                    ToastUtils.longToast((String) msg.obj);
                }
            }
        };
    }

    private void showImg() {
        APIService.getInstance().apis.getPicture()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        srl_refresh.setRefreshing(false);
                        Log.d(TAG, "toString: " + response.toString());
                        Log.d(TAG, "body: " + response.body());
                        Log.d(TAG, "message: " + response.raw().message());
                        Log.d(TAG, "headers: " + response.headers());
                        try {
                            byte[] Picture_bt = response.body().bytes();
                            Message message = handler.obtainMessage();
                            message.obj = Picture_bt;
                            message.what = SUCCESS;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Message message = handler.obtainMessage();
                        message.obj = t;
                        message.what = ERROR;
                        handler.sendMessage(message);
                    }
                });
    }


}
