package com.courage.platform.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 远程方法同步调用时间
 * 张勇
 * 2019-07-05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSAnnotation {

    String value();//远程服务ID

    long retryTimes() default 2;//调用失败 重试次数

    long readTimeout() default 15;//读取超时时间

    String remark() default "";//备注

}