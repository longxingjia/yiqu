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
    public  String id_img;
    public  String teacher_img;
    public  String desc;
    public  String phone;
    public  String address;
    public  String source;
    public  String reason;
    public  int status;
    public  long created;
    public  long edited;

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
