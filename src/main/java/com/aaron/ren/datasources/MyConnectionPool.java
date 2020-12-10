package com.aaron.ren.datasources;

import java.sql.Connection;

public interface MyConnectionPool  {
  /**
   * 获取连接
   * @return
   */
  public Connection getConnection();

  /**
   * 释放连接
   * @param connection
   */
  public void releaseConnection(Connection connection);
}
