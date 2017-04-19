package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Tab2_groups implements Serializable{

    public int id;
    public String type;
    public String group_name;
    public String image;
    public String sort;
    public String status;
    public long created;
    public long edited;


    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", group_name='" + group_name + '\'' +
                ", image='" + image + '\'' +
                ", sort='" + sort + '\'' +
                ", status='" + status + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}
