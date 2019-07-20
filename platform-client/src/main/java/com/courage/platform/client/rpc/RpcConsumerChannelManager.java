package com.courage.platform.client.rpc;

import com.alibaba.fastjson.JSON;
import com.courage.platform.client.rpc.domain.RpcConsumerChannelEntity;
import com.courage.platform.client.rpc.protocol.RpcCommandEnum;
import com.courage.platform.rpc.remoting.PlatformRemotingClient;
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
public class RpcConsumerChannelManager {

    private final static Logger logger = LoggerFactory.getLogger(RpcConsumerChannelManager.class);

    private final Map<Long, RpcConsumerChannelEntity> channelMap = new ConcurrentHashMap<>(1024);

    private static volatile boolean running = false;

    private PlatformRemotingClient platformRemotingClient;

    public RpcConsumerChannelManager bindPlatformRemotingClient(PlatformRemotingClient platformRemotingClient) {
        this.platformRemotingClient = platformRemotingClient;
        return this;
    }

    public void start() {
        if (running) {
            return;
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                heartBeatCheck();
            }
        });
        t.setName("consumerHeartBeatCheck");
        t.start();
        running = true;
        logger.info("heartBeat 检测线程启动 ");
    }

    public void addNewChannel(RpcConsumerChannelEntity rpcChannelEntity) {
        if (!channelMap.containsKey(rpcChannelEntity.getChannelId())) {
            channelMap.put(rpcChannelEntity.getChannelId(), rpcChannelEntity);
        }
    }

    public void removeChannel(Long channelId) {
        channelMap.remove(channelId);
    }

    private void heartBeatCheck() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                //ignore
            }
            Iterator<Map.Entry<Long, RpcConsumerChannelEntity>> entries = channelMap.entrySet().iterator();
            while (entries.hasNext()) {
                boolean heartBeatFlag = false;
                RpcConsumerChannelEntity rpcChannelEntity = null;
                try {
                    Map.Entry<Long, RpcConsumerChannelEntity> entry = entries.next();
                    rpcChannelEntity = entry.getValue();
                    PlatformRemotingCommand heartBeatRemotingCommand = new PlatformRemotingCommand();
                    heartBeatRemotingCommand.setRequestCmd(RpcCommandEnum.RPC_HEART_BEAT_CMD);
                    platformRemotingClient.invokeSync(rpcChannelEntity.getAddr(), heartBeatRemotingCommand, 3000);
                    heartBeatFlag = true;
                } catch (Throwable e) {
                    logger.error("heartbeat to channel error:" + JSON.toJSONString(rpcChannelEntity), e);
                } finally {
                    if (rpcChannelEntity != null && heartBeatFlag) {
                        //设置最后的心跳时间
                        rpcChannelEntity.setLastTriggerTime(System.currentTimeMillis());
                    }
                }
            }
        }

    }

}
