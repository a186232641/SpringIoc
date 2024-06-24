package com.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 韩飞龙
 * @version 1.0
 * 2024/6/24
 */

/**
 * 保存到运行阶段
 * Target 可以用在类接口上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
   //给注入的bean制指定name
   String value() default "";
}
