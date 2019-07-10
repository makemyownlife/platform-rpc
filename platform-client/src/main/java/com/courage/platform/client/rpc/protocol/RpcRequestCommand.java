package com.courage.platform.client.rpc.protocol;

import java.io.Serializable;

/**
 * rpc请求命令
 * Created by zhangyong on 2019/7/8.
 */
public class RpcRequestCommand implements Serializable {

    private static final long serialVersionUID = -5565366231695911316L;

    private String serviceId;

    private String appName;

    private transient byte[] body;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
