package com.codetiger.we.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codetiger.we.R;
import com.codetiger.we.data.dto.Picture;
import com.codetiger.we.net.APIService;
import com.codetiger.we.ui.adapter.OnePictureAdapter;
import com.codetiger.we.ui.adapter.TwoPictureAdapter;
import com.codetiger.we.utils.LogUtil;
import com.codetiger.we.utils.RxSchedulers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureTwoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String TAG = "TestFragment";
    private SwipeRefreshLayout srl_refresh;
    private TwoPictureAdapter adapter;
    private RecyclerView rec_pic;
    private Picture picture;

    private FloatingActionButton fab_top;
    private ImageView img;

    private CompositeDisposable mSubscriptions;


    // TODO: Rename and change types of parameters
/*    private String mParam1;
    private String mParam2;*/

    public PictureTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
     * @return A new instance of fragment PictureTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureTwoFragment newInstance(/*String param1, String param2*/) {
        PictureTwoFragment fragment = new PictureTwoFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture_two, container, false);
        fab_top = view.findViewById(R.id.fab_top1);
        rec_pic = view.findViewById(R.id.rec_pic);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        picture = null;
        adapter = new TwoPictureAdapter(getActivity(),picture);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        rec_pic.setLayoutManager(layoutManager);

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
        rec_pic.setAdapter(adapter);
        srl_refresh.setRefreshing(true);
        showImg();
    }

    private void showImg() {
        Disposable subscribe = APIService.getInstance().apis.getPictureTwo("美女","json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> srl_refresh.setRefreshing(true))
                .doFinally(() -> srl_refresh.setRefreshing(false))
                .subscribe(date->{
                    adapter.addAll(date);
                },RxSchedulers::processRequestException);

        mSubscriptions.add(subscribe);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

}