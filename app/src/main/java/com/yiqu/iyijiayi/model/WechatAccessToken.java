package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/10.
 */

public class WechatAccessToken {

    public  String access_token;
    public  String expires_in;
    public  String refresh_token;
    public  String openid;
    public  String scope;
    public  String unionid;

    @Override
    public String toString() {
        return "WechatAccessToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
