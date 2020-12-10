package com.aaron.ren.spring.old;

import com.aaron.ren.normal.common.UserEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class Service {

  public SqlSessionTemplate getSqlSession() {
    return sqlSession;
  }

  public void setSqlSession(SqlSessionTemplate sqlSession) {
    this.sqlSession = sqlSession;
  }

  private SqlSessionTemplate sqlSession;

  @Transactional
  public  UserEntity getUser(){
    UserEntity user2 =sqlSession.selectOne("com.aaron.ren.spring.old.UserMapper.getUser", 1);
    return user2;
  }


  @Transactional
  public  int addUser(){
    UserEntity user=new UserEntity();
    user.setId(66);
    user.setName("renshuabign");
    sqlSession.insert("com.aaron.ren.spring.old.UserMapper.addUser", user);
    System.out.println(1/0);
    return 1;
  }




}
