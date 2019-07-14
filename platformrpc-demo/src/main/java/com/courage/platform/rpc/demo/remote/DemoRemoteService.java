package com.courage.platform.rpc.demo.remote;

import com.courage.platform.client.annotations.RSAnnotation;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zhangyong on 2019/7/14.
 */
@Component
public class DemoRemoteService {

    @RSAnnotation(value = "demo.mylife")
    public String mylife(String param) {
        System.out.println("param:" + param);
        return "lilin" + "" + param;
    }

    @RSAnnotation(value = "demo.hashmap")
    public String hashmap(Map hashmap) {
        System.out.println("param:" + hashmap);
        return "lilin" + "" + hashmap;
    }

}
