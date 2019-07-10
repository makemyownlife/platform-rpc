package com.courage.platform.rpc.demo;

import com.courage.platform.client.config.ApplicationConfig;
import com.courage.platform.client.config.RpcProducerConfig;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.RpcProducerClient;
import com.courage.platform.client.rpc.impl.RpcConsumerClientImpl;
import com.courage.platform.client.rpc.impl.RpcProducerClientImpl;

/**
 * Created by zhangyong on 2019/7/10.
 */
public class Main {

    public static void main(String[] args) {
        RpcProducerConfig rpcProducerConfig = new RpcProducerConfig();
        RpcProducerClient rpcProducerClient = new RpcProducerClientImpl(rpcProducerConfig);
        rpcProducerClient.start();

        ApplicationConfig applicationConfig = new ApplicationConfig();
        RpcConsumerClient rpcConsumerClient = new RpcConsumerClientImpl(applicationConfig);
        rpcConsumerClient.execute("127.0.0.1:10029", "shop.create.new", "lilin", "张勇");
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


