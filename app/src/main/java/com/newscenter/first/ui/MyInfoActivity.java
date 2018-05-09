package com.newscenter.first.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.model.User;
import com.newscenter.first.util.ActivityStack;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.base.BaseViewModel;


/**
 * Created by Admin on 2018/2/8.
 */
@ContentView(R.layout.wn_my_info_activity)
public class MyInfoActivity extends BaseActivity<BaseViewModel>{

    Button btnLoginOut;
    TextView tvName;
    TextView tvSex;
    TextView tvPhoneNumber;
    TextView tvJobTitle;
    private User mUser;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("基础信息");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        tvName = findViewById(R.id.tvName);
        tvSex = findViewById(R.id.tvSex);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvJobTitle = findViewById(R.id.tvJobTitle);
        btnLoginOut = findViewById(R.id.btnLoginOut);
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
        if (!CommonUtil.isStrEmpty(json)){
            mUser = new Gson().fromJson(json,User.class);
        }
        if (mUser != null){
            tvName.setText(CommonUtil.isStrEmpty(mUser.getXm())?"未知":mUser.getXm());
            tvSex.setText(CommonUtil.isStrEmpty(mUser.getSex())?"未知":mUser.getSex());
            tvJobTitle.setText(CommonUtil.isStrEmpty(mUser.getJob_title())?"未知":mUser.getJob_title());
            tvPhoneNumber.setText(CommonUtil.isStrEmpty(mUser.getPhone_number())?"未知":mUser.getPhone_number());
        }
    }

    @Override
    protected void initEvent() {
        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);
                builder.setTitle("退出");
                builder.setMessage("您确定要退出吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStack.create().finishAll();
                        ARouter.getInstance()
                                .build("/center/LoginActivity")
                                .navigation();
                        CommonUtil.setShardPString(Constants.IS_LOGIN,"");
                    }
                });
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }

}
