package com.spring.IOC;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * @author 韩飞龙
 * @version 1.0
 * 2024/6/29
 */

public class BeanDefintion {
   private Class clazz;
   private String scope;

   public Class getClazz() {
      return clazz;
   }

   public void setClazz(Class clazz) {
      this.clazz = clazz;
   }

   public String getScope() {
      return scope;
   }

   public void setScope(String scope) {
      this.scope = scope;
   }
}
