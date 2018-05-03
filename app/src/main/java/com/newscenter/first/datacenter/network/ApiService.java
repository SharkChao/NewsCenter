package com.newscenter.first.datacenter.network;


import com.newscenter.first.model.GirlsData;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.User;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by yuzhijun on 2018/4/2.
 */

public interface ApiService<T> {
    @GET
    Flowable<ResponseBody> getDynamicData(@Url String url);

    @GET ("api/data/福利/{size}/{index}")
    Flowable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);

    @POST("/")
    Flowable<HttpResult> getLoginData(@Body User user);

    @POST("user/register")
    Flowable<HttpResult> getRegisterData(@Body User user);
}
