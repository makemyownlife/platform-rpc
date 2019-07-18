package com.courage.platform.rpc.demo.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

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

        NamingService namingService = NamingFactory.createNamingService(properties);
        namingService.registerInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");

        Instance instance = new Instance();
        instance.setIp("192.168.1.2");
        instance.setPort(10229);
        instance.setEphemeral(false);
        namingService.registerInstance("mytest", instance);

        Thread.sleep(100000);
    }

}
