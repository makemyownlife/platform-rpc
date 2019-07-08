package com.courage.platform.client.exception;


/**
 * 远程客户端异常
 * Created by 张勇 on 2019/6/19
 */
public class RemoteClientException extends RuntimeException {

    private static final long serialVersionUID = -5565366231695911316L;

    public RemoteClientException(String message) {
        super(message);
    }

    public RemoteClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
