package com.newscenter.first.ui.fragment;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.databinding.FragmentMineBinding;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.MyItemOne;
import com.newscenter.first.views.WaveHelper;
import com.newscenter.first.views.WaveView;

import skin.support.SkinCompatManager;


/**
 * Created by Admin on 2018/2/1.
 */
@ContentView(R.layout.fragment_mine)
public class MineFragment extends BaseFragment<BaseViewModel> {

    MyItemOne lrvShare;
    MyItemOne lrvOpinion;
    MyItemOne lrvSetting;
    WaveView mWaveView;
    private WaveHelper mWaveHelper;
    private TextView mTvSkin;
    private LinearLayout mLlHistory;
    private LinearLayout mLlCollection;

    @Override
    protected void handleArguments(Bundle arguments) {

    }

    @Override
    protected void initTitle() {
        isShowToolBar(false);
    }

    @Override
    protected void initViews(ViewDataBinding viewDataBinding, Bundle savedInstanceState) {
        FragmentMineBinding binding = (FragmentMineBinding) viewDataBinding;
         lrvOpinion = binding.lrvOpinion;
         lrvSetting = binding.lrvSetting;
         lrvShare = binding.lrvShare;
        mWaveView = binding.waveView;
        mTvSkin = binding.tvSkin;
        mLlHistory = binding.llHistory;
        mLlCollection = binding.llCollection;
    }

    @Override
    protected void initData(ViewModel baseViewModel) {
        //设置波浪线
        setWaveView();
        setItemInfos();
        String isNight = CommonUtil.getShardPStringByKey(Constants.IS_NIGHT);
        if ("normal".equals(isNight)){
            //转换成夜间模式
            mTvSkin.setText("夜间");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_night);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }else{
            //转换成白天模式
            mTvSkin.setText("白天");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        }
    }

    @Override
    protected void initEvent() {
        mTvSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             changeSkin();
            }
        });
        mLlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/HistoryActivity")
                        .navigation();
            }
        });
        mLlCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/CollectionActivity")
                        .navigation();
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }
    private void setWaveView() {
        mWaveView.setShowWave(true);
        mWaveHelper = new WaveHelper(mWaveView);
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper.start();
    }

    private void setItemInfos() {
        lrvShare.setItemInfo(R.mipmap.wn_iv_my_process, "分享软件", "");
        lrvOpinion.setItemInfo(R.mipmap.wn_iv_my_opinion, "意见反馈", "");
        lrvSetting.setItemInfo(R.mipmap.wn_iv_my_setting, "设置", "");
    }

    public void changeSkin(){
        String isNight = CommonUtil.getShardPStringByKey(Constants.IS_NIGHT);
        if ("normal".equals(isNight)){
            //转换成夜间模式
            CommonUtil.setShardPString(Constants.IS_NIGHT,"night");
            mTvSkin.setText("白天");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        }else{
            //转换成白天模式
            CommonUtil.setShardPString(Constants.IS_NIGHT,"normal");
            mTvSkin.setText("夜间");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_night);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
    }
}
