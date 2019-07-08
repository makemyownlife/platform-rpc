package com.courage.platform.client.config;

/**
 * 应用配置信息
 * Created by zhangyong on 2019/7/7.
 */
public class ApplicationConfig {

    public ApplicationConfig(String appName, String appKey) {
        this.appName = appName;
        this.appKey = appKey;
    }

    private String appName;

    private String appKey;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


}
