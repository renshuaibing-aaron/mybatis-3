package com.aaron.ren.plugin;

import com.aaron.ren.normal.common.UserEntity;
import com.aaron.ren.normal.common.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class TestMybatisPlugin {
  public static void main(String[] args) {
    try {
      //配置文件
      String configXml = "mybatis-config.xml";
      //加载配置获取流
      Reader reader = Resources.getResourceAsReader(configXml);
      //获取sqlSessionFactory工厂
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      //获取sqlSession  这一步是关键啊
      SqlSession sqlSession = sqlSessionFactory.openSession();


     List<UserEntity> list= sqlSession.selectList("com.aaron.ren.normal.UserMapper.getAllUser");
      sqlSession.close();

      System.out.println("user2:" + list);
    } catch (IOException e) {
      e.printStackTrace();

    }
    finally {

    }
  }
}
