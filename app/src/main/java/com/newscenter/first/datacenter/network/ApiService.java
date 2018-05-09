package com.newscenter.first.datacenter.network;


import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.GirlsData;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.News;
import com.newscenter.first.model.SignEntity;
import com.newscenter.first.model.User;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by yuzhijun on 2018/4/2.
 */

public interface ApiService<T> {
    @GET
    Flowable<ResponseBody> getDynamicData(@Url String url);

    @GET ("api/data/福利/{size}/{index}")
    Flowable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);

    @POST("user/login")
    Flowable<HttpResult<User>> getLoginData(@Body User user);

    @POST("user/register")
    Flowable<HttpResult<User>> getRegisterData(@Body User user);

    @GET("news")
    Flowable<HttpResult<List<News>>> getNewsData(@Query("type") String type,@Query("isrefresh")boolean isRefresh);

    @GET("getSign")
    Flowable<HttpResult<List<SignEntity>>> getSignData(@Query("user_name")String name);

    @GET("setSign")
    Flowable<HttpResult<List<SignEntity>>> setSignData(@Query("user_name")String name,@Query("date")String date);

    @GET("getFeedbackList")
    Flowable<HttpResult<List<FeedBack>>> getFeedbackListData(@Query("user_name")String name);

    @GET("setFeedback")
    Flowable<HttpResult> setFeedbackData(@Query("user_name") String username,@Query("content")String content);
}
