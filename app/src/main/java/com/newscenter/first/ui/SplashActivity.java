package com.newscenter.first.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;

import com.alibaba.android.arouter.launcher.ARouter;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.viewmodel.base.BaseViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity<BaseViewModel>{

    @Override
    public void initTitle() {
        isShowToolBar(false);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {

    }

    @Override
    public void initData(ViewModel baseViewModel) {

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initEvent() {
        //先创建被观察者，以及被观察者被注册之后发送的事件
        Observable.create((ObservableOnSubscribe<Integer>) e -> e.onNext(0)).delay(2, TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 0){
                    ARouter.getInstance()
                            .build("/center/RegisterActivity")
                            .navigation();
                    finish();

                }
            }
        });
    }
}
