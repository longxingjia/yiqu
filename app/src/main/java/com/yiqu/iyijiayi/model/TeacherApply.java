package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/3/6.
 */

public class TeacherApply {

    public  int id;
    public  int uid;
    public  String username;
    public  String school;
    public  String title;
    public  String desc;
    public  int reason;
    public  int status;

    @Override
    public String toString() {
        return "TeacherApply{" +
                "id=" + id +
                ", uid=" + uid +
                ", username='" + username + '\'' +
                ", school='" + school + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", reason=" + reason +
                ", status=" + status +
                '}';
    }
}
