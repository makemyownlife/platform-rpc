package com.courage.platform.client.rpc.processor;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.rpc.RpcServiceResolver;
import com.courage.platform.client.rpc.domain.RpcServiceInvoker;
import com.courage.platform.client.rpc.protocol.RpcCommandConstants;
import com.courage.platform.client.rpc.protocol.RpcRequestCommand;
import com.courage.platform.client.rpc.protocol.RpcResponseCommand;
import com.courage.platform.client.util.HessianUtils;
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
        RpcResponseCommand rpcResponseCommand = new RpcResponseCommand();
        try {
            String json = (String) request.getHeadParams().get(RpcCommandConstants.RPC_REQUEST_COMMAND_HEADER);
            RpcRequestCommand rpcRequestCommand = JSON.parseObject(json, RpcRequestCommand.class);
            byte[] requestBody = request.getBody();
            rpcRequestCommand.setBody(requestBody);
            //请求参数
            Object[] requestObjects = HessianUtils.decodeObject(requestBody, rpcRequestCommand.getObjectLength());
            String serviceId = rpcRequestCommand.getServiceId();
            RpcServiceInvoker rpcServiceInvoker = RpcServiceResolver.getInvoker(rpcRequestCommand.getServiceId());
            if (rpcServiceInvoker == null) {
                platformRemotingCommand.setCode(PlatformRemotingSysResponseCode.SYSTEM_ERROR);
                rpcResponseCommand.setMessage(RpcCommandConstants.RPC_SERVICE_NOT_EXIST);
            } else {
                Object result = rpcServiceInvoker.invoke(serviceId, requestObjects);
                platformRemotingCommand.setCode(PlatformRemotingSysResponseCode.SUCCESS);
                platformRemotingCommand.setBody(HessianUtils.encodeObject(result));
            }
            platformRemotingCommand.putHeadParam(RpcCommandConstants.RPC_RESPONSE_COMMAND_HEADER, rpcResponseCommand);
        } catch (Throwable e) {
            logger.error("processRequest error:", e);
            platformRemotingCommand.setCode(PlatformRemotingSysResponseCode.SYSTEM_ERROR);
            rpcResponseCommand.setMessage(e.getMessage());
            platformRemotingCommand.putHeadParam(RpcCommandConstants.RPC_RESPONSE_COMMAND_HEADER, rpcResponseCommand);
        }
        return platformRemotingCommand;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }

}
