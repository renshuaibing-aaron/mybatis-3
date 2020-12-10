package com.aaron.ren.spring.sqlsessiontemplate;

import com.aaron.ren.normal.common.UserEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTestswssion {

  public static void main(String[] args) {
    //拿到上下文对象

    System.out.println("========【context 开始启动】===========");
    ApplicationContext context = new ClassPathXmlApplicationContext("springmybatissqltemplate.xml");
    // 用我们的配置文件来启动一个 ApplicationContext
    System.out.println("========【context 启动成功 准备获取getBean 】===========");

    //数据库查询
    SqlSessionTemplate template = (SqlSessionTemplate) context.getBean("sqlSessionTemplate");

    UserEntity user4 = template.selectOne("com.aaron.ren.normal.UserMapper.getUser",1);

  }
}
