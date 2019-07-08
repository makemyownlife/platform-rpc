package com.courage.platform.client.rpc.impl;

import com.courage.platform.client.exception.RemoteClientException;
import com.courage.platform.client.rpc.RemoteConsumerClient;
import com.courage.platform.rpc.remoting.PlatformRemotingClient;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyClientConfig;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRemotingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程消费者客户端
 * Created by zhangyong on 2019/7/7.
 */
public class RemoteConsumerClientImpl implements RemoteConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(RemoteConsumerClientImpl.class);

    private PlatformNettyClientConfig platformNettyClientConfig;

    private PlatformRemotingClient remotingClient;

    public RemoteConsumerClientImpl() {
        this.platformNettyClientConfig = new PlatformNettyClientConfig();
        this.remotingClient = new PlatformNettyRemotingClient(platformNettyClientConfig);
        this.remotingClient.start();
    }

    public <T> T execute(String ipAndPort, String serviceId, Object... objects) throws RemoteClientException {

        return null;
    }

}
