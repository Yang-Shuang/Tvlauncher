package com.yang.tvlauncher.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by
 * yangshuang on 2018/11/30.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryBean {


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
