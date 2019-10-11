package com.courage.platform.client.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.config.RpcAppConfig;
import com.courage.platform.client.exception.RpcClientConsumerException;
import com.courage.platform.client.exception.RpcClientException;
import com.courage.platform.client.rpc.RpcConsumerChannelListener;
import com.courage.platform.client.rpc.RpcConsumerChannelManager;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.protocol.RpcCommandConstants;
import com.courage.platform.client.rpc.protocol.RpcCommandEnum;
import com.courage.platform.client.rpc.protocol.RpcRequestCommand;
import com.courage.platform.client.rpc.protocol.RpcResponseCommand;
import com.courage.platform.client.util.HessianUtils;
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

    private PlatformRemotingClient platformRemotingClient;

    private RpcConsumerChannelManager rpcConsumerChannelManager;

    private RpcAppConfig rpcAppConfig;

    public RpcConsumerClientImpl(RpcAppConfig rpcAppConfig) {
        this.platformNettyClientConfig = new PlatformNettyClientConfig();
        this.rpcAppConfig = rpcAppConfig;
        //rpc消费者链接管理
        this.rpcConsumerChannelManager = new RpcConsumerChannelManager();
        //平台远程调用客户端
        this.platformRemotingClient = new PlatformNettyRemotingClient(platformNettyClientConfig, new RpcConsumerChannelListener(rpcConsumerChannelManager));
        this.platformRemotingClient.start();
        //绑定当前的rpc消费者客户端,并启动异步线程 检测心跳
        this.rpcConsumerChannelManager.bindPlatformRemotingClient(platformRemotingClient).start();
    }

    public <T> T execute(String addr, String serviceId, Object... objects) throws RpcClientException {
        try {
            //rpc请求命令
            RpcRequestCommand rpcRequestCommand = new RpcRequestCommand();
            rpcRequestCommand.setAppName(rpcAppConfig.getAppName());
            rpcRequestCommand.setServiceId(serviceId);
            rpcRequestCommand.setObjectLength(objects.length);
            rpcRequestCommand.setBody(HessianUtils.encodeObject(objects));

            //转化成远程通讯框架所需的命令
            PlatformRemotingCommand platformRemotingCommand = new PlatformRemotingCommand();
            platformRemotingCommand.setRequestCmd(RpcCommandEnum.RPC_REQUEST_CMD);
            platformRemotingCommand.setBody(rpcRequestCommand.getBody());
            platformRemotingCommand.putHeadParam(RpcCommandConstants.RPC_REQUEST_COMMAND_HEADER, JSON.toJSONString(rpcRequestCommand));

            //发送请求到生产者 返回response
            PlatformRemotingCommand response = platformRemotingClient.invokeSync(addr, platformRemotingCommand, 30000L);
            if (response != null) {
                RpcResponseCommand rpcResponseCommand = JSON.parseObject((String) response.getHeadParam(RpcCommandConstants.RPC_RESPONSE_COMMAND_HEADER), RpcResponseCommand.class);
                if (response.getCode() == PlatformRemotingSysResponseCode.SUCCESS) {
                    byte[] responseBody = response.getBody();
                    return (T) HessianUtils.decodeObject(responseBody);
                } else {
                    throw new RpcClientConsumerException("execute fail addr:" + addr + " serviceId:" + serviceId + " return rpccode:" + response.getCode() + " message:" + rpcResponseCommand.getMessage());
                }
            }
            throw new RpcClientConsumerException("execute cant return response may be network problem  addr:" + addr + " serviceId:" + serviceId);
        } catch (Exception e) {
            String message = "execute error addr:" + addr + " serviceId:" + serviceId;
            logger.error(message, e);
            throw new RpcClientConsumerException(message, e);
        }
    }

    public static void main(String[] args) {
        RpcAppConfig rpcAppConfig = new RpcAppConfig();
        RpcConsumerClient rpcConsumerClient = new RpcConsumerClientImpl(rpcAppConfig);
        rpcConsumerClient.execute("127.0.0.1:10029", "shop.create.new", "lilin", "张勇");
    }

}
