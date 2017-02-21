package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Discovery {

    public int sid;
    public int type;
    public int stype;
    public int isfree;
    public String desc;
    public int fromuid;  // 提问人
    public int mid;
    public int isformulation;  //是否艺考曲目
    public int touid;  //被提问老师UID
    public int views;//浏览量
    public int commenttime;
    public int questionprice;
    public int listenprice;

    public int like;
    public int isopen;
    public int ispay;
    public int isreply;
    public int status;
    public int oid;
    public int isnew;
    public int isnewreply;
    public int isnewread;
    public String musicname;
    public String musictype;
    public String accompaniment;
    public String chapter;
    public String soundtime;
    public String soundpath;
    public String commentpath;
    public long created;
    public long edited;
    public String stuname;
    public String stuimage;
    public String tecname;
    public String tecimage;
    public String tectitle;

    @Override
    public String toString() {
        return "Discovery{" +
                "sid=" + sid +
                ", type=" + type +
                ", stype=" + stype +
                ", isfree=" + isfree +
                ", desc='" + desc + '\'' +
                ", fromuid=" + fromuid +
                ", mid=" + mid +
                ", isformulation=" + isformulation +
                ", touid=" + touid +
                ", views=" + views +
                ", commenttime=" + commenttime +
                ", questionprice=" + questionprice +
                ", listenprice=" + listenprice +
                ", like=" + like +
                ", isopen=" + isopen +
                ", ispay=" + ispay +
                ", isreply=" + isreply +
                ", status=" + status +
                ", oid=" + oid +
                ", isnew=" + isnew +
                ", isnewreply=" + isnewreply +
                ", isnewread=" + isnewread +
                ", musicname='" + musicname + '\'' +
                ", musictype='" + musictype + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", chapter='" + chapter + '\'' +
                ", soundtime='" + soundtime + '\'' +
                ", soundpath='" + soundpath + '\'' +
                ", commentpath='" + commentpath + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                ", stuname='" + stuname + '\'' +
                ", stuimage='" + stuimage + '\'' +
                ", tecname='" + tecname + '\'' +
                ", tecimage='" + tecimage + '\'' +
                ", tectitle='" + tectitle + '\'' +
                '}';
    }
}
