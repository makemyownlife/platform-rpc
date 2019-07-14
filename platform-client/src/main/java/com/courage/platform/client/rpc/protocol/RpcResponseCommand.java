package com.courage.platform.client.rpc.protocol;

import java.io.Serializable;

/**
 * rpc响应命令
 * Created by zhangyong on 2019/7/8.
 */
public class RpcResponseCommand implements Serializable {

    private static final long serialVersionUID = -5125366231695911316L;

    private String message;

    //返回实体对象数据
    private transient byte[] body;

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
