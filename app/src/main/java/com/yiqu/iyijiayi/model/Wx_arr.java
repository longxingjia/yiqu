package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/9.
 */

public class Wx_arr implements Serializable{

    public String appid;
    public String partnerid;
    public String prepayid;
//    public String packag;
    public String noncestr;
    public String timestamp;
    public String sign;

    @Override
    public String toString() {
        return "Wx_arr{" +
                "appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
