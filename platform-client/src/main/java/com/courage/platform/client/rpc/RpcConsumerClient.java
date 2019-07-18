package com.courage.platform.client.rpc;

import com.courage.platform.client.exception.RpcClientException;

/**
 * 远程服务客户端(默认调用方法)
 * Created by zhangyong on 2019/6/29.
 */
public interface RpcConsumerClient {
    /**
     * 消费远程服务
     * @param addr  127.0.0.1:10029
     * @param serviceId 类似: shop.submit.info
     * @param objects   对象
     * @param <T>       返回对象
     * @return
     * @throws RpcClientException
     */
    <T> T execute(String addr, String serviceId, Object... objects) throws RpcClientException;

}
