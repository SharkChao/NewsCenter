package com.newscenter.first.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.newscenter.first.datacenter.DataRepository;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.SignEntity;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.viewmodel.base.CommonDisposableSubscriber;

import java.util.List;

public class FeebackListViewModel extends BaseViewModel<HttpResult>{
    public MutableLiveData<List<FeedBack>> registerData = new MutableLiveData<>();
    public FeebackListViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<List<FeedBack>> getFeedBackListController(String username){
        DataRepository.getFeedbackListData(username)
                .subscribe(new CommonDisposableSubscriber<List<FeedBack>>(registerData,errorObservableData,""));
        return registerData;
    }

}
