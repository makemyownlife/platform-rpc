package com.courage.platform.client.rpc.processor;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.rpc.protocol.RpcRequestCommand;
import com.courage.platform.client.rpc.protocol.RpcRequestConstants;
import com.courage.platform.client.util.Hessian1Utils;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRequestProcessor;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommandFormat;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingSysResponseCode;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc的请求处理器
 * Created by zhangyong on 2019/7/7.
 */
public class RpcRequestProcessor implements PlatformNettyRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequestProcessor.class);
    
    @Override
    public PlatformRemotingCommand processRequest(ChannelHandlerContext ctx, PlatformRemotingCommand request) throws Exception {
        PlatformRemotingCommand platformRemotingCommand = new PlatformRemotingCommand();
        platformRemotingCommand.setFormat(PlatformRemotingCommandFormat.RESPONSE.getCode());
        try {
            String json = (String) request.getHeadParams().get(RpcRequestConstants.RPC_REQUEST_COMMAND_HEADER);
            RpcRequestCommand rpcRequestCommand = JSON.parseObject(json, RpcRequestCommand.class);
            byte[] requestBody = request.getBody();
            rpcRequestCommand.setBody(requestBody);
            //请求参数
            Object[] requestObjects = Hessian1Utils.decodeObject(requestBody, rpcRequestCommand.getObjectLength());
            platformRemotingCommand.setCode(PlatformRemotingSysResponseCode.SUCCESS);
        } catch (Throwable e) {
            logger.error("processRequest error:", e);
            platformRemotingCommand.setCode(PlatformRemotingSysResponseCode.SYSTEM_ERROR);
        }
        return platformRemotingCommand;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

}
