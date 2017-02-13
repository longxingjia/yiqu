package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Xizuo {

    public int sid;
    public int type;
    public int isformulation;
    public int mid;
    public int fromuid;
    public int touid;
    public int views;
    public int commenttime;
    public int questionprice;
    public int like;
    public int isopen;
    public int ispay;
    public int isreply;
    public int status;
    public int oid;
    public int isnew;
    public int isnewreply;
    public int isnewread;
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
        return "Xizuo{" +
                "sid=" + sid +
                ", type=" + type +
                ", isformulation=" + isformulation +
                ", mid=" + mid +
                ", fromuid=" + fromuid +
                ", touid=" + touid +
                ", views=" + views +
                ", commenttime=" + commenttime +
                ", questionprice=" + questionprice +
                ", like=" + like +
                ", isopen=" + isopen +
                ", ispay=" + ispay +
                ", isreply=" + isreply +
                ", status=" + status +
                ", oid=" + oid +
                ", isnew=" + isnew +
                ", isnewreply=" + isnewreply +
                ", isnewread=" + isnewread +
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
