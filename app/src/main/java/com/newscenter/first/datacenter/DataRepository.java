package com.newscenter.first.datacenter;


import com.newscenter.first.datacenter.network.ApiService;
import com.newscenter.first.datacenter.network.ApiServiceModule;
import com.newscenter.first.datacenter.network.HttpResultFunc;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.News;
import com.newscenter.first.model.SignEntity;
import com.newscenter.first.model.User;
import com.newscenter.first.util.GsonHelper;
import com.newscenter.first.util.SwitchSchedulers;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class DataRepository {
    private static ApiService sService = ApiServiceModule.getInstance().getNetworkService();


    public static <T> Flowable getDynamicData(String url, final Type clazz) {
        return sService
                .getDynamicData(url)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        return GsonHelper.getIntance().str2JsonBean(responseBody.string(),clazz);
                    }
                });
    }

    public static Flowable getLoginData(User user){
        return sService.getLoginData(user)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<User>());
    }
    public static Flowable getRegisterData(User user){
        return sService.getRegisterData(user)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<User>());

    }

    public static Flowable getNewsData(String type,boolean isRefresh){
        return sService.getNewsData(type,isRefresh)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<List<News>>());
    }

    public static Flowable getSignData(String name){
        return sService.getSignData(name)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<List<SignEntity>>());
    }
    public static Flowable setSignData(String name,String date){
        return sService.setSignData(name,date)
                .compose(SwitchSchedulers.applySchedulers())
                 .map(new HttpResultFunc<List<SignEntity>>());
    }
    public static Flowable getFeedbackListData(String name){
        return sService.getFeedbackListData(name)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<List<FeedBack>>());
    }
    public static Flowable setFeedbackNewData(String name,String content){
        return sService.setFeedbackData(name,content)
                .compose(SwitchSchedulers.applySchedulers());
    }
    public static Flowable saveMyInfoData(Map<String, RequestBody> image){
        return sService.saveEdit(image)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new HttpResultFunc<User>());
    }
    public static Flowable setNewsData(Map<String,RequestBody> news){
        return sService.setNewsData(news)
                .compose(SwitchSchedulers.applySchedulers());
    }
    public static Flowable updateNewsData(Map<String,RequestBody> news){
        return sService.updateNewsData(news)
                .compose(SwitchSchedulers.applySchedulers());
    }
    public static Flowable deleteNewsData(String url){
        return sService.deleteNews(url)
                .compose(SwitchSchedulers.applySchedulers());
    }
}
