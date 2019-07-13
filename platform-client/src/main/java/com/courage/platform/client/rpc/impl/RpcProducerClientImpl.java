package com.courage.platform.client.rpc.impl;

import com.courage.platform.client.config.RpcProducerConfig;
import com.courage.platform.client.rpc.RpcProducerClient;
import com.courage.platform.client.rpc.processor.RpcHeartBeatProcessor;
import com.courage.platform.client.rpc.processor.RpcRequestProcessor;
import com.courage.platform.client.rpc.protocol.RpcCommandEnum;
import com.courage.platform.rpc.remoting.netty.codec.NodePlatformRemotingServer;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyRequestProcessor;
import com.courage.platform.rpc.remoting.netty.codec.PlatformNettyServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 远程服务生产者
 * Created by zhangyong on 2019/7/7.
 */
public class RpcProducerClientImpl implements RpcProducerClient {

    private final static Logger logger = LoggerFactory.getLogger(RpcProducerClientImpl.class);

    //默认 10029
    private Integer listenPort;

    private NodePlatformRemotingServer nodePlatformRemotingServer;

    private RpcProducerConfig rpcProducerConfig;

    private Map<Integer, PlatformNettyRequestProcessor> processorTable = new HashMap<Integer, PlatformNettyRequestProcessor>(4);

    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private final static int POOL_CORE_SIZE = AVAILABLE_PROCESSORS > 100 ? AVAILABLE_PROCESSORS : 100;

    private final static ThreadPoolExecutor remoteRpcThreadPool = new ThreadPoolExecutor(POOL_CORE_SIZE, POOL_CORE_SIZE * 2, 500, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500000), new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "RemoteRpcThreadPool_" + this.threadIndex.incrementAndGet());
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());

    public RpcProducerClientImpl(RpcProducerConfig rpcProducerConfig) {
        this.rpcProducerConfig = rpcProducerConfig;
        this.listenPort = rpcProducerConfig.getPort();
    }

    public void start() {
        PlatformNettyServerConfig platformNettyServerConfig = new PlatformNettyServerConfig();
        //设置监听端口
        platformNettyServerConfig.setListenPort(rpcProducerConfig.getPort());
        platformNettyServerConfig.setServerChannelMaxIdleTimeSeconds(rpcProducerConfig.getMaxIdleTime());
        this.nodePlatformRemotingServer = new NodePlatformRemotingServer(platformNettyServerConfig) {
            @Override
            public void boot(ServerBootstrap serverBootstrap) {
                try {
                    boolean bindSuccess = false;
                    while (!bindSuccess) {
                        try {
                            ChannelFuture sync = serverBootstrap.bind(listenPort);
                            sync.sync();
                            bindSuccess = true;
                            logger.info("启动rpc Node服务，监听端口:" + listenPort);
                            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
                        } catch (Exception e) {
                            if (e instanceof BindException) {
                                logger.warn(e.getMessage() + ", port=" + listenPort + ", we will try a new port");
                                listenPort = listenPort + 1;
                                if (listenPort > 65535) {
                                    listenPort = 40000;
                                }
                            } else if (e instanceof InterruptedException) {
                                throw (InterruptedException) e;
                            } else {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (InterruptedException e1) {
                    throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
                }
            }
        };
        synchronized (this) {
            //心跳命令
            processorTable.put(RpcCommandEnum.RPC_HEART_BEAT_CMD, new RpcHeartBeatProcessor());
            //同步调用命令
            processorTable.put(RpcCommandEnum.RPC_REQUEST_CMD, new RpcRequestProcessor());
            //添加命令处理器
            Set<Integer> set = processorTable.keySet();
            for (Integer cmd : set) {
                this.nodePlatformRemotingServer.registerProcessor(cmd, processorTable.get(cmd), remoteRpcThreadPool);
            }
        }
        this.nodePlatformRemotingServer.start();
    }

    public void shutdown() {
        this.nodePlatformRemotingServer.shutdown();
        remoteRpcThreadPool.shutdown();
    }

}
