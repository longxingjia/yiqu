package com.yiqu.iyijiayi.model;

import java.io.Serializable;

public class AccessTokenEntity implements Serializable {

    private String deviceUUID;
    private String accessToken;

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}