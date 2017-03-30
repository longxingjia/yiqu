package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Listened {

    public int sid;
    public String musicname;
    public String musictype;
    public String chapter;
    public String accompaniment;
    public int type;
    public String desc;
    public int fromuid;  // 提问人
    public int mid;
    public int views;//浏览量
    public long edited;
    public int like;
    public String username;
    public String userimage;
    public String title;
    public int uid;  //被提问老师UID
    public String school;

    @Override
    public String toString() {
        return "Listened{" +
                "sid=" + sid +
                ", musicname='" + musicname + '\'' +
                ", musictype='" + musictype + '\'' +
                ", chapter='" + chapter + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", type=" + type +
                ", desc='" + desc + '\'' +
                ", fromuid=" + fromuid +
                ", mid=" + mid +
                ", views=" + views +
                ", edited=" + edited +
                ", like=" + like +
                ", username='" + username + '\'' +
                ", userimage='" + userimage + '\'' +
                ", title='" + title + '\'' +
                ", uid=" + uid +
                ", school='" + school + '\'' +
                '}';
    }
}