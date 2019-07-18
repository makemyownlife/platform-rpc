package com.courage.platform.client.spring;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.courage.platform.client.config.RpcAppConfig;
import com.courage.platform.client.exception.RpcClientConsumerException;
import com.courage.platform.client.exception.RpcClientException;
import com.courage.platform.client.regcenter.RegcenterService;
import com.courage.platform.client.rpc.RpcConsumerClient;
import com.courage.platform.client.rpc.impl.RpcConsumerClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * spring rpc 生产者控制台
 * Created by zhangyong on 2019/7/14.
 */
public class SpringRpcConsumerClient {

    private static final Logger logger = LoggerFactory.getLogger(SpringRpcConsumerClient.class);

    private RpcAppConfig rpcAppConfig;

    private RpcConsumerClient rpcConsumerClient;

    private RegcenterService regcenterService;

    public SpringRpcConsumerClient(RpcAppConfig rpcAppConfig, RegcenterService regcenterService) throws NacosException {
        this.rpcAppConfig = rpcAppConfig;
        this.regcenterService = regcenterService;
        this.rpcConsumerClient = new RpcConsumerClientImpl(this.rpcAppConfig);
        logger.info("init spring rpcconsumerclient");
    }

    /**
     * 通过注册中心消费远程服务
     *
     * @param serviceName 服务名/实例名
     * @param serviceId   类似: shop.submit.info
     * @param objects     对象
     * @param <T>         返回对象
     * @return
     * @throws RpcClientException
     */
    public <T> T executeByServiceName(String serviceName, String serviceId, Object... objects) throws RpcClientException, NacosException {
        Instance instance = regcenterService.queryOneHealthyInstance(serviceName);
        if (instance != null) {
            String addr = instance.getIp() + ":" + instance.getPort();
            T result = (T) rpcConsumerClient.execute(addr, serviceId, objects);
            return result;
        }
        throw new RpcClientConsumerException("cant find serviceName:" + serviceName + " one healthy instance");
    }

}
