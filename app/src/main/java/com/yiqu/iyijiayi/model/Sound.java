package com.yiqu.iyijiayi.model;

import org.w3c.dom.Comment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Sound implements Serializable {
    public int sid; //我问ID
    public int stype; //1问题 2习作
    public int type;  //声乐1，播音2 ,文字提问 3
    public int isfree;
    public String desc;
    public String article_content;
    public String musicname;
    public String musictype;
    public String chapter;
    public String accompaniment;//伴奏乐器
    public int soundtime;
    public String soundpath;
    public int isformulation; //是否艺考曲目
    public String commentpath;
    public int commenttime;// 评论时长
    public int questionprice;  //问题价格
    public int listenprice; //偷听价格
    public int views;// 浏览量
    public int like;  //点赞量
    public String comments;
    public long created;
    public long edited;
    public int fromuid; //提问人
    public int touid;
    public String stuname;
    public String stuimage;
    public String stuspecialities;
    public String tecname;
    public String tecimage;
    public String tectitle;
    public int listen; //0,1 是否不用支付试听
    public int islike;
    public int isopen; //1：公开 0：不公开
    public int ispay; //0:未支付，1：支付成功
    public int mid; //伴奏歌曲ID
    public int isreply; // 导师是否已回复 1:已回复 0:未回复
    public int status; //状态，1：上线 2：下线
    public int oid;
    public int isnew; //是否可以追问
    public int isnewreply; //是否有新追问(导师) 1:有 0:没有
    public String followcount;
    public int isnewread;
    public String lrc_url;
    public String lrcpath;
    public String stuschool;
    public String tecschool;
    public String img1;
//    public ArrayList<String> images;
    public String images;

    public ArrayList<CommentsInfo> top_comments;
    public ArrayList<Question> question;

    @Override
    public String toString() {
        return "Sound{" +
                "sid=" + sid +
                ", islike=" + islike +
                ", images=" + images +
                ", stype=" + stype +
                ", type=" + type +
                ", mid=" + mid +
                ", isformulation=" + isformulation +
                ", fromuid=" + fromuid +
                ", touid=" + touid +
                ", lrc_url=" + lrc_url +
                ", lrcpath=" + lrcpath +
                ", commenttime=" + commenttime +
                ", questionprice=" + questionprice +
                ", views=" + views +
                ", followcount=" + followcount +
                ", like=" + like +
                ", comments=" + comments +
                ", isopen=" + isopen +
                ", listenprice=" + listenprice +
                ", ispay=" + ispay +
                ", isreply=" + isreply +
                ", status=" + status +
                ", oid=" + oid +
                ", isnew=" + isnew +
                ", isnewreply=" + isnewreply +
                ", isnewread=" + isnewread +
                ", listen=" + listen +
                ", desc='" + desc + '\'' +
                ", musicname='" + musicname + '\'' +
                ", musictype='" + musictype + '\'' +
                ", accompaniment='" + accompaniment + '\'' +
                ", chapter='" + chapter + '\'' +
                ", soundtime='" + soundtime + '\'' +
                ", soundpath='" + soundpath + '\'' +
                ", commentpath='" + commentpath + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                ", stuname='" + stuname + '\'' +
                ", stuimage='" + stuimage + '\'' +
                ", tecname='" + tecname + '\'' +
                ", tecimage='" + tecimage + '\'' +
                ", tectitle='" + tectitle + '\'' +
                '}';
    }
}
