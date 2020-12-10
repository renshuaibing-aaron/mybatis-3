package com.aaron.ren.normal.threadnotsafe;

import com.aaron.ren.normal.common.UserEntity;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;

public class TestThreadNotSafe {

  private static SqlSession sqlSession;
  private static CountDownLatch count = new CountDownLatch(10);
  static {
    //配置文件
    String configXml = "mybatis-config.xml";
    //加载配置获取流
    Reader reader = null;
    try {
      reader = Resources.getResourceAsReader(configXml);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //获取sqlSessionFactory工厂
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    //获取sqlSession  这一步是关键啊
     sqlSession = sqlSessionFactory.openSession();

  }


  public static void main(String[] args) {
    UserEntity user=new UserEntity();
    user.setName("renshuabign");


    for(int i=1;i<=10;i++){
      new Thread(()->{
        //sqlSession.insert("com.aaron.ren.normal.common.UserMapper.addUser",user);
        UserEntity user4 = sqlSession.selectOne("com.aaron.ren.normal.common.UserMapper.getUser",160);
        System.out.println("[获取结果]"+user4);
      }).start();
    }
  }


}
