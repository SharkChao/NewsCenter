package com.newscenter.first.datacenter.network;



import com.newscenter.first.util.Constants;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuzhijun on 2018/4/2.
 */

public class ApiServiceModule {
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 3000;
    private static ApiServiceModule mInstance;
    private ApiServiceModule(){

    }
    public static ApiServiceModule getInstance(){
        if (null == mInstance){
            synchronized (ApiServiceModule.class){
                if (null == mInstance){
                    mInstance = new ApiServiceModule();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClient provideOkHttpClientBuilder(){
        OkHttpClient httpClientBuilder = new OkHttpClient();
        httpClientBuilder.newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return httpClientBuilder;
    }

    private Retrofit provideRetrofit(OkHttpClient OkHttpClientBuilder,String url){
        return new Retrofit.Builder()
                .baseUrl(url == null || "".equalsIgnoreCase(url) ? Constants.BASE_URL:url)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClientBuilder)
                .build();
    }

    private ApiService provideApiService(Retrofit retrofit){
        return getByProxy(ApiService.class,retrofit);
    }

    private ApiService getByProxy(Class<? extends ApiService> apiService, Retrofit retrofit){
        ApiService api = retrofit.create(apiService);
        return (ApiService) Proxy.newProxyInstance(apiService.getClassLoader(),new Class<?>[] { apiService },new ResponseErrorProxy(api,retrofit.baseUrl().toString()));
    }

    public ApiService getNetWorkService(Class<? extends ApiService> apiService,String url){
        OkHttpClient okHttpClient = provideOkHttpClientBuilder();
        Retrofit retrofit = provideRetrofit(okHttpClient,url);
        return getByProxy(apiService,retrofit);
    }

    public ApiService getNetworkService(String url){
        OkHttpClient okHttpClient = provideOkHttpClientBuilder();
        Retrofit retrofit = provideRetrofit(okHttpClient,url);
        return provideApiService(retrofit);
    }

    public ApiService getNetworkService(){
       return getNetworkService(null);
    }
}
