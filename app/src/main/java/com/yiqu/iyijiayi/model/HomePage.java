package com.yiqu.iyijiayi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */

public class HomePage implements Serializable{

    public UserInfo user;
    public ArrayList<Sound> sound;

    @Override
    public String toString() {
        return "HomePage{" +
                "agreement=" + user +
                ", sound=" + sound +
                '}';
    }
}
