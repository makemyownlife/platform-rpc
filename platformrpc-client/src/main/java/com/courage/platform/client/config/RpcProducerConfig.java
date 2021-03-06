package com.courage.platform.client.config;

/**
 * rpc 生产者配置
 * Created by zhangyong on 2019/7/5.
 */
public class RpcProducerConfig {

    //默认监听端口
    private Integer port = 10029;

    //服务端链接删除时间 (1个小时)
    private int maxIdleTime = 3600;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

}
