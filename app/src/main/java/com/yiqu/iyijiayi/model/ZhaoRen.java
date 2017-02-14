package com.yiqu.iyijiayi.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ZhaoRen {

    public ArrayList<Teacher> teacher;
    public ArrayList<Student> student;

    @Override
    public String toString() {
        return "Remen{" +
                "sound=" + teacher +
                ", xizuo=" + student +
                '}';
    }
}
