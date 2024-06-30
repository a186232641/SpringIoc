package com.spring.processor;

/**
 * @author 韩飞龙
 * @version 1.0
 * 2024/6/30
 * * 1. 我们根据原生Spring 定义了一个InitializingBean
 *  * 2. 该InitializingBean接口有一个方法void afterPropertiesSet() throws Exception;
 *  * 3. afterPropertiesSet() 在Bean的 setter后执行,即就是我们原来的初始化方法
 *  * 4. 当一个Bean实现这个接口后，就实现afterPropertiesSet() , 这个方法就是初始化方法
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
