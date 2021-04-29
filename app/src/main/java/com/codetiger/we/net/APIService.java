package com.codetiger.we.net;



import com.codetiger.we.WeInit;
import com.codetiger.we.data.dto.GankPicture;
import com.codetiger.we.data.dto.GankPictureAll;
import com.codetiger.we.data.result.GankResult;
import com.codetiger.we.data.result.GankResultNew;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 描述：API 请求接口
 *
 */

public class APIService {

    private static String BASE_URL = "http://www.coderpig.com/";   //未启用

    public APIs apis;

    private static APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            instance = new APIService();
        }
        return instance;
    }

    private APIService() {
        Retrofit storeRestAPI = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(WeInit.mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apis = storeRestAPI.create(APIs.class);
    }

    public interface APIs{

        /* Gank.io 妹子图 */
        @GET("http://gank.io/api/data/福利/{count}/{page}")
        Flowable<GankResult> fetchGankMZ(
                @Path("count") int count,
                @Path("page") int page
        );

//        http://yaokui.ltd:8888/1080p
//        https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10

        @GET("https://gank.io/api/v2/data/category/Girl/type/Girl/page/{page}/count/{count}")
        Flowable<GankPictureAll> fetchGankPicture(
                @Path("page") int page,
                @Path("count") int count
        );

    }

}
