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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codetiger.we.R;
import com.codetiger.we.net.APIService;
import com.codetiger.we.ui.adapter.OnePictureAdapter;
import com.codetiger.we.utils.LogUtil;
import com.codetiger.we.utils.RxSchedulers;
import com.codetiger.we.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureOneFragment extends Fragment {
    private String TAG = "TestFragment";
    private SwipeRefreshLayout srl_refresh;
    private OnePictureAdapter adapter;
    private RecyclerView rec_mz;
    private ResponseBody responseBody;

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
        rec_mz = view.findViewById(R.id.rec_mz);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        responseBody = null;
        adapter = new OnePictureAdapter(getActivity(),responseBody);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        rec_mz.setLayoutManager(layoutManager);

        fab_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImg();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        mSubscriptions = new CompositeDisposable();
        srl_refresh.setOnRefreshListener(() -> {
            showImg();
        });
        rec_mz.setAdapter(adapter);
        srl_refresh.setRefreshing(true);
        showImg();
    }


    private void showImg() {
        Disposable subscribe = APIService.getInstance().apis.getPicture()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> srl_refresh.setRefreshing(true))
                .doFinally(() -> srl_refresh.setRefreshing(false))
                .subscribe(data->{
                    adapter.loadPicture(data);
                }, RxSchedulers::processRequestException);
        mSubscriptions.add(subscribe);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }
}
