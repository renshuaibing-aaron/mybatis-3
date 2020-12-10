package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 * 首先通过 ParameterHandler 完成 SQL 语句的实参绑定，
 * 然后通过 java.sql.Statement 对象执行 SQL 语句并得到结果集，最后通过 ResultSetHandler 完成结果集的映射，得到结果对象并返回
 * @author Clinton Begin
 */
public interface StatementHandler {

  // sql预编译, 构建statement对象
  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;

  //// 对prepare方法构建的预编译的sql进行参数的设置
  void parameterize(Statement statement)
      throws SQLException;
  // 批量处理器
  void batch(Statement statement)
      throws SQLException;
  // create update delete
  int update(Statement statement)
      throws SQLException;

  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;

  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;
  // 获取sql的封装对象
  BoundSql getBoundSql();
  // 获取参数处理对象
  ParameterHandler getParameterHandler();

}
