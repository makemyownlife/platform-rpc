package com.courage.platform.client.rpc;

/**
 * 远程服务生产者
 * Created by zhangyong on 2019/7/5.
 */
public interface RpcProducerClient {

    void start();

    void shutdown();

    int localListenPort();

}
