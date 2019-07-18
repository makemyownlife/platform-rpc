package com.courage.platform.client.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.courage.platform.client.config.RpcAppConfig;
import com.courage.platform.client.config.RpcProducerConfig;
import com.courage.platform.client.config.RpcRegserverConfig;
import com.courage.platform.client.regcenter.RegcenterService;
import com.courage.platform.client.rpc.RpcProducerClient;
import com.courage.platform.client.rpc.RpcServiceResolver;
import com.courage.platform.client.rpc.impl.RpcProducerClientImpl;
import com.courage.platform.client.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * spring rpc 生产者控制台
 * Created by zhangyong on 2019/7/14.
 */
public class SpringRpcProducerClient implements ApplicationContextAware, ApplicationListener, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(SpringRpcProducerClient.class);

    private RpcAppConfig rpcAppConfig;

    private RpcRegserverConfig rpcRegserverConfig;

    private RpcProducerConfig rpcProducerConfig;

    private ApplicationContext applicationContext;

    private RpcProducerClient rpcProducerClient;

    private RegcenterService regcenterService;

    public SpringRpcProducerClient(RpcAppConfig rpcAppConfig, RpcRegserverConfig rpcRegserverConfig, RpcProducerConfig rpcProducerConfig) throws NacosException {
        this.rpcAppConfig = rpcAppConfig;
        this.rpcRegserverConfig = rpcRegserverConfig;
        this.rpcProducerConfig = rpcProducerConfig;
        logger.info("current appconfig:" + JSON.toJSONString(rpcAppConfig));
        this.regcenterService = new RegcenterService(rpcRegserverConfig);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (!(applicationEvent instanceof ContextRefreshedEvent)) {
            return;
        }
        try {
            //获取Service注解标注的所有bean
            Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(Component.class);
            //扫描所有的远程服务的注解(放入本地缓存中)
            RpcServiceResolver.getAbstractBean(serviceBeanMap);
            //实例化，启动rpc server 并监听端口
            this.rpcProducerClient = new RpcProducerClientImpl(rpcProducerConfig);
            this.rpcProducerClient.start();
            //TODO 添加到注册中心
            logger.info("begin to instance to nacos server");
            //服务名
            String appName = rpcAppConfig.getAppName();
            //监听端口呢
            int listenPort = rpcProducerClient.localListenPort();
            //当前的本地ip
            String ip = IpUtils.LOCAL_IP;
            logger.info("应用名:" + appName + " listenPort:" + listenPort + " 本地ip:" + ip);
            regcenterService.registerInstance(appName, ip, listenPort);
        } catch (Exception e) {
            logger.error("启动rpc生产者服务失败!", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.rpcProducerClient.shutdown();
    }

}
