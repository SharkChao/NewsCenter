package com.newscenter.first.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import java.util.List;

public class FeebackNewViewModel extends BaseViewModel<HttpResult>{
    public MutableLiveData<HttpResult> registerData = new MutableLiveData<>();
    public FeebackNewViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<HttpResult> setFeedBackNewController(String username,String content){
        DataRepository.setFeedbackNewData(username,content)
                .subscribe(new CommonDisposableSubscriber<HttpResult>(registerData,errorObservableData,""));
        return registerData;
    }

}
