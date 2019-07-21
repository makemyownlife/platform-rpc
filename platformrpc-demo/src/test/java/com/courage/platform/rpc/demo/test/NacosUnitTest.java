package com.courage.platform.rpc.demo.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.List;
import java.util.Properties;

/**
 * Created by zhangyong on 2019/7/16.
 */
public class NacosUnitTest {

    public static void main(String[] args) throws NacosException, InterruptedException {
        String serverList = "192.168.31.131:8848";

        Properties properties = new Properties();
        //nacos需要部署在内网机房里面,并且各个环境是隔离的
        properties.setProperty("serverAddr", serverList);
        properties.setProperty("namespace", "41f0993c-adbb-4832-a0a7-3f01bc804c99");
        properties.setProperty("namingLoadCacheAtStart", "true");

        NamingService namingService = NamingFactory.createNamingService(properties);
        List list = namingService.getAllInstances("platformrpc-demo");

        System.out.println(list);
        Thread.sleep(100000);
    }

}
