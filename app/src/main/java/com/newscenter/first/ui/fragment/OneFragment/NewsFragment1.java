package com.newscenter.first.ui.fragment.OneFragment;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.databinding.FragmentNews1Binding;
import com.newscenter.first.model.News;
import com.newscenter.first.ui.adapter.NewsAdapter;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.viewmodel.NewsViewModel;
import com.newscenter.first.views.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/4/4.
 * 头条
 */
@ContentView(R.layout.fragment_news1)
public class NewsFragment1 extends BaseFragment<NewsViewModel> {

    private NewsViewModel mViewModel1;
    private RecyclerView mRvList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsAdapter mAdapter;
    private List<News>mNewsList = new ArrayList<>();
    private String type;


    public NewsFragment1() {
    }

    @SuppressLint("ValidFragment")
    public NewsFragment1(String type) {
        this.type = type;
    }


    @Override
    protected void handleArguments(Bundle arguments) {

    }

    @Override
    protected void initTitle() {
        isShowToolBar(false);
    }

    @Override
    protected void initViews(ViewDataBinding viewDataBinding, Bundle savedInstanceState) {
        FragmentNews1Binding binding = (FragmentNews1Binding) viewDataBinding;
        mRvList = binding.rvList;
        mSwipeRefreshLayout = binding.swipeRefreshLayout;

        mRvList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new NewsAdapter(mNewsList);
        //第一次不需要进入加载更多的回调中
        mRvList.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.VERTICAL));
        mRvList.setAdapter(mAdapter);
    }

    @Override
    protected void initData(ViewModel baseViewModel) {
        mViewModel1 = (NewsViewModel) baseViewModel;
        mViewModel1.newsData.observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                if (newsList != null && newsList.size() >= 0){
                    if (mSwipeRefreshLayout.isRefreshing()){
                        Toast.makeText(getActivity(), "刷新数据成功!", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    mNewsList.clear();
                    for (News news:newsList){
                        if (CommonUtil.isStrEmpty(news.getThumbnail_pic_s())){
                            news.setLayout_type(0);
                        }else if (CommonUtil.isStrEmpty(news.getThumbnail_pic_s02())){
                            news.setLayout_type(1);
                        }else if (CommonUtil.isStrEmpty(news.getThumbnail_pic_s03())){
                            news.setLayout_type(1);
                        }else {
                            news.setLayout_type(3);
                        }
                    }
                    mNewsList.addAll(newsList);
                    mAdapter.notifyDataSetChanged();
                }
            }

        });
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRvList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                        mAdapter.loadMoreEnd();
                        return;
                    }
                }, 1000);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel1.newsController(type,true);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance()
                        .build("/center/NewsDetailActivity")
                        .withString("url",mAdapter.getData().get(position).getUrl())
                        .navigation();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        mViewModel1.newsController(type,false);
    }
}
