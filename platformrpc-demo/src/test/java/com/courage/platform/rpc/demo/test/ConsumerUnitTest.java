package com.courage.platform.rpc.demo.test;

import com.courage.platform.client.config.RpcAppConfig;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.impl.RpcConsumerClientImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyong on 2019/7/14.
 */
public class ConsumerUnitTest {

    public static void main(String[] args) {
        RpcAppConfig rpcAppConfig = new RpcAppConfig();
        rpcAppConfig.setAppName("demo.desktop");
        rpcAppConfig.setAppKey("我的生活");
        RpcConsumerClient rpcConsumerClient = new RpcConsumerClientImpl(rpcAppConfig);
        Map map = new HashMap<String, Object>();
        map.put("aa", new BigDecimal("12.21"));
        map.put("bb", 1231);
        String result = rpcConsumerClient.execute("localhost:10029", "demo.hashmap", map);
        System.out.println(result);
    }

}
