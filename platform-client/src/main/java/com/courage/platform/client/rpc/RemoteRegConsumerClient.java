package com.courage.platform.client.rpc;

import com.courage.platform.client.exception.RemoteClientException;

/**
 * 远程服务客户端(实现了注册中心的接口类)
 * Created by zhangyong on 2019/6/29.
 */
public interface RemoteRegConsumerClient extends RemoteConsumerClient {

    <T> T exe(String ipAndPort, String serviceId, Object... objects) throws RemoteClientException;

}
