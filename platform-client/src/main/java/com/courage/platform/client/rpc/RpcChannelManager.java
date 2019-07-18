package com.courage.platform.client.rpc;

import com.courage.platform.client.rpc.domain.RpcChannel;
import com.courage.platform.client.rpc.protocol.RpcCommandEnum;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc的链接管理 (心跳)
 * Created by zhangyong on 2019/7/18.
 */
public class RpcChannelManager {

    private final static Logger logger = LoggerFactory.getLogger(RpcChannelManager.class);

    private static final Map<Long, RpcChannel> channelMap = new ConcurrentHashMap<>(1024);

    static {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                heartBeatCheck();
            }
        });
        t.setName("heartBeatCheck");
        t.start();
        logger.info("heartBeat 检测线程启动 ");
    }

    public static void addNewChannel(RpcChannel rpcChannel) {
        if (!channelMap.containsKey(rpcChannel.getChannelId())) {
            channelMap.put(rpcChannel.getChannelId(), rpcChannel);
        }
    }

    public static void removeChannel(Long channelId) {
        channelMap.remove(channelId);
    }

    private static void heartBeatCheck() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                //ignore
            }
            Iterator<Map.Entry<Long, RpcChannel>> entries = channelMap.entrySet().iterator();
            while (entries.hasNext()) {
                try {
                    Map.Entry<Long, RpcChannel> entry = entries.next();
                    RpcChannel rpcChannel = entry.getValue();
                    PlatformRemotingCommand heartBeatRemotingCommand = new PlatformRemotingCommand();
                    heartBeatRemotingCommand.setRequestCmd(RpcCommandEnum.RPC_HEART_BEAT_CMD);
                    rpcChannel.getChannel().writeAndFlush(heartBeatRemotingCommand);
                } catch (Throwable e) {
                    logger.error("writeAndFlush error:", e);
                }
            }
        }

    }

}
