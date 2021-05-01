package com.codetiger.we.net;

import com.codetiger.we.data.dto.GankPictureAll;
import com.codetiger.we.data.result.GankResult;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIs {

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

    @GET("http://yaokui.ltd:8888/lmms_images")
    Call<ResponseBody> getPicture();
}
