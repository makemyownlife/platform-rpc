package com.courage.platform.client.rpc.regcenter;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.courage.platform.client.config.RpcRegserverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 注册服务工具类
 * Created by zhangyong on 2019/7/18.
 */
public class NacosRegcenterService {

    private static final Logger logger = LoggerFactory.getLogger(NacosRegcenterService.class);

    private NamingService namingService;

    private RpcRegserverConfig rpcRegserverConfig;

    public NacosRegcenterService(RpcRegserverConfig rpcRegserverConfig) throws NacosException {
        this.rpcRegserverConfig = rpcRegserverConfig;
        this.init();
    }

    private void init() throws NacosException {
        Properties properties = new Properties();
        //nacos需要部署在内网机房里面,并且各个环境是隔离的
        properties.setProperty("serverAddr", this.rpcRegserverConfig.getAddress());
        properties.setProperty("namespace", this.rpcRegserverConfig.getNamespace());
        //名字服务
        this.namingService = NamingFactory.createNamingService(properties);
    }

    //后续需要做相关测试 比如 注册中心挂了之后 ，nacos是否有重试功能 ，以及重试时间 TODO 否则 自定义单线程处理
    public void registerInstance(String instanceName, String ip, int port) throws NacosException {
        registerInstance(instanceName, ip, port, Constants.DEFAULT_CLUSTER_NAME);
    }

    public void registerInstance(String instanceName, String ip, int port, String group) throws NacosException {
        namingService.registerInstance(instanceName, ip, port, group);
    }

    public void unregisterInstance(String instanceName, String groupName, EventListener eventListener) throws NacosException {
        namingService.unsubscribe(instanceName, groupName, eventListener);
    }

    //查询存活实例部分
    public List<Instance> queryAliveInstance(String serviceName) throws NacosException {
        List<Instance> instanceLIst = namingService.getAllInstances(serviceName);
        return instanceLIst;
    }

    public Instance queryOneHealthyInstance(String serviceName) throws NacosException {
        try {
            Instance instance = namingService.selectOneHealthyInstance(serviceName);
            return instance;
        } catch (Exception e) {
            logger.error("queryOneHealthyInstance error, serviceName:" + serviceName, e);
        }
        return null;
    }

    //TODO 非健康的还需要继续处理 重点看文件逻辑部分
    public Instance queryOneUnhealthyInstance(String serviceName) throws NacosException {
        List l = namingService.getSubscribeServices();
        List<Instance> instanceList = namingService.selectInstances(serviceName, false);
        if (!CollectionUtils.isEmpty(instanceList)) {
            if (instanceList.size() == 1) {
                return instanceList.get(0);
            }
            int length = instanceList.size();
            int index = new Random().nextInt(length);
            return instanceList.get(index);
        }
        return null;
    }

}
