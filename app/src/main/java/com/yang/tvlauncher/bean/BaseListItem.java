package com.yang.tvlauncher.bean;

public class BaseListItem {
    public static final int ITEM_APP = 0;
    public static final int ITEM_APPS_CATEGORY_TITLE = 1;

    private int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
