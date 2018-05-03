package com.newscenter.first.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by Admin on 2018/4/17.
 */

public class MarsApplication extends Application {
    private static MarsApplication sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    public static MarsApplication getInstance(){
       return sApplication;
    }
}
