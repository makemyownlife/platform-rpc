package com.courage.platform.client.rpc;

import com.courage.platform.client.annotations.RSAnnotation;
import com.courage.platform.client.rpc.domain.RpcServiceInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc service 解析器
 * Created by zhangyong on 2019/7/14.
 */
public class RpcServiceResolver {

    private static final Logger logger = LoggerFactory.getLogger(RpcServiceResolver.class);

    private static final Map<String, RpcServiceInvoker> invokerMap = new ConcurrentHashMap<String, RpcServiceInvoker>(512);

    public static RpcServiceInvoker getInvoker(String serviceId) {
        //将服务id转换为小写
        serviceId = serviceId.toLowerCase();
        return invokerMap.get(serviceId);
    }

    public static void getAbstractBean(Map<String, Object> serviceBeanMap) {
        //遍历每个Component注解标注的bean
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            Object object = entry.getValue();
            Class<? extends Object> clazz = object.getClass();
            getRSAnnotationMethod(clazz.getMethods(), object);
        }
    }

    /**
     * 获取标注了@RSAnnotation注解的方法，并添加到全局变量Map中
     *
     * @param methods 当前类的所有方法
     * @param object  当了类的实例
     */
    private static void getRSAnnotationMethod(Method[] methods, Object object) {
        for (Method method : methods) {
            //使用AnnotationUtils.findAnnotation拿到代理类中的注解
            RSAnnotation rsAnnotation = AnnotationUtils.findAnnotation(method, RSAnnotation.class);
            //如果当前方法被@RSAnnotation注解标注了，则添加当前serviceId和当前对象到全局变量Map中
            if (rsAnnotation != null) {
                RpcServiceInvoker rpcServiceInvoker = new RpcServiceInvoker(rsAnnotation.value(), rsAnnotation.remark(), object, method);
                addInvoker(rpcServiceInvoker);
            }
        }
    }

    /**
     * 将保存RPC服务的ClientInvoke对象保存到缓存Map中
     *
     * @param clientInvoke
     */
    public static synchronized void addInvoker(RpcServiceInvoker clientInvoke) {
        //将服务id转换为小写
        String serviceId = clientInvoke.getServiceId().toLowerCase();
        RpcServiceInvoker temp = invokerMap.get(serviceId);
        //如果全局变量Map已经存在serviceId
        if (temp != null) {
            //如果已经存在当前方法,则覆盖
            if (temp.isSameClassMethod(clientInvoke)) {
                logger.error("RpcclientInvoke is reload! serviceId=" + serviceId);
                invokerMap.put(serviceId, clientInvoke);
            } else {
                logger.error("RpcClientInvoke定义重复" + temp);
            }
        }
        invokerMap.put(serviceId, clientInvoke);
    }

}
