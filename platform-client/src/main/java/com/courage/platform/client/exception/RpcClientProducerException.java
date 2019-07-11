package com.courage.platform.client.exception;


/**
 * 远程rpc生产则异常
 * Created by 张勇 on 2019/7/11
 */
public class RpcClientProducerException extends RpcClientException {

    private static final long serialVersionUID = -5263346231695911316L;

    public RpcClientProducerException(String message) {
        super(message);
    }

    public RpcClientProducerException(String message, Throwable cause) {
        super(message, cause);
    }

}
