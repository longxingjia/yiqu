package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Order implements Serializable {
    public int sid;//问题sid
    public int uid;//用户uid
    public int payment;//1:微信支付
    public int price;//价格
    public String type; // 1:提问 2:试听

}
