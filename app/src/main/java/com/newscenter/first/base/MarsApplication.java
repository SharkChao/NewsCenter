package com.newscenter.first.base;

import android.app.Application;

/**
 * Created by Admin on 2018/4/17.
 */

public class MarsApplication extends Application {
    private static MarsApplication sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static MarsApplication getInstance(){
       return sApplication;
    }
}
