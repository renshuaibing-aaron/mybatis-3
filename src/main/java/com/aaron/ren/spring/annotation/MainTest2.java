package com.aaron.ren.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest2 {

  public static void main(String[] args) {
      //拿到上下文对象
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

      context.register(AppConfig.class);
      context.refresh();

      //数据库查询
      UserMapper service = context.getBean(UserMapper.class);

      System.out.println(service.getAllUser());
  }
}
