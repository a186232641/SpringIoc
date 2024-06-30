package com.spring.IOC;

import com.spring.annotation.*;
import com.spring.processor.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    private final ConcurrentHashMap<String, Object> ioc = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefintion> beanDefintionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public SpringApplicationContext(Class configClass) {


    }

    /**
     * 扫描指定包下带有注解的bean
     *
     * @param configClass
     */
    public void beanDefintionsByScan(Class configClass) {
        this.configClass = configClass;
        ComponentScan componentScan = (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScan.value();
        ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
        path = path.replace(".", "/");
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String absolutePath = f.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    String className = absolutePath.substring(absolutePath.lastIndexOf("/") + 1, absolutePath.indexOf(".class"));
                    String classFullName = path.replace("/", ".") + "." + className;

                    try {
                        Class<?> aClass = classLoader.loadClass(classFullName);
                        if (aClass.isAnnotationPresent(Component.class)) {
                            Component declaredAnnotation = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = declaredAnnotation.value();
                            if ("".equals(beanName)) {
                                beanName = StringUtils.uncapitalize(beanName);
                            }
                            BeanDefintion beanDefintion = new BeanDefintion();
                            beanDefintion.setClazz(aClass);
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scopeValue = aClass.getDeclaredAnnotation(Scope.class);
                                beanDefintion.setScope(scopeValue.value());
                            } else {
                                beanDefintion.setScope("singleton");
                            }
                            beanDefintionMap.put(beanName, beanDefintion);
                        }

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }


                }
            }

        }
    }

    private Object createbean(String beanName, BeanDefintion beanDefintion) {
        Class clazz = beanDefintion.getClazz();
        //设置属性值
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    //3. 得到这个字段名字
                    String name = declaredField.getName();
                    //4. 通过getBean方法来获取要组装对象
                    Object bean = getBean(name);
                    //5. 进行组装
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }
            //是否实现InitializingBean接口
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Object getBean(String name) {
        if (beanDefintionMap.containsKey(name)) {
            BeanDefintion beanDefintion = beanDefintionMap.get(name);
            if ("singleton".equalsIgnoreCase(beanDefintion.getScope())) {
                return singletonObjects.get(name);
            } else {
                return createbean(name, beanDefintion);
            }
        } else {
            throw new NullPointerException("没有该bean");
        }
    }
}

