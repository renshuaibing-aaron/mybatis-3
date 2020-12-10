package com.aaron.ren.datasources;

import java.sql.Connection;

public interface IConnection {

  public  void releaseConnection(Connection connection);
  public  Connection getConnection();
}
