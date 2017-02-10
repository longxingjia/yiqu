package com.yiqu.iyijiayi.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/4/16 0016.
 */
public class AccessTokenEntityResponse implements Serializable {

    AccessTokenEntity data;
//    Meta meta;

    public AccessTokenEntity getData() {
        return data;
    }

    public void setData(AccessTokenEntity data) {
        this.data = data;
    }
//
//    public Meta getMeta() {
//        return meta;
//    }
//
//    public void setMeta(Meta meta) {
//        this.meta = meta;
//    }
}
