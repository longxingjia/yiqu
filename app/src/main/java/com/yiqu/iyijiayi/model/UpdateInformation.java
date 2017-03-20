package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/3/15.
 */

public class UpdateInformation {
    public  int id = 1;

    public  String version ;// 本地版本名
    public  String content ;
    public  String url ;// 升级包获取地址
    public  String ismust ;// 1 强制更新

    @Override
    public String toString() {
        return "UpdateInformation{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", ismust='" + ismust + '\'' +
                '}';
    }
}
