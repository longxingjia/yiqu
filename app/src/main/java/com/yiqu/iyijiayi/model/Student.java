package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/14.
 */

public class Student {

    public String uid;
    public String username;
    public String userimage;
    public String title;
    public String sex;
    public String phone;
    public String created;
    public String edited;
    public String price;
    public String isfollow;

    @Override
    public String toString() {
        return "Student{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", userimage='" + userimage + '\'' +
                ", title='" + title + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                ", price='" + price + '\'' +
                ", isfollow='" + isfollow + '\'' +
                '}';
    }
}
