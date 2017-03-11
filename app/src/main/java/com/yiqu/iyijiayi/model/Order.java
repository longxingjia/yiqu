package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Order implements Serializable {


    public int oid;
    public String order_number;
    public int type; // 1:提问 2:试听
    public int sid;//问题sid
    public int uid;//用户uid
    public String openid;//用户uid
    public String device_info;
    public String payment_content;
    public int payment;//1:微信支付
    public int price;//价格
    public int status;//
    public int created;//
    public int edited;//
    public String isincome;//
    public int isfree;//

    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", order_number='" + order_number + '\'' +
                ", type=" + type +
                ", sid=" + sid +
                ", uid=" + uid +
                ", openid=" + openid +
                ", device_info='" + device_info + '\'' +
                ", payment_content='" + payment_content + '\'' +
                ", payment=" + payment +
                ", price=" + price +
                ", status=" + status +
                ", created=" + created +
                ", edited=" + edited +
                ", isincome=" + isincome +
                ", isfree=" + isfree +
                '}';
    }


}
