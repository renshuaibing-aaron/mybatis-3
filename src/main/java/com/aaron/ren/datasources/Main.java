package com.aaron.ren.datasources;

import java.sql.*;

public class Main {

  public static void main(String[] args) throws SQLException {
    findAllUsersUseDataSources();
  }

  // 查询所有用户
  public static  void findAllUsersUseDataSources() throws SQLException {
    // 1、使用连接池建立数据库连接
    ConnectionPool dataSource = new ConnectionPool();
    Connection conn = dataSource.getConnection();
    // 2、创建状态
    Statement state = conn.createStatement();
    // 3、查询数据库并返回结果
    ResultSet result = state.executeQuery("select * from users");

    // 4、输出查询结果
    while(result.next()){
      System.out.println(result.getString("email"));
    }
    // 5、断开数据库连接
    result.close();
    state.close();

    // 6、归还数据库连接给连接池
    dataSource.releaseConnection(conn);
  }


  // 查询所有用户
  public void findAllUsersOld() throws SQLException, ClassNotFoundException {
    // 1、装载 sqlserver 驱动对象
    Class.forName("com.mysql.cj.jdbc.Driver");
    // 2、通过 JDBC 建立数据库连接
    Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.2.6:1433;DatabaseName=customer", "sa", "123");
    // 3、创建状态
    Statement state = con.createStatement();
    // 4、查询数据库并返回结果
    ResultSet result = state.executeQuery("select * from users");

    // 5、输出查询结果
    while(result.next()) {
      System.out.println(result.getString("email"));
    }
    // 6、断开数据库连接
    result.close();
    state.close();
    con.close();
  }
}
