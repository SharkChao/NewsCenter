package com.newscenter.first.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class HistoryNewsSon extends AbstractExpandableItem<HistoryNewsSon> implements MultiItemEntity {

    private String title;
    private boolean isChecked;
    @Override
    public int getItemType() {
        return 1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
