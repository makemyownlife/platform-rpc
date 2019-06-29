package com.courage.platform.client;

import com.courage.platform.client.exception.RemoteClientException;

/**
 * 远程服务客户端
 * Created by zhangyong on 2019/6/29.
 */
public interface RemoteClient {

    <T> T exe(String serviceId, Object... objects) throws RemoteClientException;

}
