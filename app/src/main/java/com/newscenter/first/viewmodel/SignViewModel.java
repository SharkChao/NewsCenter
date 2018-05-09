package com.newscenter.first.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.SignEntity;
import com.newscenter.first.model.User;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import java.util.List;

public class SignViewModel extends BaseViewModel<HttpResult>{
    public MutableLiveData<List<SignEntity>> registerData = new MutableLiveData<>();
    public MutableLiveData<List<SignEntity>> signData = new MutableLiveData<>();
    public SignViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<SignEntity>> getSignController(String user){
        DataRepository.getSignData(user)
                .subscribe(new CommonDisposableSubscriber<List<SignEntity>>(registerData,errorObservableData,""));
        return registerData;
    }
    public LiveData<List<SignEntity>> setSignController(String name,String date){
        DataRepository.setSignData(name,date)
                .subscribe(new CommonDisposableSubscriber<List<SignEntity>>(signData,errorObservableData,""));
        return signData;
    }

}
