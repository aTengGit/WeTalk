package com.codetiger.we.ui.fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codetiger.we.R;
import com.codetiger.we.data.dto.GankMeizi;
import com.codetiger.we.data.dto.GankPicture;
import com.codetiger.we.net.APIService;
import com.codetiger.we.ui.adapter.GankMZAdapter;
import com.codetiger.we.utils.ResUtils;
import com.codetiger.we.utils.RxSchedulers;
import com.codetiger.we.utils.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：妹子Fragment
 *
 */

public class GankMZFragment extends Fragment {

    private static final String TAG = "GankMZFragment";
    private SwipeRefreshLayout srl_refresh;
    private FloatingActionButton fab_top;
    private RecyclerView rec_mz;
    private CompositeDisposable mSubscriptions;
    private GankMZAdapter mAdapter;
    private static final int PRELOAD_SIZE = 6;
    private int mCurPage = 1;
    private ArrayList<GankPicture> mData;
    private final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    public static GankMZFragment newInstance() {
        return new GankMZFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_mz_content, container, false);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        rec_mz = view.findViewById(R.id.rec_mz);
        fab_top = view.findViewById(R.id.fab_top);
        srl_refresh.setOnRefreshListener(() -> {
            mCurPage = 1;
            fetchGankMZ(true);
        });
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rec_mz.setLayoutManager(layoutManager);
        rec_mz.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.d(TAG, "onScrolled: dx: "+dx +", dy: "+dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d(TAG, "onScrollStateChanged: newState : "+newState+", recyclerView: " +recyclerView);
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//加载更多
                    Log.d(TAG, "layoutManager.getItemCount(): "+layoutManager.getItemCount());
                    Log.d(TAG, "recyclerView.getChildCount(): "+recyclerView.getChildCount());
                    Log.d(TAG, "layoutManager.findFirstVisibleItemPosition(): "+layoutManager.findFirstVisibleItemPosition());
                    if (layoutManager.getItemCount() - recyclerView.getChildCount() <= layoutManager.findFirstVisibleItemPosition()) {
                        ++mCurPage;
                        fetchGankMZ(false);
                        Log.d(TAG, "update: ");
                    }
                }
                if (layoutManager.findFirstVisibleItemPosition() != 0) {
                    fabInAnim();
                } else {
                    fabOutAnim();
                }
            }
        });
        fab_top.setOnClickListener(v -> {
            LinearLayoutManager manager = (LinearLayoutManager) rec_mz.getLayoutManager();
            //如果超过50项直接跳到开头，不然要滚好久
            if(manager.findFirstVisibleItemPosition() < 50) {
                rec_mz.smoothScrollToPosition(0);
            } else {
                rec_mz.scrollToPosition(0);
                fabOutAnim();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSubscriptions = new CompositeDisposable();
        mData = new ArrayList<>();
        mAdapter = new GankMZAdapter(getActivity(), mData);
        rec_mz.setAdapter(mAdapter);
        srl_refresh.setRefreshing(true);
        fetchGankMZ(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    /* 拉取妹子数据 */
/*    private void fetchGankMZ(boolean isRefresh) {
        Log.d(TAG, "fetchGankMZ: ");
        Disposable subscribe = APIService.getInstance().apis.fetchGankMZ(20, mCurPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> srl_refresh.setRefreshing(true))
                .doFinally(() -> srl_refresh.setRefreshing(false))
                .subscribe(data -> {
                    if(data != null && data.getResults() != null && data.getResults().size() > 0) {
                        ArrayList<GankMeizi> results = data.getResults();
                        if (isRefresh) {
                            mAdapter.addAll(results);
                            ToastUtils.shortToast(ResUtils.getString(R.string.refresh_success));
                        } else {
                            mAdapter.loadMore(results);
                            String msg = String.format(ResUtils.getString(R.string.load_more_num),results.size(),"妹子");
                            ToastUtils.shortToast(msg);
                        }
                    }
                }, RxSchedulers::processRequestException);
        mSubscriptions.add(subscribe);
    }*/
    private void fetchGankMZ(boolean isRefresh) {
        Log.d(TAG, "fetchGankMZ: ");
        Disposable subscribe = APIService.getInstance().apis.fetchGankPicture(mCurPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> srl_refresh.setRefreshing(true))
                .doFinally(() -> srl_refresh.setRefreshing(false))
                .subscribe(data -> {
                    Log.d(TAG, "fetchGankMZ: data:" +data);
                    if(data != null && data.getPictures() != null && data.getPictures().size() > 0) {
                        ArrayList<GankPicture> results = data.getPictures();
                        if (isRefresh) {
                            mAdapter.addAll(results);
                            ToastUtils.shortToast(ResUtils.getString(R.string.refresh_success));
                        } else {
                            mAdapter.loadMore(results);
                            String msg = String.format(ResUtils.getString(R.string.load_more_num),results.size(),"妹子");
                            ToastUtils.shortToast(msg);
                        }
                    }
                }, RxSchedulers::processRequestException);
        mSubscriptions.add(subscribe);
    }

    /* 悬浮按钮显示动画 */
    private void fabInAnim() {
        Log.d(TAG, "fabInAnim: ");
        if (fab_top.getVisibility() == View.GONE) {
            fab_top.setVisibility(View.VISIBLE);
            ViewCompat.animate(fab_top).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null).start();
        }
    }

    /* 悬浮图标隐藏动画 */
    private void fabOutAnim() {
        Log.d(TAG, "fabOutAnim: ");
        if (fab_top.getVisibility() == View.VISIBLE) {
            ViewCompat.animate(fab_top).scaleX(0.0F).scaleY(0.0F).alpha(0.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {

                }

                @Override
                public void onAnimationEnd(View view) {
                    fab_top.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            }).start();
        }
    }

}
