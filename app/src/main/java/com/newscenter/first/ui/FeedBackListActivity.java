package com.newscenter.first.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityFeedbackListBinding;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.User;
import com.newscenter.first.ui.adapter.FeedBackListAdapter;
import com.newscenter.first.ui.adapter.HistoryAdapter;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.FeebackListViewModel;
import com.newscenter.first.views.EmptyView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2018/2/8.
 */
@Route(path = "/center/FeedBackListActivity")
@ContentView(R.layout.activity_feedback_list)
public class FeedBackListActivity extends BaseActivity<FeebackListViewModel> {

    private EmptyView mEmptyView;
    private RecyclerView mRvList;
    private List<FeedBack>mFeedBackList = new ArrayList<>();
    private FeedBackListAdapter mAdapter;
    private Button mBtnNewFeedBack;
    private FeebackListViewModel mViewModel;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("意见反馈");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityFeedbackListBinding binding = (ActivityFeedbackListBinding) viewDataBinding;
        mRvList = binding.rvList;
        mBtnNewFeedBack = binding.btnNewFeedBack;
        mEmptyView = new EmptyView(mRvList.getContext(), (ViewGroup) mRvList.getParent());
        mEmptyView.setType(EmptyView.TYPE_NO_DATA);
        mRvList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new FeedBackListAdapter(R.layout.wn_feed_back_list_item);
        mAdapter.setEmptyView(mEmptyView.getView());
        mRvList.setAdapter(mAdapter);
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mViewModel = (FeebackListViewModel) baseViewModel;
        mViewModel.registerData.observe(this, new Observer<List<FeedBack>>() {
            @Override
            public void onChanged(@Nullable List<FeedBack> feedBacks) {
                if (feedBacks != null && feedBacks.size() >= 0){
                    mAdapter.setNewData(feedBacks);
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        mBtnNewFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/FeedBackNewActivity")
                        .navigation();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance()
                        .build("/center/FeedBackNewActivity")
                        .withSerializable("feedback",mAdapter.getData().get(position))
                        .navigation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
        User user = new Gson().fromJson(json,User.class);
        mViewModel.getFeedBackListController(user.getU_name());
    }
}
