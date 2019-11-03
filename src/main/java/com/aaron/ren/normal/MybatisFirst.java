package com.aaron.ren.normal;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MybatisFirst {


  //保存数据库中已有的主键。
  private static List<Integer> allUserId = new ArrayList<Integer>();
  private static final String resource = "SqlMapConfig.xml";
  private SqlSessionFactory sqlSessionFactory;

  MybatisFirst() throws IOException
  {
    InputStream inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  }

  //根据主键选择用户
  public void selectUserById(int selectId)
  {
    SqlSession session = sqlSessionFactory.openSession();
    try{
      User user = session.selectOne("org.mybatis.findUserById", selectId);
      System.out.println("id 为"+selectId+"的User信息为："+user);
    } finally {
      session.close();
    }
  }

  //选取所有的主键
  public void selectAllUserId()
  {
    SqlSession session = sqlSessionFactory.openSession();
    try{
      allUserId = session.selectList("org.mybatis.selectAllUserId");
      System.out.println("所有的ID为：");
      for(Integer integer : allUserId)
      {
        System.out.println(integer);
      }
    } finally {
      session.close();
    }
  }

  //插入一个用户
  public void insertIntoUser(User user)
  {
    SqlSession session = sqlSessionFactory.openSession();
    try {
      session.selectOne("org.mybatis.insertIntoUser", user);
      System.out.println("插入的的User信息为："+user);
    } finally {
      session.close();
    }
  }

  //插入用户List
  public void insertUsers(List<User> userList)
  {
    SqlSession session = sqlSessionFactory.openSession();
    try {
      session.selectOne("org.mybatis.insertUsers", userList);
      System.out.println("插入的Users信息为：");
      for(User user : userList)
      {
        System.out.println(user);
      }
    } finally {
      session.close();
    }
  }

  //更新用户
  public void updateUser(User user)
  {
    SqlSession session = sqlSessionFactory.openSession();
    try {
      session.selectOne("org.mybatis.updateUser", user);
      System.out.println("更新后的用户信息为：" + user);
    } finally {
      session.close();
    }
  }

  //通过主键删除用户
  public void deleteUserById(int deleteId)
  {
    SqlSession session = sqlSessionFactory.openSession();
    try{
      User user = session.selectOne("org.mybatis.deleteUser", deleteId);
      System.out.println(user);
    } finally {
      session.close();
    }
  }

  public void deleteAllUsers()
  {
    SqlSession session = sqlSessionFactory.openSession();
    try{
      for(Integer integer : allUserId)
      {
        User user = session.selectOne("org.mybatis.deleteUser", integer);
        System.out.println(user);
      }
    } finally {
      session.close();
    }
  }


  public boolean isAllUserIdContains(Integer Id)
  {
    for(Integer integer : allUserId)
    {
      if(integer.equals(Id))
      {
        return true;
      }
    }
    return false;
  }

  //随机生成用户
  public List<User> createUser(int userNumber)
  {
    List<User> userList = new ArrayList<User>();
    Random random = new Random();
    for(int i = 0; i < userNumber; i++)
    {
      User user = new User();
      Integer Id = Integer.valueOf(random.nextInt(1000000));

      while(isAllUserIdContains(Id))
      {
        Id = Integer.valueOf(random.nextInt(1000000));
      }
      allUserId.add(Id);
      user.setId(Id);
      user.setUserAge(random.nextInt(100));
      user.setUserName(getRandomString(10));
      user.setUserAddress(getRandomString(10));
      userList.add(user);
    }
    return userList;
  }

  public static String getRandomString(int length)
  {
    String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();

    for(int i = 0; i < length; i++)
    {
      sb.append(str.charAt(random.nextInt(62)));
    }
    return sb.toString();
  }
}
