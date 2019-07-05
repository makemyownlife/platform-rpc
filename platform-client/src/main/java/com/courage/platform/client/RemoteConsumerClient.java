package com.courage.platform.client;

import com.courage.platform.client.exception.RemoteClientException;

/**
 * 远程服务客户端(默认调用方法)
 * Created by zhangyong on 2019/6/29.
 */
public interface RemoteConsumerClient {

    <T> T exe(String ipAndPort, String serviceId, Object... objects) throws RemoteClientException;

}
