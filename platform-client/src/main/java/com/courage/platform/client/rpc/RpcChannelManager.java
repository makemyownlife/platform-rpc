package com.courage.platform.client.rpc;

import com.courage.platform.client.rpc.domain.RpcChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyong on 2019/7/18.
 */
public class RpcChannelManager {

    private static final Map<Long, RpcChannel> channelMap = new ConcurrentHashMap<>(1024);

    public static void addNewChannel(RpcChannel rpcChannel) {
        if (!channelMap.containsKey(rpcChannel.getChannelId())) {
            channelMap.put(rpcChannel.getChannelId(), rpcChannel);
        }
    }

    public static void removeChannel(Long channelId) {
        channelMap.remove(channelId);
    }

}
