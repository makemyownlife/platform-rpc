package com.courage.platform.client.rpc;

import com.courage.platform.client.rpc.domain.RpcChannel;
import com.courage.platform.rpc.remoting.PlatformChannelEventListener;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * channel 监听器
 * Created by zhangyong on 2019/7/18.
 */
public class RpcChannelListener implements PlatformChannelEventListener {

    private static final Logger logger = LoggerFactory.getLogger(RpcChannelListener.class);

    private static AtomicLong channelIdSequence = new AtomicLong(0);

    private static AttributeKey<Long> keyChannelId = AttributeKey.valueOf("channelId");

    @Override
    public void onChannelConnect(String remoteAddr, Channel channel) {
        //生成新的链接id
        long channelId = channelIdSequence.incrementAndGet();
        channel.attr(keyChannelId).set(channelId);
        RpcChannel rpcChannel = new RpcChannel(channelId, channel);
        RpcChannelManager.addNewChannel(rpcChannel);
    }

    @Override
    public void onChannelClose(String remoteAddr, Channel channel) {
        long channelId = channel.attr(keyChannelId).get();
        RpcChannelManager.removeChannel(channelId);
    }

    @Override
    public void onChannelException(String remoteAddr, Channel channel) {

    }

    @Override
    public void onChannelIdle(String remoteAddr, Channel channel) {

    }

}
