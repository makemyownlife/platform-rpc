package com.courage.platform.client.rpc.domain;

import io.netty.channel.Channel;

/**
 * rpc 消费端链接实体
 * Created by zhangyong on 2019/7/18.
 */
public class RpcConsumerChannelEntity {

    //远程调用地址
    private String addr;

    private Long channelId;

    private Channel channel;

    private Long createTime;

    public RpcConsumerChannelEntity(String addr, Long channelId, Channel channel) {
        this.addr = addr;
        this.channelId = channelId;
        this.channel = channel;
        this.createTime = System.currentTimeMillis();
    }

    private Long lastTriggerTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastTriggerTime() {
        return lastTriggerTime;
    }

    public void setLastTriggerTime(Long lastTriggerTime) {
        this.lastTriggerTime = lastTriggerTime;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
