package com.yang.tvlauncher.bean;


/**
 * Created by
 * yangshuang on 2018/11/30.
 */


public class HomeRowBean  extends BaseListItem{

    /**
     * rid : 1
     * name : 直播
     * position : 0
     */

    private int rid;
    private String name;
    private int position;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
