package com.aaron.ren.spring.old;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTestOld {

  public static void main(String[] args) {
    //拿到上下文对象


    System.out.println("========【context 开始启动】===========");
    ApplicationContext context = new ClassPathXmlApplicationContext("springmybatisold.xml");
    // 用我们的配置文件来启动一个 ApplicationContext
    System.out.println("========【context 启动成功 准备获取getBean 】===========");

    //数据库查询
    Service service = context.getBean(Service.class);

    System.out.println(service.getUser());
  }
}
