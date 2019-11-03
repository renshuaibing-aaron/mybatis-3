package com.aaron.ren.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

  public static void main(String[] args) {
    //拿到上下文对象
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    context.register(AppConfig.class);
    context.refresh();

    //数据库查询
    UserMapper userMapper = context.getBean(UserMapper.class);
    System.out.println(userMapper.findAll());
  }
}
