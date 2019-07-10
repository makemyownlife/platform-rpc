package com.courage.platform.client.rpc.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.config.ApplicationConfig;
import com.courage.platform.client.exception.RpcClientException;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.protocol.CommandEnum;
import com.courage.platform.client.rpc.protocol.RpcRequestCommand;
import com.courage.platform.client.rpc.protocol.RpcRequestConstants;
import com.courage.platform.client.util.Hessian1Utils;
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

    private ApplicationConfig applicationConfig;

    public RpcConsumerClientImpl(ApplicationConfig applicationConfig) {
        this.platformNettyClientConfig = new PlatformNettyClientConfig();
        this.platformRemotingClient = new PlatformNettyRemotingClient(platformNettyClientConfig);
        this.platformRemotingClient.start();
        this.applicationConfig = applicationConfig;
    }

    public <T> T execute(String addr, String serviceId, Object... objects) throws RpcClientException {
        try {
            //rpc请求命令
            RpcRequestCommand rpcRequestCommand = new RpcRequestCommand();
            rpcRequestCommand.setAppName(applicationConfig.getAppName());
            rpcRequestCommand.setServiceId(serviceId);
            rpcRequestCommand.setObjectLength(objects.length);
            rpcRequestCommand.setBody(Hessian1Utils.encodeObject(objects));

            //转化成远程通讯框架所需的命令
            PlatformRemotingCommand platformRemotingCommand = new PlatformRemotingCommand();
            platformRemotingCommand.setRequestCmd(CommandEnum.RPC_REQUEST_CMD);
            platformRemotingCommand.setBody(rpcRequestCommand.getBody());
            platformRemotingCommand.putHeadParam(RpcRequestConstants.RPC_REQUEST_COMMAND_HEADER, JSON.toJSONString(rpcRequestCommand));

            //发送请求到生产者 返回response
            PlatformRemotingCommand response = platformRemotingClient.invokeSync(addr, platformRemotingCommand, 30000L);
            byte[] responseBody = null;
            if (response != null) {
                if (response.getCode() == PlatformRemotingSysResponseCode.SUCCESS) {
                    responseBody = response.getBody();
                    return (T) Hessian1Utils.decodeObject(responseBody);
                }
            }
            return null;
        } catch (Exception e) {
            String message = "execute error addr:" + addr + " serviceId:" + serviceId;
            logger.error(message, e);
            throw new RpcClientException(message, e);
        }
    }

    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        RpcConsumerClient rpcConsumerClient = new RpcConsumerClientImpl(applicationConfig);
        rpcConsumerClient.execute("127.0.0.1:10029", "shop.create.new", "lilin", "张勇");
    }

}
