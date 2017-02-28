package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Discovery {

    public int sid;
    public int stype;
    public int type;
    public int isfree;
    public String desc;
    public int fromuid;  // 提问人
    public int mid;
    public String musicname;
    public String musictype;
    public String accompaniment;
    public String chapter;
    public String soundtime;
    public String soundpath;
    public int isformulation;  //是否艺考曲目
    public int touid;  //被提问老师UID
    public String commentpath;
    public int commenttime;
    public int questionprice;
    public int listenprice;
    public int views;//浏览量
    public int like;
    public int isopen;
    public int ispay;
    public int isreply;
    public int status;
    public int oid;
    public int isnew;
    public int isnewreply;
    public int isnewread;
    public long created;
    public long edited;

    public String tecname;
    public String tecimage;
    public String tecschool;


    public String stuname;
    public String stuimage;
    public String stuschool;

    @Override
    public String toString() {
        return "Discovery{" +
                "sid=" + sid +
                ", stype=" + stype +
                ", type=" + type +
                ", isfree=" + isfree +
                ", desc='" + desc + '\'' +
                ", fromuid=" + fromuid +
                ", mid=" + mid +
                ", musicname='" + musicname + '\'' +
                ", musictype='" + musictype + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", chapter='" + chapter + '\'' +
                ", soundtime='" + soundtime + '\'' +
                ", soundpath='" + soundpath + '\'' +
                ", isformulation=" + isformulation +
                ", touid=" + touid +
                ", commentpath='" + commentpath + '\'' +
                ", commenttime=" + commenttime +
                ", questionprice=" + questionprice +
                ", listenprice=" + listenprice +
                ", views=" + views +
                ", like=" + like +
                ", isopen=" + isopen +
                ", ispay=" + ispay +
                ", isreply=" + isreply +
                ", status=" + status +
                ", oid=" + oid +
                ", isnew=" + isnew +
                ", isnewreply=" + isnewreply +
                ", isnewread=" + isnewread +
                ", created=" + created +
                ", edited=" + edited +
                ", tecname='" + tecname + '\'' +
                ", tecimage='" + tecimage + '\'' +
                ", tecschool='" + tecschool + '\'' +
                ", stuname='" + stuname + '\'' +
                ", stuimage='" + stuimage + '\'' +
                ", stuschool='" + stuschool + '\'' +
                '}';
    }
}
