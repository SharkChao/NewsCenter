package com.newscenter.first.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newscenter.first.R;
import com.newscenter.first.model.News;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.viewmodel.base.BaseViewModel;

import java.util.List;

public class NewsAdapter extends BaseMultiItemQuickAdapter<News,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsAdapter(List<News> data) {
        super(data);
        addItemType(News.TYPE_IMG0, R.layout.fragment_news_type0);
        addItemType(News.TYPE_IMG1, R.layout.fragment_news_type1);
        addItemType(News.TYPE_IMG3, R.layout.fragment_news_type3);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        switch (helper.getItemViewType()){
            case News.TYPE_IMG0:
                helper.setText(R.id.tvTitle, CommonUtil.isStrEmpty(item.getTitle())?"":item.getTitle());
                helper.setText(R.id.tvFrom,CommonUtil.isStrEmpty(item.getAuthor_name())?"":item.getAuthor_name());
                helper.setText(R.id.tvDate,CommonUtil.isStrEmpty(item.getDate())?"":item.getDate());
                break;
            case News.TYPE_IMG1:
                helper.setText(R.id.tvTitle, CommonUtil.isStrEmpty(item.getTitle())?"":item.getTitle());
                helper.setText(R.id.tvFrom,CommonUtil.isStrEmpty(item.getAuthor_name())?"":item.getAuthor_name());
                helper.setText(R.id.tvDate,CommonUtil.isStrEmpty(item.getDate())?"":item.getDate());
                ImageView ivImg1 = helper.getView(R.id.ivImg1);
                Glide.with(helper.getConvertView().getContext()).load(item.getThumbnail_pic_s()).into(ivImg1);
                break;
            case News.TYPE_IMG3:
                helper.setText(R.id.tvTitle, CommonUtil.isStrEmpty(item.getTitle())?"":item.getTitle());
                helper.setText(R.id.tvFrom,CommonUtil.isStrEmpty(item.getAuthor_name())?"":item.getAuthor_name());
                helper.setText(R.id.tvDate,CommonUtil.isStrEmpty(item.getDate())?"":item.getDate());
                ImageView ivImg2 = helper.getView(R.id.ivImg1);
                ImageView ivImg3 = helper.getView(R.id.ivImg2);
                ImageView ivImg4 = helper.getView(R.id.ivImg3);
                Glide.with(helper.getConvertView().getContext()).load(item.getThumbnail_pic_s()).into(ivImg2);
                Glide.with(helper.getConvertView().getContext()).load(item.getThumbnail_pic_s02()).into(ivImg3);
                Glide.with(helper.getConvertView().getContext()).load(item.getThumbnail_pic_s03()).into(ivImg4);
                break;
        }
    }


}
