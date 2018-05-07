package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityHomeBinding;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.ui.fragment.ConnectFragment;
import com.newscenter.first.ui.fragment.MineFragment;
import com.newscenter.first.ui.fragment.NewsFragment;
import com.newscenter.first.viewmodel.base.BaseViewModel;

@Route(path = "/center/HomeActivity")
@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity<BaseViewModel> {

    private BottomNavigationBar mBottomBar;
    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;
    private NewsFragment mNewsFragment;
    private ConnectFragment mConnectFragment;
    private MineFragment mMineFragment;

    @Override
    public void initTitle() {
        setCenterTitle("首页");
        isShowToolBar(false);
        isShowLeft(false);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityHomeBinding binding = (ActivityHomeBinding) viewDataBinding;
        mBottomBar = binding.bottomBar;
        mFrameLayout = binding.frameLayout;
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mFragmentManager = getSupportFragmentManager();
        if (mNewsFragment == null){
            mNewsFragment = new NewsFragment();
        }
        if (mConnectFragment == null){
            mConnectFragment = new ConnectFragment();
        }
        if (mMineFragment == null){
            mMineFragment = new MineFragment();
        }
        mFragmentManager.beginTransaction().add(R.id.frameLayout,mNewsFragment).add(R.id.frameLayout,mConnectFragment).add(R.id.frameLayout,mMineFragment).commit();
        mFragmentManager.beginTransaction().show(mNewsFragment).hide(mConnectFragment).hide(mMineFragment).commit();
        mBottomBar
                .addItem(new BottomNavigationItem(R.mipmap.iv_news, "新闻").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.iv_group, "交流").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.iv_mine, "我的").setActiveColorResource(R.color.colorPrimary))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化

    }

    @Override
    protected void initEvent() {
        mBottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0){
                    mFragmentManager.beginTransaction().show(mNewsFragment).hide(mConnectFragment).hide(mMineFragment).commit();
                }else if (position == 1){
                    mFragmentManager.beginTransaction().show(mConnectFragment).hide(mNewsFragment).hide(mMineFragment).commit();

                }else if (position == 2){
                    mFragmentManager.beginTransaction().show(mMineFragment).hide(mNewsFragment).hide(mConnectFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onRepsonseError(Throwable throwable) {
        super.onRepsonseError(throwable);
    }
}
