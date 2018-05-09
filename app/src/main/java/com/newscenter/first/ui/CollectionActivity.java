package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityCollectionBinding;
import com.newscenter.first.databinding.ActivityHistoryBinding;
import com.newscenter.first.model.CollectionNewsParent;
import com.newscenter.first.model.CollectionNewsSon;
import com.newscenter.first.model.News;
import com.newscenter.first.ui.adapter.CollectionAdapter;
import com.newscenter.first.ui.adapter.HistoryAdapter;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.util.TimeUtils;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.EmptyView;
import com.newscenter.first.views.RecycleViewDivider;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;

@Route(path = "/center/CollectionActivity")
@ContentView(R.layout.activity_collection)
public class CollectionActivity extends BaseActivity<BaseViewModel> {

    private SearchView mSearchView;
    private EmptyView mEmptyView;
    private RecyclerView mRvList;
    private LinearLayout bottomLayout;
    private List<MultiItemEntity>mNewsList = new ArrayList<>();
    private List<MultiItemEntity>mNewsListTemp = new ArrayList<>();
    private CollectionAdapter mAdapter;
    private TextView tvDelete;
    private TextView tvAll;
    private List<News> mNewsJson;
    private int count;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        isShowRight(true);
        setCenterTitle("我的收藏");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityCollectionBinding binding = (ActivityCollectionBinding) viewDataBinding;
        mSearchView =   binding.searchView;
        mRvList = binding.rvList;
        bottomLayout = binding.bottomLayout;
        tvAll = binding.tvAll;
        tvDelete = binding.tvDelete;

        mSearchView.findViewById(R.id.search_back).setVisibility(View.INVISIBLE);
        mSearchView.findViewById(R.id.listView).setVisibility(View.GONE);
        mSearchView.findViewById(R.id.tv_clear).setVisibility(View.GONE);
        mEmptyView = new EmptyView(mRvList.getContext(), (ViewGroup) mRvList.getParent());
        mEmptyView.setType(EmptyView.TYPE_NO_DATA);
        mAdapter = new CollectionAdapter(mNewsList);
        mAdapter.setEmptyView(mEmptyView.getView());

        mRvList.addItemDecoration(
                // Set the type of pinned header
                new PinnedHeaderItemDecoration.Builder(HistoryAdapter.TYPE_LEVEL_0)
                        // Set separator line resources id.
                        .setDividerId(R.drawable.divider)
                        // Enable draw the separator line, by default it's disable.
                        .enableDivider(true)
                        // Set click event for the header and its internal child view.
                        // Disable header click event， by default it's enable.
                        .disableHeaderClick(true)
                        // Set the listener. If the listener is not null but disable the header click event(eg. disableHeaderClick(true)), then the callback don't return.
//                        .setHeaderClickListener(clickAdapter)
                        .create());
        mRvList.addItemDecoration(new RecycleViewDivider(CollectionActivity.this, LinearLayoutManager.VERTICAL));
        mRvList.setLayoutManager(new LinearLayoutManager(CollectionActivity.this, LinearLayoutManager.VERTICAL, false));

        mRvList.setAdapter(mAdapter);

