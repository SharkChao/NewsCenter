package com.newscenter.first.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.ui.fragment.OneFragment.NewsFragment1;


/**
 * Created by Admin on 2018/4/4.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {"头条","社会","国内","娱乐","体育","军事","科技","财经","时尚"};
    private NewsFragment1 mNewsFragment1;
    private NewsFragment1 mNewsFragment2;
    private NewsFragment1 mNewsFragment3;
    private NewsFragment1 mNewsFragment4;
    private NewsFragment1 mNewsFragment5;
    private NewsFragment1 mNewsFragment6;
    private NewsFragment1 mNewsFragment7;
    private NewsFragment1 mNewsFragment8;
    private NewsFragment1 mNewsFragment9;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment baseFragment = null;
        switch (position){
            case 0:
                if (mNewsFragment1 == null){
                    mNewsFragment1 = new NewsFragment1("top");
                }
                baseFragment = mNewsFragment1;
                break;
            case 1:
                if (mNewsFragment2 == null){
                    mNewsFragment2 = new NewsFragment1("shehui");
                }
                baseFragment = mNewsFragment2;
                break;
            case 2:
                if (mNewsFragment3 == null){
                    mNewsFragment3 = new NewsFragment1("guonei");
                }
                baseFragment = mNewsFragment3;
                break;
            case 3:
                if (mNewsFragment4 == null){
                    mNewsFragment4 = new NewsFragment1("yule");
                }
                baseFragment = mNewsFragment4;
                break;
            case 4:
                if (mNewsFragment5 == null){
                    mNewsFragment5 = new NewsFragment1("tiyu");
                }
                baseFragment = mNewsFragment5;
                break;
            case 5:
                if (mNewsFragment6 == null){
                    mNewsFragment6 = new NewsFragment1("junshi");
                }
                baseFragment = mNewsFragment6;
                break;
            case 6:
                if (mNewsFragment7 == null){
                    mNewsFragment7 = new NewsFragment1("keji");
                }
                baseFragment = mNewsFragment7;
                break;
            case 7:
                if (mNewsFragment8 == null){
                    mNewsFragment8 = new NewsFragment1("caijing");
                }
                baseFragment = mNewsFragment8;
                break;
            case 8:
                if (mNewsFragment9 == null){
                    mNewsFragment9 = new NewsFragment1("shishang");
                }
                baseFragment = mNewsFragment9;
                break;
        }
        return baseFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
