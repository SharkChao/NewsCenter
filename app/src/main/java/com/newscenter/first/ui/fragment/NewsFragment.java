package com.newscenter.first.ui.fragment;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.databinding.FragmentNewsBinding;
import com.newscenter.first.ui.adapter.TabPagerAdapter;
import com.newscenter.first.viewmodel.base.BaseViewModel;


/**
 * Created by Admin on 2018/4/2.
 * 返回头条，社会，国内，娱乐，体育，军事，科技，财经，时尚等新闻信息
 */
@ContentView(R.layout.fragment_news)
public class NewsFragment extends BaseFragment<BaseViewModel> {
    private FragmentNewsBinding mBinding;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void handleArguments(Bundle arguments) {

    }

    @Override
    protected void initTitle() {
        isShowToolBar(false);
    }

    @Override
    protected void initViews(ViewDataBinding viewDataBinding, Bundle savedInstanceState) {
        mBinding = (FragmentNewsBinding) viewDataBinding;
        mTabLayout = mBinding.tabLayout;
        mViewPager = mBinding.viewPager;
    }

    @Override
    protected void initData(ViewModel baseViewModel) {
        mViewPager.setAdapter(new TabPagerAdapter(getFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager,true);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void lazyLoad() {

    }
}
