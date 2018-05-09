package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.viewmodel.base.BaseViewModel;


/**
 * Created by Admin on 2018/2/8.
 */
@ContentView(R.layout.wn_app_about_us_activity)
public class AboutUsActivity extends BaseActivity<BaseViewModel> {

    TextView tvVersion;


    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("关于我们");


    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText("Android版本："+ CommonUtil.getAppVersionName(this));
    }

    @Override
    public void initData(ViewModel baseViewModel) {

    }

    @Override
    protected void initEvent() {

    }
    public static void startAboutUsActivity(Context context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

}
