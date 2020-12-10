package com.aaron.ren.spring.xmltype;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTestXml {

  public static void main(String[] args) {
    //拿到上下文对象


    System.out.println("========【context 开始启动】===========");
    ApplicationContext context = new ClassPathXmlApplicationContext("springmybatis.xml");
    // 用我们的配置文件来启动一个 ApplicationContext
    System.out.println("========【context 启动成功 准备获取getBean 】===========");

    //数据库查询
    UserMapper service = context.getBean(UserMapper.class);

    //System.out.println(service.getAllUser());
    service.saveUser();
  }
}