        mAdapter.expandAll();
    }

    @Override
    public void initData(ViewModel baseViewModel) {

    }

    @Override
    protected void initEvent() {
        mSearchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                mSearchView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                mSearchView.findViewById(R.id.tv_clear).setVisibility(View.VISIBLE);

                if (CommonUtil.isStrEmpty(string)){
                   mAdapter.setNewData(mNewsList);
                    return;
                }
                Iterator<MultiItemEntity> iterator = mNewsList.iterator();
                for (;iterator.hasNext();){
                    MultiItemEntity next = iterator.next();
                    if (next.getItemType() == 1){
                        CollectionNewsSon son = (CollectionNewsSon) next;
                        if (son.getTitle().contains(string)){
                           mNewsListTemp.add(son);
                        }

                    }
                }
                mAdapter.setNewData(mNewsListTemp);
            }
        });
        setRightTitle("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setChange(!mAdapter.isChange());
                setRightTitle(mAdapter.isChange()?"取消":"编辑");
                bottomLayout.setVisibility(mAdapter.isChange()?View.VISIBLE:View.GONE);
                tvDelete.setTextColor(getResources().getColor(R.color.gray_2));
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.setOnBoxSelectListener(new CollectionAdapter.onCheckboxSelectListener() {
            @Override
            public void onSelect(int position) {
                count++;
                CollectionNewsSon son = (CollectionNewsSon) mNewsList.get(position);
                son.setChecked(true);
                tvDelete.setText("删除("+count+")");
                tvDelete.setTextColor(getResources().getColor(R.color.white));
                if (!mAdapter.isChange()){
                    String json = CommonUtil.getShardPStringByKey(Constants.IS_COLLECTION);
                    List<News>newsList = new Gson().fromJson(json,new TypeToken<List<News>>(){}.getType());
                    for (News news: newsList){
                        if (news.getTitle().equals(son.getTitle())){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("news",news);
                            ARouter.getInstance()
                                    .build("/center/NewsDetailActivity")
                                    .withBundle("bundle_news",bundle)
                                    .navigation();
                            return;
                        }
                    }
                    Toast.makeText(CollectionActivity.this, "出现未知错误!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void unSelect(int position) {
                count--;
                CollectionNewsSon son = (CollectionNewsSon) mNewsList.get(position);
                son.setChecked(false);
                tvDelete.setText("删除("+count+")");
                tvDelete.setTextColor(getResources().getColor(R.color.white));
            }
        });
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                for (MultiItemEntity son:mNewsList){
                    if (son.getItemType() == 1){
                        CollectionNewsSon itemEntity = (CollectionNewsSon) son;
                        itemEntity.setChecked(true);
                        count++;
                    }
                }
                tvDelete.setText("删除("+count+")");
                tvDelete.setTextColor(getResources().getColor(R.color.white));
                mAdapter.notifyDataSetChanged();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Iterator<News> iterator = mNewsJson.iterator();iterator.hasNext();){
                    News next = iterator.next();
                    Iterator<MultiItemEntity> iterator1 = mNewsList.iterator();
                    for (;iterator1.hasNext();){
                        MultiItemEntity son = iterator1.next();
                        if (son.getItemType() ==1 &&((CollectionNewsSon)son).isChecked()&&next.getTitle().equals(((CollectionNewsSon)son).getTitle())){
                            iterator.remove();
                            iterator1.remove();
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                tvDelete.setText("删除");
                tvDelete.setTextColor(getResources().getColor(R.color.gray_2));
                CommonUtil.setShardPString(Constants.IS_COLLECTION,new Gson().toJson(mNewsJson));
                if (mNewsList.size() > 0){
                    CollectionNewsParent parent = (CollectionNewsParent) mNewsList.get(0);
                    if (parent.getSubItems() == null || parent.getSubItems().size() == 0){
                        mNewsList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }else {
                    mNewsList.clear();
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       getNewsData();
    }
    public void getNewsData(){
        String json = CommonUtil.getShardPStringByKey(Constants.IS_COLLECTION);
        if (!CommonUtil.isStrEmpty(json)){
            Date date = new Date();
            Date startDate = TimeUtils.getDateBefore(date, 30);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = sdf.format(date);
            String dateNowStr = sdf.format(startDate);
            try {
                List<String> datePeriod = CommonUtil.getDatePeriod(dateNowStr, dateNow);
                Collections.reverse(datePeriod);
                for (String time:datePeriod){
                    CollectionNewsParent parent = new CollectionNewsParent();
                    parent.setTitle( TimeUtils.getFriendlyTimeSpanByNow(time,new SimpleDateFormat("yyyy-MM-dd")));
                    mNewsJson = new Gson().fromJson(json,new TypeToken<List<News>>(){}.getType());
                    for (News news: mNewsJson){
                        if (news.getCollectDate().equals(time)){
                            CollectionNewsSon son = new CollectionNewsSon();
                            son.setTitle(news.getTitle());
                            son.setAuthor_name(news.getAuthor_name());
                            son.setDate(news.getDate());
                            son.setUrl(news.getUrl());
                            son.setThumbnail_pic_s(news.getThumbnail_pic_s());
                            parent.addSubItem(son);
                        }
                    }
                    mNewsList.add(parent);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            CollectionNewsParent parent = (CollectionNewsParent) mNewsList.get(0);
            if (parent.getSubItems() == null || parent.getSubItems().size() == 0){
                mNewsList.clear();
//                mAdapter.notifyDataSetChanged();
            }
            mAdapter.expandAll();
            mAdapter.notifyDataSetChanged();
        }
    }
}
