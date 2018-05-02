package com.newscenter.first.datacenter;

import android.app.Application;


import com.newscenter.first.datacenter.db.AppDatabase;
import com.newscenter.first.datacenter.db.User;
import com.newscenter.first.datacenter.network.ApiServiceModule;
import com.newscenter.first.datacenter.network.HttpResultFunc;
import com.newscenter.first.model.GirlsData;
import com.newscenter.first.util.AppExecutors;
import com.newscenter.first.util.SwitchSchedulers;

import io.reactivex.Flowable;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class GirlsResposity {
    private static AppExecutors mAppExecutors = new AppExecutors();
    public static Flowable getFuliData(String size, String index) {
         return  ApiServiceModule.getInstance().getNetworkService()
                 .getFuliData(size,index)
                 .map(new HttpResultFunc<GirlsData>())
                 .compose(SwitchSchedulers.applySchedulers())
                 .map(girlsData -> girlsData);
    }

    public static Flowable<User> getUserData(Application application){
        return AppDatabase.getInstance(application,mAppExecutors)
                .userDao().getUser();
    }
}
