package com.aaron.ren.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPool implements IConnection {
  // 线程安全的两个容器,分别存放空闲线程数和活动的线程数
  private List<Connection> freeConnection = new CopyOnWriteArrayList<>();
  private List<Connection> activeConnection = new CopyOnWriteArrayList<>();
  // 原子类 标记的是 空闲池的存放的连接数
  private AtomicInteger atomicInteger;

  public ConnectionPool() {

    this.atomicInteger = new AtomicInteger(0);
    // 初始化空闲连接池
    init();
  }

  // 初始空闲连接池
  public void init() {
    // 获取连接数,给freeConnection 池添加指定数量的连接数
    for (int i = 0; i < Integer.valueOf(PropertiesUtil.getValue("initConnections")); i++) {
      // 创建连接
      Connection connection = newConnection();
      if (null != connection) {
        // 添加到容器
        freeConnection.add(connection);
      }
    }
  }

  // 获取连接
  @Override
  public synchronized Connection getConnection() {
    Connection connection = null;
    // 判断是否达到了最大连接数--> 决定给用户连接还是让他等待
    if (atomicInteger.get() < Integer.valueOf(PropertiesUtil.getValue("maxActiveConnetions"))) {
      // 当前小于最大的连接数,直接给当前的用户连接
      if (freeConnection.size() > 0) { // 空闲线程里面有直接从空闲线程里面取
        connection = freeConnection.remove(0);
      } else {  // 空闲线程里面没有,直接创建一个新的连接
        connection = newConnection();
      }
      // 判断连接是否可用
      if (isAvailable(connection)) {
        // 添加到一个活动线程里面
        activeConnection.add(connection);
      } else { // 如果连接不可用,递归
        //  如果连接不可用的话,说明有一次newConnection()失败了,我们得 atomicInteger.decrementAndGet(); 把newConnection()里面的原子增加去掉
        atomicInteger.decrementAndGet();
        connection = getConnection();
      }
    } else {
      // 等待
      try {
        wait(Integer.valueOf(PropertiesUtil.getValue("connTimeOut")));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return connection;
  }

  // 释放本次连接  ; 把本次连接从活动池 转移到 空闲池
  @Override
  public synchronized void releaseConnection(Connection connection) {
    // 判断连接是否可用
    if (isAvailable(connection)) { // 可用
      // 回收
      // 判断空闲池是否满了
      if (freeConnection.size() < Integer.valueOf(PropertiesUtil.getValue("maxConnections"))) {
        // 未满
        freeConnection.add(connection);
      } else {
        // 满了
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      // 移除出去当前的这个连接
      activeConnection.remove(connection);
      atomicInteger.decrementAndGet();
      // 现在可能有连接正在等待,既然这里释放了,那么就唤醒全部等待的线程
      notifyAll();
    } else { // 不可用
      throw new RuntimeException("连接回收异常");
    }
  }

  // 创建新的连接
  public Connection newConnection() {
    try {
      // 注册驱动
      Class.forName(PropertiesUtil.getValue("driverName"));
      // 获取连接
      Connection connection = DriverManager.getConnection(
        PropertiesUtil.getValue("url"),
        PropertiesUtil.getValue("userName"),
        PropertiesUtil.getValue("password"));
      // 原子增加
      atomicInteger.addAndGet(1);
      return connection;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // 判断连接是否可用
  public boolean isAvailable(Connection connection) {
    try {
      if (null == connection || connection.isClosed()) {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return true;
  }
}
