package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.viewmodel.base.BaseViewModel;

@Route(path = "/center/HomeActivity")
@ContentView(R.layout.activity_main)
public class HomeActivity extends BaseActivity<BaseViewModel> {
    private TextView tvTest;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("测试");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        tvTest = findViewById(R.id.tvTest);
    }

    @Override
    public void initData(ViewModel baseViewModel) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onRepsonseError(Throwable throwable) {
        super.onRepsonseError(throwable);
    }
}
