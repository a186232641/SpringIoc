package com.spring.IOC;

import com.spring.annotation.Component;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 韩飞龙
 * @version 1.0
 * 2024/6/24
 */
public class SpringApplicationContext {
    private Class configClass;
    private final ConcurrentHashMap<String, Objects> ioc = new ConcurrentHashMap<>();

    public SpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        //拿到指定注解
        Component declaredAnnotation = (Component)this.configClass.getDeclaredAnnotation(Component.class);
       //获取注解的值
        String path = declaredAnnotation.value();
      //得到类加载器
        ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
        //通过类加载器获得扫描包的url
        path = path.replace(".","/");
        URL resource = classLoader.getResource(path);

    }
}

