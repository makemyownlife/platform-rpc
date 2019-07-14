package com.courage.platform.client.rpc.domain;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;

/**
 * 用于存储提供的RPC服务，方便调用
 */
public class RpcServiceInvoke {
    /**
     * 服务id
     */
    private String serviceId;
    /**
     * 服务备注
     */
    private String remark;
    /**
     * 服务对应的实例对象
     */
    private Object obj;
    /**
     * 被注解标注的方法
     */
    private Method method;

    public RpcServiceInvoke(String serviceId, String remark, Object obj, Method method) {
        this.serviceId = serviceId;
        this.remark = remark;
        this.obj = obj;
        this.method = method;
    }

    /**
     * 通过服务id调用RPC注解标注方法
     *
     * @param serviceId
     * @param params
     * @return
     * @throws Exception
     */
    public Object invoke(String serviceId, Object[] params) throws Exception {
        Object result = null;
        try {
            result = method.invoke(obj, params);
        } catch (Exception e) {
            String errorMsg = "调用RSAnnotation服务异常，服务erviceId:" + serviceId + ",备注：" + remark + "," + "被调用对象：" + obj + ",被调用方法：" + method + ",调用参数" + JSON.toJSONString(params);
            throw new Exception(errorMsg, e);
        }
        return result;
    }

    public String getServiceId() {
        return serviceId;
    }

    public boolean isSameClassMethod(RpcServiceInvoke invoker) {
        if (invoker instanceof RpcServiceInvoke) {
            RpcServiceInvoke other = (RpcServiceInvoke) invoker;
            //如果对当前类是一致的
            if (other.obj == this.obj || other.obj.getClass().getName().equals(this.obj.getClass().getName())) {
                if (other.method == this.method || other.method.getName().equals(this.method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "RpcClientInvoke{" + "serviceId='" + serviceId + '\'' + ", remark='" + remark + '\'' + ", obj=" + obj + ", method=" + method + '}';
    }

}
