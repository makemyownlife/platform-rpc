package com.courage.platform.client.rpc.processor;

import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRequestProcessor;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingSysResponseCode;
import io.netty.channel.ChannelHandlerContext;

/**
 * 心跳处理器
 * Created by zhangyong on 2019/7/7.
 */
public class RpcHeartBeatProcessor implements PlatformNettyRequestProcessor {

    public PlatformRemotingCommand processRequest(ChannelHandlerContext ctx, PlatformRemotingCommand request) throws Exception {
        //直接返回响应码
        PlatformRemotingCommand response = new PlatformRemotingCommand();
        response.setCode(PlatformRemotingSysResponseCode.SUCCESS);
        return response;
    }

    public boolean rejectRequest() {
        return false;
    }

}
