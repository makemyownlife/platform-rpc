package com.courage.platform.client.rpc;

import com.courage.platform.client.rpc.domain.RpcChannelEntity;
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

    private static final Map<Long, RpcChannelEntity> channelMap = new ConcurrentHashMap<>(1024);

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

    public static void addNewChannel(RpcChannelEntity rpcChannelEntity) {
        if (!channelMap.containsKey(rpcChannelEntity.getChannelId())) {
            channelMap.put(rpcChannelEntity.getChannelId(), rpcChannelEntity);
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
            Iterator<Map.Entry<Long, RpcChannelEntity>> entries = channelMap.entrySet().iterator();
            while (entries.hasNext()) {
                RpcChannelEntity rpcChannelEntity = null;
                try {
                    Map.Entry<Long, RpcChannelEntity> entry = entries.next();
                    rpcChannelEntity = entry.getValue();
                    PlatformRemotingCommand heartBeatRemotingCommand = new PlatformRemotingCommand();
                    heartBeatRemotingCommand.setRequestCmd(RpcCommandEnum.RPC_HEART_BEAT_CMD);
                    rpcChannelEntity.getChannel().writeAndFlush(heartBeatRemotingCommand);
                } catch (Throwable e) {
                    logger.error("writeAndFlush error:", e);
                } finally {
                    //设置最后的心跳时间
                    rpcChannelEntity.setLastTriggerTime(System.currentTimeMillis());
                }
            }
        }

    }

}
