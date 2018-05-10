package com.newscenter.first.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.User;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import java.util.Map;

import okhttp3.RequestBody;

public class MyInfoViewModel extends BaseViewModel<HttpResult<User>>{
    public MutableLiveData<User> userData = new MutableLiveData<>();
    public MyInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public LiveData<User> saveInfoController(Map<String, RequestBody>images){
        DataRepository.saveMyInfoData(images)
                .subscribeWith(new CommonDisposableSubscriber<User>(userData,errorObservableData,""));
        return userData;
    }


}
