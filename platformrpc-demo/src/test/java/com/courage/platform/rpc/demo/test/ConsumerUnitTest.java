package com.courage.platform.rpc.demo.test;

import com.courage.platform.client.config.RpcAppConfig;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.impl.RpcConsumerClientImpl;

/**
 * Created by zhangyong on 2019/7/14.
 */
public class ConsumerUnitTest {

    public static void main(String[] args) {
        RpcAppConfig rpcAppConfig = new RpcAppConfig();
        rpcAppConfig.setAppName("demo.desktop");
        rpcAppConfig.setAppKey("我的生活");
        RpcConsumerClient rpcConsumerClient = new RpcConsumerClientImpl(rpcAppConfig);
        String result = rpcConsumerClient.execute("localhost:10029", "demo.mylife1", "张勇");
        System.out.println(result);
    }

}
