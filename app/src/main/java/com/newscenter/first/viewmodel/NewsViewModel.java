package com.newscenter.first.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.News;
import com.newscenter.first.model.User;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class NewsViewModel extends BaseViewModel<List<News>>{
    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    public MutableLiveData<HttpResult> setNewsData = new MutableLiveData<>();
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public LiveData<List<News>> newsController(String type,boolean isRefresh){
        if (isRefresh){
            DataRepository.getNewsData(type,isRefresh)
                    .subscribeWith(new CommonDisposableSubscriber<List<News>>(newsData,errorObservableData,type));
        }else {
            String json = CommonUtil.getShardPStringByKey(type);
            if (CommonUtil.isStrEmpty(json)){
                DataRepository.getNewsData(type,isRefresh)
                        .subscribeWith(new CommonDisposableSubscriber<List<News>>(newsData,errorObservableData,type));
            }else {
                List<News> newss= new Gson().fromJson(json, new TypeToken<List<News>>(){}.getType());
                newsData.setValue(newss);
            }
        }
        return newsData;
    }

    public LiveData<HttpResult> setNewsController(Map<String, RequestBody> news) {
        DataRepository.setNewsData(news)
                .subscribeWith(new CommonDisposableSubscriber<HttpResult>(setNewsData,errorObservableData,""));
        return setNewsData;
    }

    public LiveData<HttpResult> updateNewsControllser(Map<String, RequestBody> news) {
        DataRepository.updateNewsData(news)
                .subscribeWith(new CommonDisposableSubscriber<HttpResult>(setNewsData,errorObservableData,""));
        return setNewsData;
    }
}
