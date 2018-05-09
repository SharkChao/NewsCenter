package com.newscenter.first.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.newscenter.first.R;
import com.newscenter.first.model.HistoryNewsParent;
import com.newscenter.first.model.HistoryNewsSon;
import com.oushangfeng.pinnedsectionitemdecoration.utils.FullSpanUtil;

import java.util.List;

public class HistoryAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    private boolean isChange;
    private boolean mAllDelete;
    private onCheckboxSelectListener mSelectListener;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HistoryAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.activity_history_parent);
        addItemType(TYPE_LEVEL_1, R.layout.activity_history_son);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case TYPE_LEVEL_0:
                HistoryNewsParent parent = (HistoryNewsParent) item;
                helper.setText(R.id.tvTitle,parent.getTitle().contains("今天")?"今天":parent.getTitle().contains("昨天")?"昨天":parent.getTitle());
                break;
            case TYPE_LEVEL_1:
                HistoryNewsSon son = (HistoryNewsSon) item;
                helper.setText(R.id.tvTitle,son.getTitle());
                CheckBox checkBox = helper.getView(R.id.checkbox);
                checkBox.setChecked(son.isChecked());
                checkBox.setVisibility(isChange?View.VISIBLE:View.GONE);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isChange){
                            checkBox.setChecked(!checkBox.isChecked());
                            if (checkBox.isChecked()){
                                mSelectListener.onSelect(helper.getLayoutPosition());
                            }else {
                                if (mSelectListener != null){
                                    mSelectListener.unSelect(helper.getLayoutPosition());
                                }
                            }
                        }else {
                            checkBox.setChecked(false);
                            if (mSelectListener != null){
                                mSelectListener.onSelect(helper.getLayoutPosition());
                            }
                        }


                    }
                });
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_LEVEL_0);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this,TYPE_LEVEL_1);
    }


    public interface onCheckboxSelectListener{
        void onSelect(int position);
        void  unSelect(int position);
    }
    public void setOnBoxSelectListener(onCheckboxSelectListener listener){
        mSelectListener = listener;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
    public void allDelete(boolean b){
        mAllDelete = b;
    }
}
