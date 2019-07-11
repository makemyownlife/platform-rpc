package com.courage.platform.client.rpc;

public interface RpcServiceInvoke {

    String getServiceId();

    Object invoke(String serviceId, Object[] params) throws Exception;

    boolean isSameClassMethod(RpcServiceInvoke invoker);

}
