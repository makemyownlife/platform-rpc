package com.courage.platform.rpc.demo.controller;

import com.courage.platform.client.spring.SpringRpcConsumerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by zhangyong on 2019/7/18.
 */
@Controller
public class HelloController {

    @Autowired
    private SpringRpcConsumerClient springRpcConsumerClient;

    private static String SERVICE_NAME = "platformrpc-demo";

    private static String SERVICE_ID = "demo.hashmap";

    @RequestMapping("/producer")
    @ResponseBody
    public String producer(HttpServletRequest request) {
        try {
            HashMap map = new HashMap();
            map.put("name", "李林");
            map.put("test", "mlife");
            String result = springRpcConsumerClient.executeByServiceName("platformrpc-demo", SERVICE_ID, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "hello";
    }

}
