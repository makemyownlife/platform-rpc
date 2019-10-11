package com.courage.platform.client.exception;


/**
 * 远程rpc消费者异常
 * Created by 张勇 on 2019/7/11
 */
public class RpcClientConsumerException extends RpcClientException {

    private static final long serialVersionUID = -5563346231695911316L;

    public RpcClientConsumerException(String message) {
        super(message);
    }

    public RpcClientConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

}
