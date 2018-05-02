package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.widget.TextView;

import com.newscenter.com.newscenter.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.model.GirlsData;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.DynamicGirlsViewModel;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity<DynamicGirlsViewModel> {
    private DynamicGirlsViewModel mGirlsViewModel;
    private TextView tvTest;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("测试");
        isShowLeft(true);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        tvTest = findViewById(R.id.tvTest);
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mGirlsViewModel = (DynamicGirlsViewModel) baseViewModel;
        mGirlsViewModel.getELiveObservableData(Constants.GIRLS_URL, "3").observe(MainActivity.this, girlsData -> {
            if (null != girlsData){
//                List<GirlsData.ResultsBean> resultsBeans = girlsData.getResults();
            }
        });
    }

    @Override
    protected void initEvent() {
        tvTest.setOnClickListener(view -> mGirlsViewModel.getGirlsData("2","3").observe(MainActivity.this, girlsData -> {
            if (null != girlsData){
//                List<GirlsData.ResultsBean> resultsBeans = girlsData.getResults();
            }
        }));
    }

    @Override
    public void onRepsonseError(Throwable throwable) {
        super.onRepsonseError(throwable);
    }
}
