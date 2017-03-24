package com.yiqu.iyijiayi.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */

public class GlobleSearch {
    public ArrayList<UserInfo> user;
    public ArrayList<Sound> sound1;
    public ArrayList<Sound> sound2;
    public ArrayList<Music> music;

    @Override
    public String toString() {
        return "GlobleSearch{" +
                "user=" + user +
                ", sound1=" + sound1 +
                ", sound2=" + sound2 +
                ", music=" + music +
                '}';
    }
}
