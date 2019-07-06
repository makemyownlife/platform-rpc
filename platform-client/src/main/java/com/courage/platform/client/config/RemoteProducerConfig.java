package com.courage.platform.client.config;

/**
 * rpc 生产者配置
 * Created by zhangyong on 2019/7/5.
 */
public class RemoteProducerConfig {

    //默认监听端口
    private Integer port = 10029;

    //生产者线程处理数 默认100
    private Integer threadCount = 100;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

}
