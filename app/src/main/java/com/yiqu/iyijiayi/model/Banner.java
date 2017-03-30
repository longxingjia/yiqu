package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/3/29.
 */

public class Banner {

    public int id;
    public int status;
    public String title;
    public String desc;
    public String url;
    public String image;
    public long created;
    public long edited;

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}
