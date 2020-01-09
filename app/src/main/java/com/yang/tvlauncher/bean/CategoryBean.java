package com.yang.tvlauncher.bean;


/**
 * Created by
 * yangshuang on 2018/11/30.
 */

public class CategoryBean extends BaseListItem{


    /**
     * cid : 101
     * name : 点播
     */

    private int cid;
    private String name;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
