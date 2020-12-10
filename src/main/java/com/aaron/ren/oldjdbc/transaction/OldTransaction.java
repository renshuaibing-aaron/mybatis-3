package com.aaron.ren.oldjdbc.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OldTransaction {

  public static final String URL = "jdbc:mysql://10.1.168.56:3306/activity?useUnicode=true&characterEncoding=utf-8";
  public static final String USER = "root";
  public static final String PASSWORD = "Abc12345";

  public static void main(String[] args) throws Exception {
    //1.加载驱动程序
    Class.forName("com.mysql.jdbc.Driver");
    //2. 获得数据库连接
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

    try {
      conn.setAutoCommit(false); //开启事务
      //3.操作数据库，实现增删改查
      updateBalance(conn, null, null); //try的最后提交事务


      conn.commit();//try的最后提交事务
    } catch (Exception e) {
      conn.rollback();//回滚事务
    }


  }

  private static void updateBalance(Connection conn, Double balance, String name) {
    try {
      String sql = "UPDATE account SET balance=balance+? WHERE name=?";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setDouble(1, balance);
      pstmt.setString(2, name);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
