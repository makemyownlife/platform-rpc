package com.courage.platform.client.rpc.impl;

import com.courage.platform.client.config.ApplicationConfig;
import com.courage.platform.client.config.RemoteProducerConfig;
import com.courage.platform.client.rpc.RemoteProducerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 远程服务生产者
 * Created by zhangyong on 2019/7/7.
 */
public class RemoteProducerClientImpl implements RemoteProducerClient {

    private final static Logger logger = LoggerFactory.getLogger(RemoteProducerClientImpl.class);

    private ApplicationConfig applicationConfig;

    private RemoteProducerConfig remoteProducerConfig;

    public RemoteProducerClientImpl(ApplicationConfig applicationConfig, RemoteProducerConfig remoteProducerConfig) {
        this.applicationConfig = applicationConfig;
        this.remoteProducerConfig = remoteProducerConfig;
    }

    public void start() {

    }

    public void shutdown() {

    }

}
