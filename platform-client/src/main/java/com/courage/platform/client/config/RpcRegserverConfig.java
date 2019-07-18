package com.courage.platform.client.config;

/**
 * nacos rpc注册配置
 * Created by zhangyong on 2019/7/18.
 */
public class RpcRegserverConfig {

    private String address;

    private String namespace;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

}
