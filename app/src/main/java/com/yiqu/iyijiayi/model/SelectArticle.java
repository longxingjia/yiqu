package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SelectArticle implements Serializable{
    public int id; //伴奏歌曲ID
    public int class_id;
    public int event_id;
    public String title;
    public String author;
    public String content;
    public String status;
    public String class_name;
    public String event_name;
    public long created;
    public long edited;

    @Override
    public String toString() {
        return "SelectArticle{" +
                "id=" + id +
                ", class_id=" + class_id +
                ", event_id=" + event_id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", class_name='" + class_name + '\'' +
                ", event_name='" + event_name + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}


