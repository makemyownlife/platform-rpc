package com.courage.platform.client.rpc.impl;

import com.courage.platform.client.exception.RpcClientException;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.protocol.RpcRequestConstants;
import com.courage.platform.rpc.remoting.PlatformRemotingClient;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyClientConfig;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRemotingClient;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingSysResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程消费者客户端
 * Created by zhangyong on 2019/7/7.
 */
public class RpcConsumerClientImpl implements RpcConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(RpcConsumerClientImpl.class);

    private PlatformNettyClientConfig platformNettyClientConfig;

    private PlatformRemotingClient remotingClient;

    public RpcConsumerClientImpl() {
        this.platformNettyClientConfig = new PlatformNettyClientConfig();
        this.remotingClient = new PlatformNettyRemotingClient(platformNettyClientConfig);
        this.remotingClient.start();
    }

    public <T> T execute(String addr, String serviceId, Object... objects) throws RpcClientException {
        try {
            PlatformRemotingCommand platformRemotingCommand = new PlatformRemotingCommand();
            platformRemotingCommand.getHeadParams().put(RpcRequestConstants.APP_HEADER, null);
            platformRemotingCommand.setBody(null);

            PlatformRemotingCommand response = remotingClient.invokeSync(addr, platformRemotingCommand, 15000L);
            byte[] body = null;
            if (response != null) {
                if (response.getCode() == PlatformRemotingSysResponseCode.SUCCESS) {
                    body = response.getBody();
                }
            }
            return null;
        } catch (Exception e) {
            String message = "execute error addr:" + addr + " serviceId:" + serviceId;
            logger.error(message, e);
            throw new RpcClientException(message, e);
        }
    }

}
