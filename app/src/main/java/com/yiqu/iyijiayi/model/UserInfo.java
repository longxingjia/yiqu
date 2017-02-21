package com.yiqu.iyijiayi.model;

/**
 * Created by Administrator on 2017/2/9.
 */

public class UserInfo {
    public String uid;//uid
    public String openid;//微信用户ID
    public String type;//用户类型1:学生 2:老师
    public String username;// 姓名
    public String phone;//电话
    public String password;// 密码
    public String school;// 学校
    public String desc;//个人介绍
    public String userimage;//  个人图片
    public String province;// 省
    public String city;//市+9--------------------------------------------------------------------------------------------
    public String title;// 教师头衔
    public String sex;// 性别:0女生 1男生
    public String specialities;//学生专业
    public String price;// 老师提问价格
    public String totalincome;// 总收入
    public String questionincome;// 提问收入
    public String listenincome;// 听答案收入
    public String created;//  创建时间(5min)
    public String edited;// 最后修改时间

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", openid='" + openid + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", school='" + school + '\'' +
                ", desc='" + desc + '\'' +
                ", userimage='" + userimage + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", title='" + title + '\'' +
                ", sex='" + sex + '\'' +
                ", specialities='" + specialities + '\'' +
                ", price='" + price + '\'' +
                ", totalincome='" + totalincome + '\'' +
                ", questionincome='" + questionincome + '\'' +
                ", listenincome='" + listenincome + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                '}';
    }
}
