package com.newscenter.first.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.User;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import io.reactivex.subscribers.DefaultSubscriber;
import io.reactivex.subscribers.DisposableSubscriber;

public class RegisterViewModel extends BaseViewModel<HttpResult>{
    public MutableLiveData<HttpResult> registerData = new MutableLiveData<>();
    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<HttpResult> getRegisterController(User user){
        DataRepository.getRegisterData(user)
                .subscribe(new CommonDisposableSubscriber<HttpResult>(registerData,errorObservableData));
        return registerData;
    }
}
