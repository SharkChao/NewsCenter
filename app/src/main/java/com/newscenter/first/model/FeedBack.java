package com.newscenter.first.model;

import java.io.Serializable;

/**
 * 意见反馈model
 * Created by yuzhijun on 2018/3/5.
 */
public class FeedBack implements Serializable {
    private String fknr;//反馈内容
    private String fksj;//反馈时间
    private String hfnr;//回复内容
    private int fkzt;//回复状态 0未回复，1是已回复

    public String getFknr() {
        return fknr;
    }

    public void setFknr(String fknr) {
        this.fknr = fknr;
    }

    public String getFksj() {
        return fksj;
    }

    public void setFksj(String fksj) {
        this.fksj = fksj;
    }

    public String getHfnr() {
        return hfnr;
    }

    public void setHfnr(String hfnr) {
        this.hfnr = hfnr;
    }

    public int getFkzt() {
        return fkzt;
    }

    public void setFkzt(int fkzt) {
        this.fkzt = fkzt;
    }
}
