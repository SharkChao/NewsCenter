package com.newscenter.first.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newscenter.first.R;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.util.CommonUtil;

/**
 * Created by Admin on 2018/2/5.
 */

public class FeedBackListAdapter extends BaseQuickAdapter<FeedBack,BaseViewHolder> {
    public FeedBackListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBack item) {
        TextView btnState = helper.getView(R.id.btnState);
        if (1 == item.getFkzt()){
            btnState.setText("已回复");
            btnState.setTextColor(CommonUtil.getResColor(R.color.font_gray));
            btnState.setBackgroundResource(R.drawable.shape_feedback_item_white);
        }else {
            btnState.setText("回复");
            btnState.setBackgroundResource(R.drawable.shape_feedback_item_red);
            btnState.setTextColor(CommonUtil.getResColor(R.color.white));
        }

        helper.setText(R.id.tvContent,CommonUtil.isStrEmpty(item.getFknr())?"":item.getFknr());
        helper.setText(R.id.tvDate,CommonUtil.isStrEmpty(item.getFksj())?"":item.getFksj());
    }
}
