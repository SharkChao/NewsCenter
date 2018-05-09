package com.newscenter.first.ui.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.databinding.FragmentMineBinding;
import com.newscenter.first.model.News;
import com.newscenter.first.ui.MyInfoActivity;
import com.newscenter.first.ui.SettingActivity;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.MyItemOne;
import com.newscenter.first.views.WaveHelper;
import com.newscenter.first.views.WaveView;

import java.io.File;
import java.net.CookieHandler;
import java.util.List;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import me.shaohui.shareutil.ShareUtil;
import me.shaohui.shareutil.share.ShareListener;
import me.shaohui.shareutil.share.SharePlatform;
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
    private TextView mTvSign;
    private TextView tvCollection;
    private TextView tvHistory;
    private TextView tvGT;
    private RelativeLayout leftInfo;


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
        mTvSign = binding.tvSign;
        mLlHistory = binding.llHistory;
        mLlCollection = binding.llCollection;
        tvCollection = binding.tvCollection;
        tvHistory = binding.tvHistory;
        tvGT = binding.tvGT;
        leftInfo = binding.leftInfo;
    }

    @Override
    protected void initData(ViewModel baseViewModel) {
        //设置波浪线
        setWaveView();
        setItemInfos();
        String isNight = CommonUtil.getShardPStringByKey(Constants.IS_NIGHT);
        if ("night".equals(isNight)){
            //转换成白天模式
            mTvSkin.setText("白天");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        }else{
            //转换成夜间模式
            mTvSkin.setText("夜间");
            Drawable drawable = getResources().getDrawable(R.mipmap.iv_skin_night);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            mTvSkin.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
        //初始化分享sdk
        // 初始化shareUtil
        ShareConfig config = ShareConfig.instance()
                .qqId("827623353")
                .weiboId("15235174661")
                .wxId("15235174661");
        ShareManager.init(config);
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
        mTvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/SignActivity")
                        .navigation();
            }
        });
        lrvOpinion.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/FeedBackListActivity")
                        .navigation();
            }
        });
        lrvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SettingActivity.class));
            }
        });
        leftInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
            }
        });
        lrvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View content) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.lx_share_more_dialog_view, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setView(view)
                        .create();
                ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
                ImageView ivShareCode = (ImageView) view.findViewById(R.id.ivShareCode);

                ImageView ivWX = view.findViewById(R.id.iv_wx_friend);
                ImageView ivPY = view.findViewById(R.id.iv_wx_circle);
                ImageView ivQQ = view.findViewById(R.id.iv_qq);
                ImageView ivWB = view.findViewById(R.id.iv_wb);
                Bitmap image = ((BitmapDrawable)ivShareCode.getDrawable()).getBitmap();
                ivWX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareUtil.shareImage(getActivity(), SharePlatform.WX, image, new ShareListener() {
                            @Override
                            public void shareSuccess() {

                            }

                            @Override
                            public void shareFailure(Exception e) {

                            }

                            @Override
                            public void shareCancel() {

                            }
                        });
                    }
                });
                ivPY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareUtil.shareImage(getActivity(), SharePlatform.WX_TIMELINE, image, new ShareListener() {
                            @Override
                            public void shareSuccess() {

                            }

                            @Override
                            public void shareFailure(Exception e) {

                            }

                            @Override
                            public void shareCancel() {

                            }
                        });
                    }
                });
                ivQQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareUtil.shareImage(getActivity(), SharePlatform.QQ, image, new ShareListener() {
                            @Override
                            public void shareSuccess() {

                            }

                            @Override
                            public void shareFailure(Exception e) {

                            }

                            @Override
                            public void shareCancel() {

                            }
                        });
                    }
                });
                ivWB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareUtil.shareImage(getActivity(), SharePlatform.WEIBO, image, new ShareListener() {
                            @Override
                            public void shareSuccess() {

                            }

                            @Override
                            public void shareFailure(Exception e) {

                            }

                            @Override
                            public void shareCancel() {

                            }
                        });
                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        String jsCollection = CommonUtil.getShardPStringByKey(Constants.IS_COLLECTION);
        if (!CommonUtil.isStrEmpty(jsCollection)){
            List<News>collectionList  = new Gson().fromJson(jsCollection, new TypeToken<List<News>>() {}.getType());
            if (collectionList!= null && collectionList.size() >= 0){
                tvCollection.setText(collectionList.size()+"");
            }
        }
        String jsHistory = CommonUtil.getShardPStringByKey(Constants.IS_HISTORY);
        if (!CommonUtil.isStrEmpty(jsHistory)){
            List<News>historyList  = new Gson().fromJson(jsHistory, new TypeToken<List<News>>() {}.getType());
            if (historyList != null && historyList.size() >= 0){
                tvHistory.setText(historyList.size()+"");
            }
        }
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
