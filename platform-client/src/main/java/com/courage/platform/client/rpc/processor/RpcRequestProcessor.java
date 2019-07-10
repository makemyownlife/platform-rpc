package com.courage.platform.client.rpc.processor;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.rpc.protocol.RpcRequestCommand;
import com.courage.platform.client.rpc.protocol.RpcRequestConstants;
import com.courage.platform.client.util.Hessian1Utils;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRequestProcessor;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
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
        try {
            String json = (String) request.getHeadParams().get(RpcRequestConstants.RPC_REQUEST_COMMAND_HEADER);
            RpcRequestCommand rpcRequestCommand = JSON.parseObject(json, RpcRequestCommand.class);
            byte[] requestBody = request.getBody();
            Object[] requestObjects = Hessian1Utils.decodeObject(requestBody, rpcRequestCommand.getObjectLength());
            rpcRequestCommand.setBody(requestBody);
        } catch (Exception e) {
            logger.error("processRequest error:", e);
        }
        return null;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

}
