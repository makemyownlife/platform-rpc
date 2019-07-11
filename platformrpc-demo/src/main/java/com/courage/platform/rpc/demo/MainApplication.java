package com.courage.platform.rpc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhangyong on 2019/7/3.
 */
@SpringBootApplication
@ImportResource(locations = "spring-app.xml")
public class MainApplication {

    private final static Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("启动服务");
        SpringApplication.run(MainApplication.class, args);
        logger.info("结束启动服务,耗时:" + (System.currentTimeMillis() - start));
    }

}
