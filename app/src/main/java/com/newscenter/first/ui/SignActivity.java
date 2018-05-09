package com.newscenter.first.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivitySignBinding;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.SignEntity;
import com.newscenter.first.model.User;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.SignViewModel;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.test.sign_calender.DPCManager;
import com.test.sign_calender.DPDecor;
import com.test.sign_calender.DatePicker;
import com.test.sign_calender.DatePicker2;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Route(path = "/center/SignActivity")
@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity<SignViewModel>{

    private DatePicker mDatePicker;
    private SignViewModel mViewModel;
    private boolean isTodaySign;
    private TextView mTvDay;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("签到详情");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivitySignBinding binding = (ActivitySignBinding) viewDataBinding;
        mDatePicker = binding.datePicker;
        mTvDay = binding.tvDay;
        mDatePicker.setFestivalDisplay(true); //是否显示节日
        mDatePicker.setHolidayDisplay(true); //是否显示假期
        mDatePicker.setDeferredDisplay(true); //是否显示补休
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mViewModel = (SignViewModel) baseViewModel;
        mViewModel.registerData.observe(this, new Observer<List<SignEntity>>() {
            @Override
            public void onChanged(@Nullable List<SignEntity> signEntities) {
                if (signEntities != null && signEntities.size() >= 0){
                    List<String>list = new ArrayList<>();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(new Date());
                    for (SignEntity signEntity:signEntities){
                        list.add(signEntity.getDate());
                        if (date.equals(signEntity.getDate())){
                            isTodaySign = true;
                            mDatePicker.setRightTitle(true);
                        }
                    }
                    DPCManager.getInstance().clearnDATE_CACHE();
                    DPCManager.getInstance().setDecorBG(list);
                    mTvDay.setText("您已累计签到  "+list.size()+"  天");
                    mDatePicker.invalidate();
                }
            }
        });
        mViewModel.signData.observe(this, new Observer<List<SignEntity>>() {
            @Override
            public void onChanged(@Nullable List<SignEntity> signEntities) {
                if (signEntities != null && signEntities.size() > 0){
                    List<String>list = new ArrayList<>();
                    for (SignEntity signEntity:signEntities){
                        list.add(signEntity.getDate());
                    }
                    DPCManager.getInstance().clearnDATE_CACHE();
                    DPCManager.getInstance().setDecorBG(list);
                    mTvDay.setText("您已累计签到  "+list.size()+"  天");
                    mDatePicker.invalidate();
                }
            }
        });
        String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
        User user = new Gson().fromJson(json,User.class);
        mViewModel.getSignController(user.getU_name());
    }

    @Override
    protected void initEvent() {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        final int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDatePicker.setDate(mYear, mMonth);
        mDatePicker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.RED);
                paint.setAntiAlias(true);
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.iv_sign);
                canvas.drawBitmap(mBitmap, rect.centerX() - mBitmap.getWidth() / 2f, rect.centerY() - mBitmap.getHeight() / 2f, paint);
            }
        });
        mDatePicker.setLeftTitle(mMonth+"月");

        mDatePicker.setIsScroll(true);
        mDatePicker.setOnClickSignIn(new DatePicker.OnClickSignIn() {
            @Override
            public void signIn() {
                if (!isTodaySign){
                    String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
                    User user = new Gson().fromJson(json,User.class);
                    mDatePicker.setRightTitle(true);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(new Date());
                    mViewModel.setSignController(user.getU_name(),date);
                }else {
                    CommonUtil.showSnackBar(mLlLeft,"您今日已签到!");
                }

            }
        });

    }
}
