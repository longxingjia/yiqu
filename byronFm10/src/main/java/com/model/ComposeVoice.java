package com.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/23.
 */

public class ComposeVoice implements Serializable{

    public String fromuid;
    public String type ;  //1声音 2 播音
    public int mid;
    public String musicname;
    public String musictype;
    public String chapter;
    public String accompaniment;
    public String commentpath;
    public String commenttime;
    public int soundtime;
    public String isformulation;
    public String listenprice;
    public String isopen;
    public String ispay  ;
    public String isreply    ;
    public String status;
    public int touid;
    public String questionprice;
    public String voicename;
    public String soundpath;
    public String desc;
    public long createtime;
    public String isCompose;


    @Override
    public String toString() {
        return "ComposeVoice{" +
                "fromuid='" + fromuid + '\'' +
                ", type='" + type + '\'' +
                ", mid=" + mid +
                ", musicname='" + musicname + '\'' +
                ", musictype='" + musictype + '\'' +
                ", chapter='" + chapter + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", commentpath='" + commentpath + '\'' +
                ", commenttime='" + commenttime + '\'' +
                ", soundtime=" + soundtime +
                ", isformulation='" + isformulation + '\'' +
                ", listenprice='" + listenprice + '\'' +
                ", isopen='" + isopen + '\'' +
                ", ispay='" + ispay + '\'' +
                ", isreply='" + isreply + '\'' +
                ", status='" + status + '\'' +
                ", touid=" + touid +
                ", questionprice='" + questionprice + '\'' +
                ", voicename='" + voicename + '\'' +
                ", soundpath='" + soundpath + '\'' +
                ", desc='" + desc + '\'' +
                ", createtime=" + createtime +
                ", isCompose='" + isCompose + '\'' +
                '}';
    }
}


