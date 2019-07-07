package com.courage.platform.client.rpc.impl;

import com.courage.platform.client.exception.RemoteClientException;
import com.courage.platform.client.rpc.RemoteConsumerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程消费者客户端
 * Created by zhangyong on 2019/7/7.
 */
public class RemoteConsumerClientImpl implements RemoteConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(RemoteConsumerClientImpl.class);

    public <T> T execute(String ipAndPort, String serviceId, Object... objects) throws RemoteClientException {
        return null;
    }

}
