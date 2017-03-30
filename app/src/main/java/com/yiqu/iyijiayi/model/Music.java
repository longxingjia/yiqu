package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Music implements Serializable {
    public int type;
    public int mid;
    public String typename;
    public String image;
    public String musicname;
    public String musicpath;
    public String musictype;
    public String chapter;
    public String accompaniment;
    public int time;
    public String size;
    public String isformulation;
    public int isdecode;
    public long decodetime;
    public long created;
    public long edited;
    public long downloadtime;


    @Override
    public String toString() {
        return "Music{" +
                "type=" + type +
                ", mid=" + mid +
                ", typename='" + typename + '\'' +
                ", image='" + image + '\'' +
                ", musicname='" + musicname + '\'' +
                ", musicpath='" + musicpath + '\'' +
                ", musictype='" + musictype + '\'' +
                ", chapter='" + chapter + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", time='" + time + '\'' +
                ", size='" + size + '\'' +
                ", isformulation='" + isformulation + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                '}';
    }
}
