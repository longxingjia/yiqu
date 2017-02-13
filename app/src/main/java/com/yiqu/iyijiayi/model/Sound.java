package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Sound {
    public int sid;
    public int stype;
    public int type;
    public int mid;
    public int isformulation;
    public int fromuid;
    public int touid;
    public int commenttime;
    public int questionprice;
    public int views;
    public int like;
    public int isopen;
    public int listenprice;
    public int ispay;
    public int isreply;
    public int status;
    public int oid;
    public int isnew;
    public int isnewreply;
    public int isnewread;
    public int listen;
    public String desc;
    public String musicname;
    public String musictype;
    public String accompaniment;
    public String chapter;
    public String soundtime;
    public String soundpath;
    public String commentpath;
    public String created;
    public String edited;
    public String stuname;
    public String stuimage;
    public String tecname;
    public String tecimage;
    public String tectitle;

    @Override
    public String toString() {
        return "Sound{" +
                "sid=" + sid +
                ", stype=" + stype +
                ", type=" + type +
                ", mid=" + mid +
                ", isformulation=" + isformulation +
                ", fromuid=" + fromuid +
                ", touid=" + touid +
                ", commenttime=" + commenttime +
                ", questionprice=" + questionprice +
                ", views=" + views +
                ", like=" + like +
                ", isopen=" + isopen +
                ", listenprice=" + listenprice +
                ", ispay=" + ispay +
                ", isreply=" + isreply +
                ", status=" + status +
                ", oid=" + oid +
                ", isnew=" + isnew +
                ", isnewreply=" + isnewreply +
                ", isnewread=" + isnewread +
                ", listen=" + listen +
                ", desc='" + desc + '\'' +
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
