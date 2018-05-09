package com.newscenter.first.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CollectionNewsParent extends AbstractExpandableItem<CollectionNewsSon> implements MultiItemEntity{
    private String title;
    @Override
    public int getItemType() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
