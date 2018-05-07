package com.newscenter.first.viewmodel.base;

import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.newscenter.first.util.CommonUtil;

import io.reactivex.subscribers.DisposableSubscriber;

public class CommonDisposableSubscriber<T> extends DisposableSubscriber<T> {

    private MutableLiveData<T> mMutableLiveData;
    private MutableLiveData<Throwable> mErrorLiveData;
    private String mType;

    public CommonDisposableSubscriber(MutableLiveData<T> mutableLiveData, MutableLiveData<Throwable> errorLiveData,String type) {
        mMutableLiveData = mutableLiveData;
        mErrorLiveData = errorLiveData;
        mType = type;
    }

    @Override
    public void onNext(T o) {
        mMutableLiveData.setValue(o);
        if (!CommonUtil.isStrEmpty(mType)){
            String json = new Gson().toJson(o);
            CommonUtil.setShardPString(mType,json);
        }

    }

    @Override
    public void onError(Throwable t) {
        mErrorLiveData.setValue(t);
    }

    @Override
    public void onComplete() {

    }
}
