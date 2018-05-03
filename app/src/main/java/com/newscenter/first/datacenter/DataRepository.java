package com.newscenter.first.datacenter;


import com.newscenter.first.datacenter.network.ApiService;
import com.newscenter.first.datacenter.network.ApiServiceModule;
import com.newscenter.first.model.User;
import com.newscenter.first.util.GsonHelper;
import com.newscenter.first.util.SwitchSchedulers;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
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
                .compose(SwitchSchedulers.applySchedulers());
    }
    public static Flowable getRegisterData(User user){
        return sService.getRegisterData(user)
                .compose(SwitchSchedulers.applySchedulers());
    }




}
