package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityNewsDatailBinding;
import com.newscenter.first.model.News;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.MyWebView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Admin on 2018/4/4.
 */

@Route(path = "/center/NewsDetailActivity")
@ContentView(R.layout.activity_news_datail)
public class NewsDetailActivity extends BaseActivity<BaseViewModel> {

    private ActivityNewsDatailBinding mBinding;
    private MyWebView mMyWebView;
    private int rightCode = 0;//0标识未收藏
    private List<News> mNewsList = new ArrayList<>();

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("新闻详情");
        isShowRight(true);

    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        mBinding = (ActivityNewsDatailBinding) viewDataBinding;
        mMyWebView = mBinding.myWebView;
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        Bundle bundle_news = getIntent().getBundleExtra("bundle_news");
        News news = (News) bundle_news.getSerializable("news");
        addToHistory(news);
        mMyWebView.setOpenUrl(news.getUrl());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightCode == 1) {
                    rightCode = 0;
                    setRightTitleAndIcon("收藏", R.mipmap.iv_like_no);
                    mNewsList.remove(news);
                    CommonUtil.setShardPString(Constants.IS_COLLECTION,new Gson().toJson(mNewsList));
                } else {
                    rightCode = 1;
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateNowStr = sdf.format(date);
                    news.setCollectDate(dateNowStr);
                    mNewsList.add(news);
                    CommonUtil.setShardPString(Constants.IS_COLLECTION,new Gson().toJson(mNewsList));
                    setRightTitleAndIcon("取消收藏", R.mipmap.iv_like_yes);
                }
            }
        };
        setRightTitleAndIcon("收藏", R.mipmap.iv_like_no, listener);
        String json = CommonUtil.getShardPStringByKey(Constants.IS_COLLECTION);
        if (!CommonUtil.isStrEmpty(json)){
            mNewsList = new Gson().fromJson(json, new TypeToken<List<News>>(){}.getType());
            for (News newsTemp: mNewsList){
                if (news.getUrl().equals(newsTemp.getUrl())) {
                    rightCode = 1;
                    setRightTitleAndIcon("取消收藏", R.mipmap.iv_like_yes, listener);
                    return;
                }
            }
        }

    }

    @Override
    protected void initEvent() {

    }
    public void addToHistory(News news){
        List<News>newsList = new ArrayList<>();
        String json = CommonUtil.getShardPStringByKey(Constants.IS_HISTORY);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        news.setHistoryDate(dateNowStr);
        if (CommonUtil.isStrEmpty(json)){
            newsList.add(news);
            CommonUtil.setShardPString(Constants.IS_HISTORY,new Gson().toJson(newsList));
        }else {
            newsList = new Gson().fromJson(json, new TypeToken<List<News>>() {}.getType());
            for (News newsTemp:newsList){
                if (newsTemp.getUrl().equals(news.getUrl())){
                    return;
                }
            }
            newsList.add(news);
            CommonUtil.setShardPString(Constants.IS_HISTORY,new Gson().toJson(newsList));

        }
    }
}
