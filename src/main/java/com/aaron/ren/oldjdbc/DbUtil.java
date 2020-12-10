package com.aaron.ren.oldjdbc;

import java.sql.*;

public class DbUtil {

  public static final String URL = "jdbc:mysql://10.1.168.56:3306/activity?useUnicode=true&characterEncoding=utf-8";
  public static final String USER = "root";
  public static final String PASSWORD = "Abc12345";

  public static void main(String[] args) throws Exception {
    //1.加载驱动程序
    Class.forName("com.mysql.jdbc.Driver");
    //2. 获得数据库连接
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

    //3.操作数据库，实现增删改查
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT name, id FROM user");



    String sql="select id,name from user where id>?";
    //通过connection 获取prepareStatement对象对sql语句进行预编译 防止sql注入
    PreparedStatement ps = conn.prepareStatement(sql);
    //paramenterindex:1  表示第一个占位符所需要输入的数值
    ps.setString(1,"2");
    ResultSet resultSet = ps.executeQuery();



    //如果有数据，rs.next()返回true
    while(rs.next()){
      System.out.println(rs.getString("name")+" 年龄："+rs.getInt("id"));
    }
  }
}
