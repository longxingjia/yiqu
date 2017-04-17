package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Events implements Serializable{

    public int id;
    public String title;
    public String desc;
    public String image;
    public String sort;
    public String status;
    public long created;
    public long edited;

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", image='" + image + '\'' +
                ", sort='" + sort + '\'' +
                ", status='" + status + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}
