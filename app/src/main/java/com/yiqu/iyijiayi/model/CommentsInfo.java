package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/9.
 */

public class CommentsInfo implements Serializable {

  // .jp！","status":"1","isread":"0","created":"1489718517","edited":"1489718517"}]', bool='1'}


    public String id;//uid
    public String cid;//微
    public String fromuid;//
    public String fromusername;//
    public String touid;//
    public String tousername;//
    public String fromuserimage;//
    public String touserimage;//
    public String comment;//
    public String status;//
    public String isread;//
    public long created;//
    public String edited;//

    @Override
    public String toString() {
        return "CommentsInfo{" +
                "id='" + id + '\'' +
                ", cid='" + cid + '\'' +
                ", fromuid='" + fromuid + '\'' +
                ", fromusername='" + fromusername + '\'' +
                ", touid='" + touid + '\'' +
                ", tousername='" + tousername + '\'' +
                ", fromuserimage='" + fromuserimage + '\'' +
                ", touserimage='" + touserimage + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", isread='" + isread + '\'' +
                ", created=" + created +
                ", edited='" + edited + '\'' +
                '}';
    }
}
