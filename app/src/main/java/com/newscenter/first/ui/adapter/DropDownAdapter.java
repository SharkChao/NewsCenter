package com.newscenter.first.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newscenter.first.R;
import com.newscenter.first.model.KeyValueObject;

/**
 * Created by Admin on 2018/2/5.
 */

public class DropDownAdapter extends BaseQuickAdapter<KeyValueObject,BaseViewHolder> {
    public DropDownAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, KeyValueObject item) {
        helper.setText(R.id.tvProject,item.getValue());
    }
}
