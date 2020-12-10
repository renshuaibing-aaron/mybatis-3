package org.apache.ibatis.executor.keygen;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 自增主键接口
 * @author Clinton Begin
 */
public interface KeyGenerator {
  // SQL 执行前
  void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter);
  // SQL 执行后
  void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter);

}
