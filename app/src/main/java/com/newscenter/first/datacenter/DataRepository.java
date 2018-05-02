package com.newscenter.first.datacenter;


import com.newscenter.first.datacenter.network.ApiServiceModule;
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

    public static <T> Flowable getDynamicData(String url, final Type clazz) {
        return ApiServiceModule.getInstance().getNetworkService()
                .getDynamicData(url)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        return GsonHelper.getIntance().str2JsonBean(responseBody.string(),clazz);
                    }
                });
    }


}
