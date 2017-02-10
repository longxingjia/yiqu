package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/10.
 */

public class WechatUserInfo {
    public  String openid;
    public  String nickname;
    public  String sex;
    public  String province;
    public  String city;
    public  String country;
    public  String headimgurl;

    public  String unionid;

    @Override
    public String toString() {
        return "WechatUserInfo{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +

                ", unionid='" + unionid + '\'' +
                '}';
    }
}
