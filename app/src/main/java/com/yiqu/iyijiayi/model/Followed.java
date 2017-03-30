package com.yiqu.iyijiayi.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Followed {

    public ArrayList<Teacher> teacher;
    public ArrayList<Teacher> student;

    @Override
    public String toString() {
        return "ZhaoRen{" +
                "teacher=" + teacher +
                ", student=" + student +
                '}';
    }
}
