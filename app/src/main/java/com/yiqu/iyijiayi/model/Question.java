package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/31.
 */

public class Question implements Serializable{

    public int id;
    public int sid; //我问ID
    public String desc;
    public int fromuid;
    public int soundtime;
    public String soundpath;
    public int touid;
    public int commenttime;// 评论时长
    public String commentpath;
    public long created;
    public long edited;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", sid=" + sid +
                ", desc='" + desc + '\'' +
                ", fromuid=" + fromuid +
                ", soundtime=" + soundtime +
                ", soundpath='" + soundpath + '\'' +
                ", touid=" + touid +
                ", commenttime=" + commenttime +
                ", commentpath='" + commentpath + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}
