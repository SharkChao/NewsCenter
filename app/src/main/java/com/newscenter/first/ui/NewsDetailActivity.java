package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
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

import java.util.ArrayList;
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
    private News mNews;

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
        String url = getIntent().getStringExtra("url");
        mMyWebView.setOpenUrl(url);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightCode == 1) {
                    rightCode = 0;
                    setRightTitleAndIcon("收藏", R.mipmap.iv_like_no);
                    if(mNews != null){
                        mNewsList.remove(mNews);
                        CommonUtil.setShardPString(Constants.IS_COLLECTION,new Gson().toJson(mNewsList));
                    }
                } else {
                    rightCode = 1;
                    News news = new News();
                    news.setUrl(url);
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
            for (News news: mNewsList){
                if (news.getUrl().equals(url)) {
                    rightCode = 1;
                    mNews = news;
                    setRightTitleAndIcon("取消收藏", R.mipmap.iv_like_yes, listener);
                    return;
                }
            }
        }


    }

    @Override
    protected void initEvent() {

    }
}
