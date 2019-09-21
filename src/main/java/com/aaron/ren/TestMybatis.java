package com.aaron.ren;

import java.io.IOException;
import java.util.List;

public class TestMybatis {
  public static void main(String[] args) throws IOException
  {
    MybatisFirst mybatis = new MybatisFirst();;
    mybatis.deleteAllUsers();
    mybatis.selectAllUserId();
    List<User> userList = mybatis.createUser(20);
    User user = new User(3, "l84102261", 21, "Xi'an");
    mybatis.updateUser(user);
    mybatis.selectUserById(3);
    mybatis.insertUsers(userList);
    mybatis.deleteAllUsers();
    mybatis.insertIntoUser(user);

  }
}
