package com.courage.platform.client.rpc.processor;

import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRequestProcessor;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommandFormat;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingSysResponseCode;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳处理器
 * Created by zhangyong on 2019/7/7.
 */
public class RpcHeartBeatProcessor implements PlatformNettyRequestProcessor {

    private final static Logger logger = LoggerFactory.getLogger(RpcHeartBeatProcessor.class);

    public PlatformRemotingCommand processRequest(ChannelHandlerContext ctx, PlatformRemotingCommand request) throws Exception {
        //直接返回响应码
        PlatformRemotingCommand response = new PlatformRemotingCommand();
        response.setCode(PlatformRemotingSysResponseCode.SUCCESS);
        response.setFormat(PlatformRemotingCommandFormat.RESPONSE.getCode());
        return response;
    }

    public boolean rejectRequest() {
        return false;
    }

}
