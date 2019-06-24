package com.courage.platform.rpc.test;

import com.courage.platform.rpc.remoting.PlatformRemotingClient;
import com.courage.platform.rpc.remoting.PlatformRemotingServer;
import com.courage.platform.rpc.remoting.exception.PlatformRemotingConnectException;
import com.courage.platform.rpc.remoting.exception.PlatformRemotingSendRequestException;
import com.courage.platform.rpc.remoting.exception.PlatformRemotingTimeoutException;
import com.courage.platform.rpc.remoting.netty.codec.*;
import com.courage.platform.rpc.remoting.netty.protocol.PlatformRemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;

public class PlatformRemotingServerUnitTest {

    private static PlatformRemotingServer remotingServer;

    private static PlatformRemotingClient remotingClient;

    public static PlatformRemotingClient createRemotingClient() {
        return createRemotingClient(new PlatformNettyClientConfig());
    }

    public static PlatformRemotingClient createRemotingClient(PlatformNettyClientConfig nettyClientConfig) {
        PlatformRemotingClient client = new PlatformNettyRemotingClient(nettyClientConfig);
        client.start();
        return client;
    }

    public static PlatformRemotingServer createRemotingServer() throws InterruptedException {

        PlatformNettyServerConfig config = new PlatformNettyServerConfig();
        PlatformRemotingServer remotingServer = new PlatformNettyRemotingServer(config);
        remotingServer.registerProcessor(0, new PlatformNettyRequestProcessor() {
            @Override
            public PlatformRemotingCommand processRequest(ChannelHandlerContext ctx, PlatformRemotingCommand request) throws UnsupportedEncodingException {
                request.setRemark("Hi" + ctx.channel().remoteAddress());
                request.setBody("hello".getBytes("UTF-8"));
                return request;
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        }, Executors.newCachedThreadPool());
        remotingServer.start();
        return remotingServer;
    }

    @BeforeClass
    public static void setup() throws InterruptedException {
        remotingServer = createRemotingServer();
        remotingClient = createRemotingClient();
    }

    @Test
    public void testInvokeSync() throws InterruptedException,
            PlatformRemotingConnectException,
            PlatformRemotingSendRequestException, PlatformRemotingConnectException, PlatformRemotingTimeoutException {
        PlatformRemotingCommand request = new PlatformRemotingCommand();
        request.setRemark("李林");
        PlatformRemotingCommand response = remotingClient.invokeSync("localhost:8888", request, 1000 * 3000);
        System.out.println(response.getRemark() + " body:" + response.getBody());

        Thread.sleep(100000);
    }

    @AfterClass
    public static void destroy() {
        remotingClient.shutdown();
        remotingServer.shutdown();
    }

}

