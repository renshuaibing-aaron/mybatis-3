package com.aaron.ren.normal;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class TestMyBatis {

  public static void main(String[] args) {
    try {
      //配置文件
      String configXml = "mybatis-config.xml";
      //加载配置获取流
      Reader reader = Resources.getResourceAsReader(configXml);
      //获取sqlSessionFactory工厂
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      //获取sqlSession
      SqlSession sqlSession = sqlSessionFactory.openSession();


      //获取对应的mapper
      UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
      //执行方法
      UserEntity user = userMapper.getUser(1);


      System.out.println("name:" + user.getName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
